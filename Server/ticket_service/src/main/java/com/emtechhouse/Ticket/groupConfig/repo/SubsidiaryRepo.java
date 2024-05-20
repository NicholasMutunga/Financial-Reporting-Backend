package com.emtechhouse.Ticket.groupConfig.repo;

import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubsidiaryRepo extends JpaRepository <Subsidiary , Long> {



   // Optional<Subsidiary> findByEntityIdAndSubsidiaryCode(String currentEntityId, String subsidiaryCode);
    Optional<Subsidiary> findByCompanyName(String companyName);

    List<Subsidiary> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String currentEntityId, char n);

    //Optional<Subsidiary> findBySubsidiaryByCode(String subsidiaryCode);

    Optional<Subsidiary> findBySubsidiaryCode(String subsidiaryCode);
}
