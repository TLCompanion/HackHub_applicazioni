package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Richiesta;
import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryRichiesta  extends JpaRepository<Richiesta, String> {

    List<Richiesta> findAllByDestinatario(Utente destinatario);
}
