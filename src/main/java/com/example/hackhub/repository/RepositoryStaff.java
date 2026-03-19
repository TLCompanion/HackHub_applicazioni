package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryStaff extends JpaRepository<Staff, String> {

    //TODO aggiornare questo metodo in uml

    /**
     * Trova un membro dello staff dal suo nome
     * @param nomeUtente il nome dell'utente
     * @return il membro dello staff se esiste, altrimenti un optional vuoto
     */
    Optional<Staff> findByUtente_NomeUtente(String nomeUtente);

    /**
     * Trova un membro dello staff dal suo id
     * @param id l'id del membro dello staff
     * @return il membro dello staff se esiste, altrimenti un optional vuoto
     */
    Optional<Staff> getStaffByIdStaff(String id);
}
