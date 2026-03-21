package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUtenti extends JpaRepository<Utente, String> {

    /**
     * Trova un utente dal suo nome
     *
     * @param nomeUtente il nome dell'utente
     * @return l'utente se presente
     */
    Optional<Utente> findByNomeUtente(String nomeUtente);

    Optional<Utente> findByIdUtente(String idUtente);
}
