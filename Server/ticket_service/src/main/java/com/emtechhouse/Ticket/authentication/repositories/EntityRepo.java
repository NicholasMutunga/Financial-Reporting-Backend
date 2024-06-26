package com.emtechhouse.Ticket.authentication.repositories;

import com.emtechhouse.Ticket.authentication.models.Entitygroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityRepo extends JpaRepository<Entitygroup, Long> {
    Optional<Entitygroup> findByEntityCodeAndDeletedFlag(String entityCode, Character deletedFlag);
    List<Entitygroup> findByDeletedFlag(Character deletedFlag);
}
