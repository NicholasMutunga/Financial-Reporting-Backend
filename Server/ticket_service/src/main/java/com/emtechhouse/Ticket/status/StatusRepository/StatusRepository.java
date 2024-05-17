package com.emtechhouse.Ticket.status.StatusRepository;

import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Override
    Optional<Status> findById(Long aLong);

    Optional<Status> findByStatusCode(String StatusCode);

    Optional<Status> findByEntityIdAndStatusCodeAndDeletedFlag(String entityId, String statusCode, Character flag);

    List<Status> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

    Optional<Status>findByEntityIdAndStatusCode(String entityId, String statusCode);
}
