package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Valutazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryValutazioni extends JpaRepository<Valutazione, String> {
}
