package com.emtechhouse.Ticket.imp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ImpRepo extends JpaRepository<Imp,Long> {
}
