package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryUtenti extends JpaRepository<Utente, String> {
/*
    //entrambi avevano String idUtente
    Utente getByID(Utente utente);

    boolean esiste(Utente utente);*/
}
