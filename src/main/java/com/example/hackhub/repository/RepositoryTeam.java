package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RepositoryTeam extends JpaRepository<Team, String> {

    /**
     * Cotrolla se esiste un team con il nome specificato
     * @param nomeTeam il nome del team
     * @return vero se il team esiste, falso altrimenti
     */
    boolean existsByNome(String nomeTeam);

    /**
     * Ritorna il team con il nome specificato
     * @param nomeTeam il nome del team
     * @return il team se esiste, altrimenti un Optional vuoto
     */
    //todo secondo me questi due fanno praticamente la stessa cosa e possiamo usare solo uno dei due
    Optional<Team> findByNomeTeam(String nomeTeam);

    Team getTeamByIdTeam(String idTeam);
}
