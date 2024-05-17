package com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeController;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeRepository.TicketAssigneeRepository;
import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeService.TicketAssigneeService;
import com.emtechhouse.Ticket.Utils.CONSTANTS;
import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
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
@RequestMapping("system/ticketing/ticketAssignee")
public class TicketAssigneeController {

    @Autowired
    TicketAssigneeRepository ticketAssigneeRepository;

    @Autowired
    TicketAssigneeService ticketAssigneeService;

    @PostMapping("/add")
    public ResponseEntity<?> addTicketAssignee(@RequestBody TicketAssignee ticketAssignee) {
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
                    Optional<TicketAssignee> checkAssigneeByEmail = ticketAssigneeRepository.findByEmail(ticketAssignee.getEmail());
                    if (checkAssigneeByEmail.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Assignee with Email " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " ALREADY CREATED ON " + ticketAssignee.getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else if (!ticketAssignee.getEmail().contains(CONSTANTS.MAIL_FORMAT)){
                        EntityResponse response = new EntityResponse();
                        response.setMessage("The email format is NOT Acceptable. Use email with format: "+CONSTANTS.MAIL_FORMAT);
                        System.out.println("The email format is NOT Acceptable");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        ticketAssignee.setNames(ticketAssignee.getNames());
                        ticketAssignee.setDesignation(ticketAssignee.getDesignation());
                        ticketAssignee.setEmail(ticketAssignee.getEmail());
                        ticketAssignee.setNationalId(ticketAssignee.getNationalId());
                        ticketAssignee.setPhoneNumber(ticketAssignee.getPhoneNumber());
                        ticketAssignee.setAvaya(ticketAssignee.getAvaya());
                        ticketAssignee.setPostedBy(UserRequestContext.getCurrentUser());
                        ticketAssignee.setEntityId(EntityRequestContext.getCurrentEntityId());
                        ticketAssignee.setPostedFlag('Y');
                        ticketAssignee.setModifiedBy("System");
                        ticketAssignee.setPostedTime(new Date());
                        TicketAssignee newTicketAssignee = ticketAssigneeService.addTicketAssignee(ticketAssignee);
                        EntityResponse response = new EntityResponse();
                        System.out.println("Ticket Assignee with Email " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " CREATED SUCCESSFULLY AT " + ticketAssignee.getPostedTime());
                        response.setMessage("Ticket Assignee with Email " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " CREATED SUCCESSFULLY AT " + ticketAssignee.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newTicketAssignee);
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
    public ResponseEntity<?> updateTicketAssignee(@RequestBody TicketAssignee ticketAssignee) {
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
                    Optional<TicketAssignee> theAssignee = ticketAssigneeRepository.findById(ticketAssignee.getId());
                    if (theAssignee.isPresent()) {
                        ticketAssignee.setNames(ticketAssignee.getNames());
                        ticketAssignee.setDesignation(ticketAssignee.getDesignation());
                        ticketAssignee.setEmail(ticketAssignee.getEmail());
                        ticketAssignee.setNationalId(ticketAssignee.getNationalId());
                        ticketAssignee.setPhoneNumber(ticketAssignee.getPhoneNumber());
                        ticketAssignee.setAvaya(ticketAssignee.getAvaya());
                        ticketAssignee.setModifiedFlag('Y');
                        ticketAssignee.setVerifiedFlag('N');
                        ticketAssignee.setModifiedTime(new Date());
                        ticketAssignee.setModifiedBy(UserRequestContext.getCurrentUser());
                        ticketAssignee.setEntityId(EntityRequestContext.getCurrentEntityId());
                        ticketAssigneeService.updateTicketAssignee(ticketAssignee);
                        EntityResponse response = new EntityResponse();
                        log.info("Ticket Assignee with Email " + ticketAssignee.getEmail() + " AND NAME: " + ticketAssignee.getNames() + " MODIFIED SUCCESSFULLY AT " + ticketAssignee.getModifiedTime());
                        response.setMessage("Ticket Assignee with Email " + ticketAssignee.getEmail() + " AND NAME: " + ticketAssignee.getNames() + " MODIFIED SUCCESSFULLY AT " + ticketAssignee.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(ticketAssignee);
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
    public ResponseEntity<?> getAllTicketAssignees() {
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
                    List<TicketAssignee> ticketAssignees = ticketAssigneeRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (ticketAssignees.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(ticketAssignees);
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
    public ResponseEntity<?> getTicketAssigneeById(@PathVariable("id") Long id) {
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
                    TicketAssignee ticketAssignee = ticketAssigneeService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(ticketAssignee);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/find/by/ticketAssignee/{code}")
    public ResponseEntity<?> getTicketAssigneeByCode(@PathVariable("code") String ticketAssigneeCode) {
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
//                    Optional<TicketAssignee> searchCode = ticketAssigneeRepository.findByEntityIdAndTicketAssigneeCode(EntityRequestContext.getCurrentEntityId(), ticketAssigneeCode);
//                    if (searchCode.isPresent()) {
//                        EntityResponse response = new EntityResponse();
//                        Optional<TicketAssignee> ticketAssignee = ticketAssigneeRepository.findByTicketAssigneeCode(ticketAssigneeCode);
//                        response.setMessage("TICKET ASSIGNEE WITH CODE " + ticketAssigneeCode + " ALREADY REGISTERED");
//                        response.setStatusCode(HttpStatus.OK.value());
//                        response.setEntity(ticketAssignee);
//                        return new ResponseEntity<>(response, HttpStatus.OK);
//                    }
//                    else
                    {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET ASSIGNEE WITH CODE " + ticketAssigneeCode + " AVAILABLE FOR REGISTRATION");
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
    public ResponseEntity<?> verifyTicketAssignee(@PathVariable String id) {
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
                    Optional<TicketAssignee> theTicketAssignee = ticketAssigneeRepository.findById(Long.parseLong(id));
                    if (theTicketAssignee.isPresent()) {
                        TicketAssignee ticketAssignee = theTicketAssignee.get();

                        if (ticketAssignee.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (ticketAssignee.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (ticketAssignee.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " ALREADY VERIFIED");
                                response.setMessage("ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    ticketAssignee.setVerifiedFlag('Y');
                                    ticketAssignee.setVerifiedTime(new Date());
                                    ticketAssignee.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    ticketAssigneeRepository.save(ticketAssignee);
                                    EntityResponse response = new EntityResponse();
                                    log.info("ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " VERIFIED SUCCESSFULLY AT " + ticketAssignee.getVerifiedTime());
                                    response.setMessage("ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " VERIFIED SUCCESSFULLY AT " + ticketAssignee.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(ticketAssignee);
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
    public ResponseEntity<?> deleteTicketAssignee(@PathVariable Long id) {
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
                    Optional<TicketAssignee> theAssignee = ticketAssigneeRepository.findById(id);
                    if (theAssignee.isPresent()) {
                        TicketAssignee ticketAssignee = theAssignee.get();
                        ticketAssignee.setDeletedFlag('Y');
                        ticketAssignee.setDeletedTime(new Date());
                        ticketAssignee.setDeletedBy(UserRequestContext.getCurrentUser());
                        ticketAssigneeRepository.save(ticketAssignee);
                        EntityResponse response = new EntityResponse();
                        log.info("TICKET ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " DELETED SUCCESSFULLY AT " + ticketAssignee.getDeletedTime());
                        response.setMessage("TICKET ASSIGNEE WITH EMAIL " + ticketAssignee.getEmail() + " AND NAME " + ticketAssignee.getNames() + " DELETED SUCCESSFULLY AT " + ticketAssignee.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(ticketAssignee);
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
