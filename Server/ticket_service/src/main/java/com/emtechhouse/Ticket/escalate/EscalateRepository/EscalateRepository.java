package com.emtechhouse.Ticket.escalate.EscalateRepository;

import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.escalate.EscalateModel.Escalate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EscalateRepository extends JpaRepository<Escalate, Long> {
    Optional<Escalate> findByEntityIdAndEscalateCodeAndDeletedFlag(String entityId, String escalateCode, Character flag);

    @Override
    Optional<Escalate> findById(Long aLong);

    Optional<Escalate> findByEscalateCode(String EscalateCode);


    List<Escalate> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

    Optional<Escalate>findByEntityIdAndEscalateCode(String entityId, String escalateCode);
}
