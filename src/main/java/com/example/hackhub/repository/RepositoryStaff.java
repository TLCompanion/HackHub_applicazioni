package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryStaff extends JpaRepository<Staff, String> {

    //TODO aggiornare questo metodo in uml
    Optional<Staff> findByUtente_NomeUtente(String nomeUtente);
}
