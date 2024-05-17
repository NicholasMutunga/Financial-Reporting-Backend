package com.emtechhouse.Ticket.faq.FaqController;


import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.faq.FaqModel.Faq;
import com.emtechhouse.Ticket.faq.FaqRepository.FaqRepository;
import com.emtechhouse.Ticket.faq.FaqService.FaqService;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.priority.PriorityRepository.PriorityRepository;
import com.emtechhouse.Ticket.priority.PriorityService.PriorityService;
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
@RequestMapping("system/ticketing/faq")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private FaqService faqService;


    @PostMapping("/add")
    public ResponseEntity<?> addFaq(@RequestBody Faq faq) {
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
                    Optional<Faq> checkFaq = faqRepository.findByEntityIdAndFaqCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), faq.getFaqCode(), 'N');
                    if (checkFaq.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("FAQ WITH CODE " + checkFaq.get().getFaqCode() + " AND NAME " + checkFaq.get().getTitle() + " ALREADY CREATED ON " + checkFaq.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        faq.setPostedBy(UserRequestContext.getCurrentUser());
                        faq.setEntityId(EntityRequestContext.getCurrentEntityId());
                        faq.setPostedFlag('Y');
                        faq.setModifiedBy("System");
                        faq.setPostedTime(new Date());
                        Faq newFaq = faqService.addFaq(faq);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " CREATED SUCCESSFULLY AT " + faq.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newFaq);
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
    public ResponseEntity<?> updateFaq(@RequestBody Faq faq) {
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
                    faq.setModifiedBy(UserRequestContext.getCurrentUser());
                    faq.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Faq> theFaq = faqRepository.findById(faq.getId());
                    if (theFaq.isPresent()) {
                        faq.setPostedTime(theFaq.get().getPostedTime());
                        faq.setPostedFlag(theFaq.get().getPostedFlag());
                        faq.setPostedBy(theFaq.get().getPostedBy());
                        faq.setModifiedFlag('Y');
                        faq.setVerifiedFlag('N');
                        faq.setModifiedTime(new Date());
                        faq.setModifiedBy(faq.getModifiedBy());
                        faqService.updateFaq(faq);
                        EntityResponse response = new EntityResponse();
                        log.info("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME: " + faq.getTitle() + " MODIFIED SUCCESSFULLY AT " + faq.getModifiedTime());
                        response.setMessage("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " MODIFIED SUCCESSFULLY AT " + faq.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(faq);
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
                    List<Faq> faq = faqRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (faq.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(faq);
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
    public ResponseEntity<?> getFaqById(@PathVariable("id") Long id) {
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
                    Faq faq = faqService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(faq);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/by/faq/{code}")
    public ResponseEntity<?> getFaqByCode(@PathVariable("code") String faqCode) {
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
                    Optional<Faq> searchCode = faqRepository.findByEntityIdAndFaqCode(EntityRequestContext.getCurrentEntityId(), faqCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Faq> faq = faqRepository.findByFaqCode(faqCode);
                        response.setMessage("FAQ WITH CODE " + faqCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(faq);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("FAQ WITH CODE " + faqCode + " AVAILABLE FOR REGISTRATION");
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
                    Optional<Faq> theFaq = faqRepository.findById(Long.parseLong(id));
                    if (theFaq.isPresent()) {
                        Faq faq = theFaq.get();
                        if (faq.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (faq.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (faq.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    faq.setVerifiedFlag('Y');
                                    faq.setVerifiedTime(new Date());
                                    faq.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    faq.setEntityId(EntityRequestContext.getCurrentEntityId());
                                    faqRepository.save(faq);
                                    EntityResponse response = new EntityResponse();
                                    log.info("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME: " + faq.getTitle() + " VERIFIED SUCCESSFULLY AT " + faq.getVerifiedTime());
                                    response.setMessage("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " VERIFIED SUCCESSFULLY AT " + faq.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(faq);
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
    public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
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
                    Optional<Faq> theFaq = faqRepository.findById(id);
                    if (theFaq.isPresent()) {
                        Faq faq = theFaq.get();
                        faq.setDeletedFlag('Y');
                        faq.setDeletedTime(new Date());
                        faq.setDeletedBy(UserRequestContext.getCurrentUser());
                        faq.setEntityId(EntityRequestContext.getCurrentEntityId());
                        faqRepository.save(faq);
                        EntityResponse response = new EntityResponse();
                        log.info("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " DELETED SUCCESSFULLY AT " + faq.getDeletedTime());
                        response.setMessage("FAQ WITH CODE " + faq.getFaqCode() + " AND NAME " + faq.getTitle() + " DELETED SUCCESSFULLY AT " + faq.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(faq);
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
