package com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeRepository;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketAssigneeRepository extends JpaRepository<TicketAssignee, Long> {

    Optional<TicketAssignee> findByNationalId(String nationlId);

    Optional<TicketAssignee> findByEmail(String email);

    List<TicketAssignee> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

//    Optional<TicketAssignee>findByEntityIdAndTicketAssigneeCode(String entityId, String ticketAssigneeCode);

//    Optional<TicketAssignee> findByTicketAssigneeCode(String ticketAssigneeCode);
}
