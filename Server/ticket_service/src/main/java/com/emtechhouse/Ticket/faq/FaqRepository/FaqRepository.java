package com.emtechhouse.Ticket.faq.FaqRepository;

import com.emtechhouse.Ticket.faq.FaqModel.Faq;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

    @Override
    Optional<Faq> findById(Long aLong);

    Optional<Faq> findByFaqCode(String FaqCode);

    Optional<Faq> findByEntityIdAndFaqCodeAndDeletedFlag(String entityId, String faqCode, Character flag);

    List<Faq> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

    Optional<Faq>findByEntityIdAndFaqCode(String entityId, String faqCode);
}
