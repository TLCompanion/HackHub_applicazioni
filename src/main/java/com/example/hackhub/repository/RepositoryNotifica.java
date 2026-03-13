package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Notifica;
import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositoryNotifica extends JpaRepository<Notifica, String> {

    List<Notifica> findAllByDestinatario(Utente destinatario);

    Optional<Notifica> findByIdNotifica(String idNotifica);
}
