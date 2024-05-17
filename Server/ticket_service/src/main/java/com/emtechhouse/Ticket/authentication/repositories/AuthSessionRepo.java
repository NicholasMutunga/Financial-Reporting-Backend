package com.emtechhouse.Ticket.authentication.repositories;

import com.emtechhouse.Ticket.authentication.models.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSessionRepo extends JpaRepository<AuthSession, Long> {
}
