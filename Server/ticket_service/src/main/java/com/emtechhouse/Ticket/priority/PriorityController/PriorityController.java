package com.emtechhouse.Ticket.priority.PriorityController;


import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
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
@RequestMapping("system/ticketing/priority")
public class PriorityController {


    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private PriorityService priorityService;

    @PostMapping("/add")
    public ResponseEntity<?> addPriority(@RequestBody Priority priority) {
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
                    Optional<Priority> checkPriority = priorityRepository.findByEntityIdAndPriorityCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), priority.getPriorityCode(), 'N');
                    if (checkPriority.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("PRIORITY WITH CODE " + checkPriority.get().getPriorityCode() + " AND NAME " + checkPriority.get().getTitle() + " ALREADY CREATED ON " + checkPriority.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        priority.setPostedBy(UserRequestContext.getCurrentUser());
                        priority.setEntityId(EntityRequestContext.getCurrentEntityId());
                        priority.setPostedFlag('Y');
                        priority.setModifiedBy("System");
                        priority.setPostedTime(new Date());
                        Priority newPriority = priorityService.addPriority(priority);
                        EntityResponse response = new EntityResponse();
                        System.out.println("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " CREATED SUCCESSFULLY AT " + priority.getPostedTime());
                        response.setMessage("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " CREATED SUCCESSFULLY AT " + priority.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newPriority);
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
    public ResponseEntity<?> updatePriority(@RequestBody Priority priority) {
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
                    priority.setModifiedBy(UserRequestContext.getCurrentUser());
                    priority.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Priority> thePriority = priorityRepository.findById(priority.getId());
                    if (thePriority.isPresent()) {
                        priority.setPostedTime(thePriority.get().getPostedTime());
                        priority.setPostedFlag(thePriority.get().getPostedFlag());
                        priority.setPostedBy(thePriority.get().getPostedBy());
                        priority.setModifiedFlag('Y');
                        priority.setVerifiedFlag('N');
                        priority.setModifiedTime(new Date());
                        priority.setModifiedBy(priority.getModifiedBy());
                        priorityService.updatePriority(priority);
                        EntityResponse response = new EntityResponse();
                        log.info("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME: " + priority.getTitle() + " MODIFIED SUCCESSFULLY AT " + priority.getModifiedTime());
                        response.setMessage("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " MODIFIED SUCCESSFULLY AT " + priority.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(priority);
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
    public ResponseEntity<?> getAllPriorities() {
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
                    List<Priority> priority = priorityRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (priority.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(priority);
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
    public ResponseEntity<?> getPriorityById(@PathVariable("id") Long id) {
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
                    Priority priority = priorityService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(priority);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/find/by/priority/{code}")
    public ResponseEntity<?> getPriorityByCode(@PathVariable("code") String priorityCode) {
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
                    Optional<Priority> searchCode = priorityRepository.findByEntityIdAndPriorityCode(EntityRequestContext.getCurrentEntityId(), priorityCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Priority> priority = priorityRepository.findByPriorityCode(priorityCode);
                        response.setMessage("PRIORITY WITH CODE " + priorityCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(priority);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("PRIORITY WITH CODE " + priorityCode + " AVAILABLE FOR REGISTRATION");
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
                    Optional<Priority> thePriority = priorityRepository.findById(Long.parseLong(id));
                    if (thePriority.isPresent()) {
                        Priority priority = thePriority.get();

                        if (priority.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                            if (priority.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (priority.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    priority.setVerifiedFlag('Y');
                                    priority.setVerifiedTime(new Date());
                                    priority.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    priorityRepository.save(priority);
                                    EntityResponse response = new EntityResponse();
                                    log.info("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME: " + priority.getTitle() + " VERIFIED SUCCESSFULLY AT " + priority.getVerifiedTime());
                                    response.setMessage("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " VERIFIED SUCCESSFULLY AT " + priority.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(priority);
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
    public ResponseEntity<?> deletePriority(@PathVariable Long id) {
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
                    Optional<Priority> thePriority = priorityRepository.findById(id);
                    if (thePriority.isPresent()) {
                        Priority priority = thePriority.get();
                        priority.setDeletedFlag('Y');
                        priority.setDeletedTime(new Date());
                        priority.setDeletedBy(UserRequestContext.getCurrentUser());
                        priorityRepository.save(priority);
                        EntityResponse response = new EntityResponse();
                        log.info("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " DELETED SUCCESSFULLY AT " + priority.getDeletedTime());
                        response.setMessage("PRIORITY WITH CODE " + priority.getPriorityCode() + " AND NAME " + priority.getTitle() + " DELETED SUCCESSFULLY AT " + priority.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(priority);
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
