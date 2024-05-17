package com.emtechhouse.Ticket.tickets.TicketsService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import com.emtechhouse.Ticket.status.StatusRepository.StatusRepository;
import com.emtechhouse.Ticket.tickets.TicketsModel.Tickets;
import com.emtechhouse.Ticket.tickets.TicketsRepository.TicketsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketsService {
    @Autowired
    TicketsRepository ticketsRepository;

    public Tickets addTickets(Tickets tickets) {
        try {
            return ticketsRepository.save(tickets);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Tickets updateTickets(Tickets tickets) {
        try {
            return ticketsRepository.save(tickets);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Tickets findById(Long id){
        try{
            return ticketsRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
