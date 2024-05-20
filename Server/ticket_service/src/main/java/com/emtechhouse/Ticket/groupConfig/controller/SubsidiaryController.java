package com.emtechhouse.Ticket.groupConfig.controller;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.Utils.CONSTANTS;
import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import com.emtechhouse.Ticket.groupConfig.repo.SubsidiaryRepo;
import com.emtechhouse.Ticket.groupConfig.service.SubsidiaryService;
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
@RequestMapping("/api/v1/subsidiary")
public class SubsidiaryController {
//    @Autowired
//     Subsidiary subsidiary;
    @Autowired
 SubsidiaryRepo subsidiaryRepo;
    @Autowired
 SubsidiaryService subsidiaryService;

    @PostMapping("/add")
    public ResponseEntity<EntityResponse> addSubsidiary(@RequestBody Subsidiary subsidiary) {
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
                    Optional<Subsidiary> checkSubsidiary = subsidiaryRepo.findByCompanyName(subsidiary.getCompanyName());
                    if (checkSubsidiary.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Subsidiary with Company Name " + checkSubsidiary.get().getCompanyName() + " ALREADY CREATED ON " + checkSubsidiary.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);

                    } else {
                        System.out.println("System is okay");
                        subsidiary.setPostedBy(UserRequestContext.getCurrentUser());
                        subsidiary.setEntityId(EntityRequestContext.getCurrentEntityId());
                        subsidiary.setPostedFlag('Y');
                        subsidiary.setEntityId("001");
                        subsidiary.setModifiedBy("System");
                        subsidiary.setPostedTime(new Date());
                        Subsidiary newSubsidiary = subsidiaryService.addSubsidiary(subsidiary);
                        EntityResponse response = new EntityResponse();
                        System.out.println("Subsidiary with Company Name " + newSubsidiary.getCompanyName() + " CREATED SUCCESSFULLY AT " + newSubsidiary.getPostedTime());
                        response.setMessage("Subsidiary with Company Name " + newSubsidiary.getCompanyName() + " CREATED SUCCESSFULLY AT " + newSubsidiary.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newSubsidiary);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.error(" Error {} " + e);
            return null;

        }
    }
        @PutMapping("/modify")
        public ResponseEntity<?> updateSubsidiary(@RequestBody Subsidiary subsidiary) {
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
                        Optional<Subsidiary> existingSubsidiary = subsidiaryRepo.findById(subsidiary.getId());
                        if (existingSubsidiary.isPresent()) {

                            subsidiary.setModifiedFlag('Y');
                            subsidiary.setVerifiedFlag('N');
                            subsidiary.setModifiedTime(new Date());
                            subsidiary.setModifiedBy(UserRequestContext.getCurrentUser());
                            subsidiary.setEntityId(EntityRequestContext.getCurrentEntityId());
                            subsidiaryService.updateSubsidiary(subsidiary);
                            EntityResponse response = new EntityResponse();
                            log.info("Subsidiary with Id " + subsidiary.getId() + " MODIFIED SUCCESSFULLY AT " + subsidiary.getModifiedTime());
                            response.setMessage("Subsidiary with Id " + subsidiary.getId() + " MODIFIED SUCCESSFULLY AT " + subsidiary.getModifiedTime());
                            response.setStatusCode(HttpStatus.OK.value());
                            response.setEntity(subsidiary);
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSubsidiary(@PathVariable Long id) {
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
                    Optional<Subsidiary> optionalSubsidiary = subsidiaryRepo.findById(id);
                    if (optionalSubsidiary.isPresent()) {
                        Subsidiary subsidiary = optionalSubsidiary.get();
                        subsidiary.setDeletedFlag('Y');
                        subsidiary.setDeletedTime(new Date());
                        subsidiary.setDeletedBy(UserRequestContext.getCurrentUser());
                        subsidiaryRepo.save(subsidiary);
                        EntityResponse response = new EntityResponse();
                        log.info("Subsidiary with Id " + subsidiary.getId() + " AND NAME " + subsidiary.getCompanyName() + " DELETED SUCCESSFULLY AT " + subsidiary.getDeletedTime());
                        response.setMessage("Subsidiary with Id " + subsidiary.getId() + " AND NAME " + subsidiary.getId() + " DELETED SUCCESSFULLY AT " + subsidiary.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(subsidiary);
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
    public ResponseEntity<?> getAllSubsidiaries() {
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
                    List<Subsidiary> subsidiaries = subsidiaryRepo.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (subsidiaries.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(subsidiaries);
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
    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verifySubsidiary(@PathVariable String id) {
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
                    Optional<Subsidiary> optionalSubsidiary = subsidiaryRepo.findById(Long.parseLong(id));
                    if (optionalSubsidiary.isPresent()) {
                        Subsidiary subsidiary = optionalSubsidiary.get();

                        if (subsidiary.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (subsidiary.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (subsidiary.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("Subsidiary with Company name " + subsidiary.getCompanyName() + " AND Id " + subsidiary.getId() + " ALREADY VERIFIED");
                                response.setMessage("Subsidiary with Company name " + subsidiary.getCompanyName() + " AND Id " + subsidiary.getId() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    subsidiary.setVerifiedFlag('Y');
                                    subsidiary.setVerifiedTime(new Date());
                                    subsidiary.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    subsidiaryRepo.save(subsidiary);
                                    EntityResponse response = new EntityResponse();
                                    log.info("Subsidiry with name " + subsidiary.getCompanyName() + " AND Id " + subsidiary.getId() + " VERIFIED SUCCESSFULLY AT " + subsidiary.getVerifiedTime());
                                    response.setMessage("Subsidiry with name " + subsidiary.getCompanyName() + " AND Id " + subsidiary.getId() + " VERIFIED SUCCESSFULLY AT " + subsidiary.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(subsidiary);
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
    @GetMapping("/find/by/subsidiary/{code}")
    public ResponseEntity<?> SubsidiaryByCode(@PathVariable("code") String subsidiaryCode) {
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
                }
                else

                {

//                    Optional<Subsidiary> searchCodes = subsidiaryRepo.findBySubsidiaryCode(subsidiaryCode);
//                    if (!searchCodes.isPresent()) {
//                        EntityResponse response = new EntityResponse();
//                       // Optional<Subsidiary> subsidiary = subsidiaryRepo.findBySubsidiaryCode(subsidiaryCode);
//                        response.setMessage("Subsidiary code " + searchCodes + " does not exist");
//                        response.setStatusCode(HttpStatus.OK.value());
//                        response.setEntity("");
//                        return new ResponseEntity<>(response, HttpStatus.OK);
//                    }
//                    else
                    {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Subsidiary with code " + subsidiaryCode + " retrieved");
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







