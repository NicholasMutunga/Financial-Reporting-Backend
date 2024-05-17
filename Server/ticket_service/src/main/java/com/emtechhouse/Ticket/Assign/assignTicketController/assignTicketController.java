package com.emtechhouse.Ticket.Assign.assignTicketController;

import com.emtechhouse.Ticket.Assign.assignTicketModel.AssignTicketModel;
import com.emtechhouse.Ticket.Assign.assignTicketService.assignTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
@RequestMapping("assign-ticket/controller")
public class assignTicketController {
    @Autowired
    private assignTicketService assignTicketService;


    @PostMapping("/add")
    public ResponseEntity<?> assignTicket(@RequestBody AssignTicketModel ticketData) {
        try {

            return assignTicketService.assignTicket(ticketData);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTicket(@RequestBody AssignTicketModel assignTicketModel) {
        try {
            System.out.println("Arrival Now");
            return assignTicketService.updateTicket(assignTicketModel);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @PostMapping("/Delete")
    public ResponseEntity<?> deleteTicket(@RequestBody AssignTicketModel ticketData) {
        try {
            return assignTicketService.deleteTicket(ticketData);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
}
