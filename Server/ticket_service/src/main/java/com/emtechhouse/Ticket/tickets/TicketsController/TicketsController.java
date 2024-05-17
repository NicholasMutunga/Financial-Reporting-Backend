package com.emtechhouse.Ticket.tickets.TicketsController;


import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import com.emtechhouse.Ticket.status.StatusRepository.StatusRepository;
import com.emtechhouse.Ticket.status.StatusService.StatusService;
import com.emtechhouse.Ticket.tickets.TicketsModel.Tickets;
import com.emtechhouse.Ticket.tickets.TicketsRepository.TicketsRepository;
import com.emtechhouse.Ticket.tickets.TicketsService.TicketsService;
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
@RequestMapping("system/ticketing/tickets")
public class TicketsController {
    @Autowired
    private TicketsRepository ticketsRepository;

    @Autowired
    private TicketsService ticketsService;

    @PostMapping("/add")
    public ResponseEntity<?> addTickets(@RequestBody Tickets tickets) {
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
                    Optional<Tickets> checkTickets = ticketsRepository.findByEntityIdAndTicketsCodeAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), tickets.getTicketsCode(), 'N');
//                    if (checkTickets.isPresent()) {
//                        EntityResponse response = new EntityResponse();
//                        response.setMessage("TICKET WITH CODE " + checkTickets.get().getTicketsCode() + " AND NAME " + checkTickets.get().getTitle() + " ALREADY CREATED ON " + checkTickets.get().getPostedTime());
//                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
//                        response.setEntity("");
//                        return new ResponseEntity<>(response, HttpStatus.OK);
//                    }
//                    else
                    {
                        tickets.setPostedBy(UserRequestContext.getCurrentUser());
                        tickets.setEntityId(EntityRequestContext.getCurrentEntityId());
                        tickets.setPostedFlag('Y');
                        tickets.setModifiedBy("System");
                        tickets.setPostedTime(new Date());
                        Tickets newTickets = ticketsService.addTickets(tickets);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " CREATED SUCCESSFULLY AT " + tickets.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newTickets);
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
    public ResponseEntity<?> updateTickets(@RequestBody Tickets tickets) {
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
                    tickets.setModifiedBy(UserRequestContext.getCurrentUser());
                    tickets.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Tickets> theTickets = ticketsRepository.findById(tickets.getId());
                    if (theTickets.isPresent()) {
                        tickets.setPostedTime(theTickets.get().getPostedTime());
                        tickets.setPostedFlag(theTickets.get().getPostedFlag());
                        tickets.setPostedBy(theTickets.get().getPostedBy());
                        tickets.setModifiedFlag('Y');
                        tickets.setVerifiedFlag('N');
                        tickets.setModifiedTime(new Date());
                        tickets.setModifiedBy(tickets.getModifiedBy());
                        ticketsService.updateTickets(tickets);
                        EntityResponse response = new EntityResponse();
                        log.info("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME: " + tickets.getTitle() + " MODIFIED SUCCESSFULLY AT " + tickets.getModifiedTime());
                        response.setMessage("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " MODIFIED SUCCESSFULLY AT " + tickets.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(tickets);
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
                    List<Tickets> tickets = ticketsRepository.findAllByEntityIdAndDeletedFlagOrderByIdDesc(EntityRequestContext.getCurrentEntityId(), 'N');
                    if (tickets.size() > 0) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.FOUND.value());
                        response.setEntity(tickets);
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
    public ResponseEntity<?> getTicketById(@PathVariable("id") Long id) {
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
                    Tickets tickets = ticketsService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(tickets);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    @GetMapping("/find/by/ticket/{code}")
    public ResponseEntity<?> getTicketByCode(@PathVariable("code") String ticketsCode) {
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
                    Optional<Tickets> searchCode = ticketsRepository.findByEntityIdAndTicketsCode(EntityRequestContext.getCurrentEntityId(), ticketsCode);
                    if (searchCode.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        Optional<Tickets> tickets = ticketsRepository.findByTicketsCode(ticketsCode);
                        response.setMessage("TICKET WITH CODE " + ticketsCode + " ALREADY REGISTERED");
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(tickets);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET WITH CODE " + ticketsCode + " AVAILABLE FOR REGISTRATION");
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
                    Optional<Tickets> theTickets = ticketsRepository.findById(Long.parseLong(id));
                    if (theTickets.isPresent()) {
                        Tickets tickets = theTickets.get();
                        if (tickets.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else
                        if (tickets.getModifiedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())) {
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you Modified");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else
                        {
                            if (tickets.getVerifiedFlag().equals('Y')) {
                                EntityResponse response = new EntityResponse();
                                log.info("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " ALREADY VERIFIED");
                                response.setMessage("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " ALREADY VERIFIED");
                                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                                response.setEntity("");
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                {
                                    tickets.setVerifiedFlag('Y');
                                    tickets.setVerifiedTime(new Date());
                                    tickets.setVerifiedBy(UserRequestContext.getCurrentUser());
                                    tickets.setEntityId(EntityRequestContext.getCurrentEntityId());
                                    ticketsRepository.save(tickets);
                                    EntityResponse response = new EntityResponse();
                                    log.info("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME: " + tickets.getTitle() + " VERIFIED SUCCESSFULLY AT " + tickets.getVerifiedTime());
                                    response.setMessage("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " VERIFIED SUCCESSFULLY AT " + tickets.getVerifiedTime());
                                    response.setStatusCode(HttpStatus.OK.value());
                                    response.setEntity(tickets);
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
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
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
                    Optional<Tickets> theTickets = ticketsRepository.findById(id);
                    if (theTickets.isPresent()) {
                        Tickets tickets = theTickets.get();
                        tickets.setDeletedFlag('Y');
                        tickets.setDeletedTime(new Date());
                        tickets.setDeletedBy(UserRequestContext.getCurrentUser());
                        tickets.setEntityId(EntityRequestContext.getCurrentEntityId());
                        ticketsRepository.save(tickets);
                        EntityResponse response = new EntityResponse();
                        log.info("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " DELETED SUCCESSFULLY AT " + tickets.getDeletedTime());
                        response.setMessage("TICKET WITH CODE " + tickets.getTicketsCode() + " AND NAME " + tickets.getTitle() + " DELETED SUCCESSFULLY AT " + tickets.getDeletedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(tickets);
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
