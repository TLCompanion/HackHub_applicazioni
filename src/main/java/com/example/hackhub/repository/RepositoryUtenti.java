package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUtenti extends JpaRepository<Utente, String> {
    Optional<Utente> findByNome(String nomeUtente);
/*
    //entrambi avevano String idUtente
    Utente getByID(Utente utente);

    boolean esiste(Utente utente);*/
}
