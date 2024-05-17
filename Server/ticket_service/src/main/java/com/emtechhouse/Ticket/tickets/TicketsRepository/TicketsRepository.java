package com.emtechhouse.Ticket.tickets.TicketsRepository;

import com.emtechhouse.Ticket.Assign.assignTicketModel.AssignTicketModel;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import com.emtechhouse.Ticket.tickets.TicketsModel.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketsRepository  extends JpaRepository<Tickets, Long> {
    Optional<Tickets> findByEntityIdAndTicketsCodeAndDeletedFlag(String entityId, String ticketsCode, Character flag);


    @Query(value = "SELECT *  FROM tickets;", nativeQuery = true)
    List<Tickets> findAllByEntityIdOrderByIdDesc();

    Optional<Tickets>findByEntityIdAndTicketsCode(String entityId, String ticketsCode);

    Optional<Tickets> findByTicketsCode(String TicketsCode);

    List<Tickets> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);


    @Query(value = "SELECT *  FROM tickets;", nativeQuery = true)
    List<Tickets> findAllOrderByIdDesc();

    @Query(value = "select * from tickets where tickets_code = :ticketsCode and deleted_flag = :deletedFlag", nativeQuery = true)
    String findByTicketData2(String ticketsCode, Character deletedFlag);

    @Query(value = "select * from tickets where tickets_code = :ticketsCode and deleted_flag = :deletedFlag", nativeQuery = true)
    String findByTicketData1(String ticketsCode, Character deletedFlag);
}
