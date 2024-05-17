package com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeService;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeRepository.TicketAssigneeRepository;
import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketAssigneeService {

    @Autowired
    TicketAssigneeRepository ticketAssigneeRepository;

    public TicketAssignee addTicketAssignee(TicketAssignee ticketAssignee) {
        try {
            return ticketAssigneeRepository.save(ticketAssignee);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public TicketAssignee updateTicketAssignee(TicketAssignee ticketAssignee) {
        try {
            return ticketAssigneeRepository.save(ticketAssignee);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public TicketAssignee findById(Long id){
        try{
            return ticketAssigneeRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
