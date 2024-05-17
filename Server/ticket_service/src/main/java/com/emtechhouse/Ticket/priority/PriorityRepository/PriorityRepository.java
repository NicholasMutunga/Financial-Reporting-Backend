package com.emtechhouse.Ticket.priority.PriorityRepository;

import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    @Override
    Optional<Priority> findById(Long aLong);

    Optional<Priority> findByPriorityCode(String PriorityCode);

    Optional<Priority> findByEntityIdAndPriorityCodeAndDeletedFlag(String entityId, String priorityCode, Character flag);

    List<Priority> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

    Optional<Priority>findByEntityIdAndPriorityCode(String entityId, String priorityCode);
}
