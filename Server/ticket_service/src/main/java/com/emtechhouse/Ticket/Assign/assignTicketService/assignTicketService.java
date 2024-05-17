package com.emtechhouse.Ticket.Assign.assignTicketService;

import com.emtechhouse.Ticket.Assign.assignTicketModel.AssignTicketModel;
import com.emtechhouse.Ticket.Assign.assignTicketRepository.assignTicketRepository;
import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.tickets.TicketsModel.Tickets;
import com.emtechhouse.Ticket.tickets.TicketsRepository.TicketsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class assignTicketService {
    @Autowired
    private assignTicketRepository assignTicketRepository;

    @Autowired
    private TicketsRepository ticketsRepository;
    public ResponseEntity<?> assignTicket(AssignTicketModel ticketData) {
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
                    System.out.println("Ticket Code: "+ticketData.getTicketsCode());
                    String checkTicketByCode = ticketsRepository.findByTicketData1(ticketData.getTicketsCode(), 'N');
                    System.out.println("checkTicketByCode: "+checkTicketByCode);
                    if (checkTicketByCode == null) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET WITH CODE " + ticketData.getTicketsCode() + " DOES NOT EXIST");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    } else {
                        ticketData.setTicketsCode(ticketData.getTicketsCode());
                        ticketData.setAssignee(ticketData.getAssignee());
                        ticketData.setStartDate(ticketData.getStartDate());
                        ticketData.setEndDate(ticketData.getEndDate());
                        ticketData.setEntityId(EntityRequestContext.getCurrentEntityId());
                        ticketData.setPostedBy(UserRequestContext.getCurrentUser());
                        ticketData.setPostedFlag('Y');
                        ticketData.setPostedTime(new Date());
                        AssignTicketModel finalAssigned = assignTicketRepository.save(ticketData);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("THE TICKET WITH CODE " + finalAssigned.getTicketsCode() +" HAS BEEN ASSIGNED TO " + finalAssigned.getAssignee());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(finalAssigned);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public ResponseEntity<?> updateTicket(AssignTicketModel assignTicketModel) {
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
                    System.out.println("After checking entities");
                    System.out.println("Ticket Code: "+assignTicketModel.getTicketsCode());
                    Optional<AssignTicketModel> checkAssignee = assignTicketRepository.findByTicketData(assignTicketModel.getTicketsCode(), 'N');
                    if (!checkAssignee.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET WITH CODE " + checkAssignee.get().getTicketsCode() + " DOES NOT EXIST");
                        log.info("TICKET WITH CODE " + checkAssignee.get().getTicketsCode() + " DOES NOT EXIST");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        assignTicketModel.setAssignee(assignTicketModel.getAssignee());
                        assignTicketModel.setTicketsCode(assignTicketModel.getTicketsCode());
                        assignTicketModel.setStartDate(assignTicketModel.getStartDate());
                        assignTicketModel.setEndDate(assignTicketModel.getEndDate());
                        assignTicketModel.setModifiedBy(UserRequestContext.getCurrentUser());
                        assignTicketModel.setEntityId(EntityRequestContext.getCurrentEntityId());
                        assignTicketModel.setModifiedTime(new Date());
                        assignTicketModel.setModifiedFlag('Y');
                        AssignTicketModel finalAssigned = assignTicketRepository.save(assignTicketModel);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("THE TICKET  " + finalAssigned.getTicketsCode() + " MODIFIED SUCCESSFULLY AT " + finalAssigned.getPostedTime() + "HAS BEEN ASSIGNED TO " + finalAssigned.getAssignee());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(finalAssigned);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public ResponseEntity<?> deleteTicket(AssignTicketModel ticketData) {
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
                        String checkAssignee = ticketsRepository.findByTicketData2(ticketData.getTicketsCode(), 'N');
                    if (!(checkAssignee == null)) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("TICKET WITH CODE " + ticketData.getTicketsCode() + " DOES NOT EXIST");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        ticketData.setDeletedBy(UserRequestContext.getCurrentUser());
                        ticketData.setEntityId(EntityRequestContext.getCurrentEntityId());
                        ticketData.setDeletedTime(new Date());
                        ticketData.setDeletedFlag('Y');
                        AssignTicketModel finalAssigned = assignTicketRepository.save(ticketData);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("THE TICKET " + finalAssigned.getTicketsCode() +  " HAS BEEN DELETED SUCCESSFULLY AT " + finalAssigned.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(finalAssigned);
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
