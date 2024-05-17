package com.emtechhouse.Ticket.authentication.repositories;

import com.emtechhouse.Ticket.authentication.models.Privilege;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    List<Privilege> findByWorkclassAndSelected(Workclass workclass, Boolean selected);
}
