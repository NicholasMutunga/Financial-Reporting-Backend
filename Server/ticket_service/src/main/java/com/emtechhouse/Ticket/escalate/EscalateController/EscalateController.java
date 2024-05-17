package com.emtechhouse.Ticket.escalate.EscalateController;


import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.escalate.EscalateModel.Escalate;
import com.emtechhouse.Ticket.escalate.EscalateRepository.EscalateRepository;
import com.emtechhouse.Ticket.escalate.EscalateService.EscalateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("system/ticketing/escalate/issue")
public class EscalateController {
        @Autowired
        private EscalateRepository escalateRepository;

        @Autowired
        private EscalateService escalateService;


        @PostMapping("/add")
        public ResponseEntity<?> addIssue(@RequestBody Escalate escalate) {
            try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
                {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                else
                    {
                        Optional<Escalate> checkIssue = escalateRepository.findByEntityIdAndEscalateCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), escalate.getEscalateCode(), 'N');
                        if (checkIssue.isPresent()) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("ISSUE WITH CODE " + checkIssue.get().getEscalateCode() + " AND NAME " + checkIssue.get().getTitle() + " ALREADY CREATED ON " + checkIssue.get().getPostedTime());
                            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else {
                            escalate.setPostedBy(UserRequestContext.getCurrentUser());
                            escalate.setEntityId(EntityRequestContext.getCurrentEntityId());
                            escalate.setPostedFlag('Y');
                            escalate.setModifiedBy("System");
                            escalate.setPostedTime(new Date());
                            Escalate newIssue = escalateService.addIssue(escalate);
                            EntityResponse response = new EntityResponse();
                            response.setMessage("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " CREATED SUCCESSFULLY AT " + escalate.getPostedTime());
                            response.setStatusCode(HttpStatus.CREATED.value());
                            response.setEntity(newIssue);
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }
                    }
                }
            } catch (Exception e) {
                log.info("Catched Error {} " + e);
                return null;
            }
        }

    @PutMapping("/modify")
    public ResponseEntity<?> updateIssue(@RequestBody Escalate escalate) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    escalate.setModifiedBy(UserRequestContext.getCurrentUser());
                    escalate.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Escalate> theIssue = escalateRepository.findById(escalate.getId());
                    if (theIssue.isPresent()) {
                        escalate.setPostedTime(theIssue.get().getPostedTime());
                        escalate.setPostedFlag(theIssue.get().getPostedFlag());
                        escalate.setPostedBy(theIssue.get().getPostedBy());
                        escalate.setModifiedFlag('Y');
                        escalate.setVerifiedFlag('N');
                        escalate.setModifiedTime(new Date());
                        escalate.setModifiedBy(escalate.getModifiedBy());
                        escalateService.updateIssue(escalate);
                        EntityResponse response = new EntityResponse();
                        log.info("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME: " + escalate.getTitle() + " MODIFIED SUCCESSFULLY AT " + escalate.getModifiedTime());
                        response.setMessage("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " MODIFIED SUCCESSFULLY AT " + escalate.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(escalate);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllMissectors() {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    List<Escalate> escalate = escalateRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (escalate.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(escalate);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<?> getIssueById(@PathVariable("id") Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Escalate escalate = escalateService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(escalate);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/by/issue/{code}")
    public ResponseEntity<?> getIssueByCode(@PathVariable("code") String escalateCode) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Optional<Escalate> searchCode = escalateRepository.findByEntityIdAndEscalateCode(EntityRequestContext.getCurrentEntityId(), escalateCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Escalate> escalate = escalateRepository.findByEscalateCode(escalateCode);
                        response.setMessage("ISSUE WITH CODE " + escalateCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(escalate);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("ISSUE WITH CODE " + escalateCode + " AVAILABLE FOR REGISTRATION");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verify(@PathVariable String id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    Optional<Escalate> theIssue = escalateRepository.findById(Long.parseLong(id));
                    if (theIssue.isPresent()) {
                        Escalate escalate = theIssue.get();
                        if (escalate.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (escalate.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (escalate.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    escalate.setVerifiedFlag('Y');
                                    escalate.setVerifiedTime(new Date());
                                    escalate.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    escalate.setEntityId(EntityRequestContext.getCurrentEntityId());
                                    escalateRepository.save(escalate);
                                    EntityResponse response = new EntityResponse();
                                    log.info("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME: " + escalate.getTitle() + " VERIFIED SUCCESSFULLY AT " + escalate.getVerifiedTime());
                                    response.setMessage("CATEGORY WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " VERIFIED SUCCESSFULLY AT " + escalate.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(escalate);
                                    return new ResponseEntity<>(response, HttpStatus.OK);
                                }

                            }

                        }

                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else
                {
                    Optional<Escalate> theIssue = escalateRepository.findById(id);
                    if (theIssue.isPresent()) {
                        Escalate escalate = theIssue.get();
                        escalate.setDeletedFlag('Y');
                        escalate.setDeletedTime(new Date());
                        escalate.setDeletedBy(UserRequestContext.getCurrentUser());
                        escalate.setEntityId(EntityRequestContext.getCurrentEntityId());
                        escalateRepository.save(escalate);
                        EntityResponse response = new EntityResponse();
                        log.info("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " DELETED SUCCESSFULLY AT " + escalate.getDeletedTime());
                        response.setMessage("ISSUE WITH CODE " + escalate.getEscalateCode() + " AND NAME " + escalate.getTitle() + " DELETED SUCCESSFULLY AT " + escalate.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(escalate);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


}
