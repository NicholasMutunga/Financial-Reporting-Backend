package com.emtechhouse.Ticket.groupConfig.repo;

import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubsidiaryRepo extends JpaRepository <Subsidiary , Long> {


    Optional<Subsidiary> findByCompanyName(String companyName);
}
