package com.emtechhouse.Ticket.Assign.assignTicketRepository;


import com.emtechhouse.Ticket.Assign.assignTicketModel.AssignTicketModel;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.tickets.TicketsModel.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface assignTicketRepository extends JpaRepository<AssignTicketModel, Long> {
    @Query(value = "select * from assign_ticket_model where tickets_code = :ticketsCode and deleted_flag = :deletedFlag", nativeQuery = true)
    Optional<AssignTicketModel> findByTicketData(String ticketsCode, Character deletedFlag);
//    Optional<AssignTicketModel> findByTicketsCodeAndDeletedFlag(String ticketsCode, Character deletedFlag);
}
