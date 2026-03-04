package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Richiesta;
import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRichiesta  extends JpaRepository<Richiesta, String> {
}
