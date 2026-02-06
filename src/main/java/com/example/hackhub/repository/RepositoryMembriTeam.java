package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    /**
     * Restituisce falso se il team non esiste, vero se è già presente
     * @param idUtente l'identificativo dell'utente che vuole creare il team
     * @return falso se il team non esiste, vero se è già presente
     */
    @Query
    boolean esisteTeam(String idUtente);

    /*Team getTeamByIdUtente(String idUtente);

    MembroTeam getMembroTeam(String idUtente);

    void salva(MembroTeam membro);*/
}
