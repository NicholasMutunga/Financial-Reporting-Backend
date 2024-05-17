package com.emtechhouse.Ticket.authentication.basicActions;

import com.emtechhouse.Ticket.authentication.basicActions.Basicactions;
import com.emtechhouse.Ticket.authentication.models.Privilege;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicactionsRepo extends JpaRepository<Basicactions, Long> {
    List<Basicactions> findByPrivilegeAndWorkclassAndCode(Privilege privilege, Workclass workclass, String code);
    List<Basicactions> findByPrivilegeAndWorkclassAndDeletedFlag(Privilege privilege, Workclass workclass, Character deletedFlag);
    List<Basicactions> findByDeletedFlag(Character deletedFlag);
}
