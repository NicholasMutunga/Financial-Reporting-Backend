package com.emtechhouse.Ticket.status.StatusController;


import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.priority.PriorityRepository.PriorityRepository;
import com.emtechhouse.Ticket.priority.PriorityService.PriorityService;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import com.emtechhouse.Ticket.status.StatusRepository.StatusRepository;
import com.emtechhouse.Ticket.status.StatusService.StatusService;
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
@RequestMapping("system/ticketing/status")
public class StatusController {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StatusService statusService;

    @PostMapping("/add")
    public ResponseEntity<?> addStatus(@RequestBody Status status) {
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
                    Optional<Status> checkStatus = statusRepository.findByEntityIdAndStatusCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), status.getStatusCode(), 'N');
                    if (checkStatus.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("STATUS WITH CODE " + checkStatus.get().getStatusCode() + " AND NAME " + checkStatus.get().getTitle() + " ALREADY CREATED ON " + checkStatus.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        status.setPostedBy(UserRequestContext.getCurrentUser());
                        status.setEntityId(EntityRequestContext.getCurrentEntityId());
                        status.setPostedFlag('Y');
                        status.setModifiedBy("System");
                        status.setPostedTime(new Date());
                        Status newStatus = statusService.addStatus(status);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " CREATED SUCCESSFULLY AT " + status.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newStatus);
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
    public ResponseEntity<?> updateStatus(@RequestBody Status status) {
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
                    status.setModifiedBy(UserRequestContext.getCurrentUser());
                    status.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Status> theStatus = statusRepository.findById(status.getId());
                    if (theStatus.isPresent()) {
                        status.setPostedTime(theStatus.get().getPostedTime());
                        status.setPostedFlag(theStatus.get().getPostedFlag());
                        status.setPostedBy(theStatus.get().getPostedBy());
                        status.setModifiedFlag('Y');
                        status.setVerifiedFlag('N');
                        status.setModifiedTime(new Date());
                        status.setModifiedBy(status.getModifiedBy());
                        statusService.updateStatus(status);
                        EntityResponse response = new EntityResponse();
                        log.info("STATUS WITH CODE " + status.getStatusCode() + " AND NAME: " + status.getTitle() + " MODIFIED SUCCESSFULLY AT " + status.getModifiedTime());
                        response.setMessage("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " MODIFIED SUCCESSFULLY AT " + status.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(status);
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
                    List<Status> status = statusRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (status.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(status);
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
    public ResponseEntity<?> getStatusById(@PathVariable("id") Long id) {
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
                    Status status = statusService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(status);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/by/status/{code}")
    public ResponseEntity<?> getStatusByCode(@PathVariable("code") String statusCode) {
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
                    Optional<Status> searchCode = statusRepository.findByEntityIdAndStatusCode(EntityRequestContext.getCurrentEntityId(), statusCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Status> status = statusRepository.findByStatusCode(statusCode);
                        response.setMessage("STATUS WITH CODE " + statusCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(status);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("STATUS WITH CODE " + statusCode + " AVAILABLE FOR REGISTRATION");
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
                    Optional<Status> theStatus = statusRepository.findById(Long.parseLong(id));
                    if (theStatus.isPresent()) {
                        Status status = theStatus.get();
                        if (status.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else if (status.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (status.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    status.setVerifiedFlag('Y');
                                    status.setVerifiedTime(new Date());
                                    status.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    statusRepository.save(status);
                                    EntityResponse response = new EntityResponse();
                                    log.info("STATUS WITH CODE " + status.getStatusCode() + " AND NAME: " + status.getTitle() + " VERIFIED SUCCESSFULLY AT " + status.getVerifiedTime());
                                    response.setMessage("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " VERIFIED SUCCESSFULLY AT " + status.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(status);
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
    public ResponseEntity<?> deleteStatus(@PathVariable Long id) {
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
                    Optional<Status> theStatus = statusRepository.findById(id);
                    if (theStatus.isPresent()) {
                        Status status = theStatus.get();
                        status.setDeletedFlag('Y');
                        status.setDeletedTime(new Date());
                        status.setDeletedBy(UserRequestContext.getCurrentUser());
                        statusRepository.save(status);
                        EntityResponse response = new EntityResponse();
                        log.info("STATUS WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " DELETED SUCCESSFULLY AT " + status.getDeletedTime());
                        response.setMessage("STATUS  WITH CODE " + status.getStatusCode() + " AND NAME " + status.getTitle() + " DELETED SUCCESSFULLY AT " + status.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(status);
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
