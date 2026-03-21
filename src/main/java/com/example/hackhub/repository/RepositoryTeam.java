package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RepositoryTeam extends JpaRepository<Team, String> {

    /**
     * Cotrolla se esiste un team con il nome specificato
     *
     * @param nomeTeam il nome del team
     * @return vero se il team esiste, falso altrimenti
     */
    boolean existsByNome(String nomeTeam);

    /**
     * Ritorna il team con il nome specificato
     *
     * @param nomeTeam il nome del team
     * @return il team se esiste, altrimenti un Optional vuoto
     */
    //TODO nei sequence cambiare i findByNomeTeam con questo
    Optional<Team> findByNome(String nomeTeam);

    /**
     * Recupera un Team insieme alla sua collezione di membri usando JOIN FETCH per evitare problemi di lazy loading
     * quando l'entità viene consultata fuori dal contesto transazionale (es. nei test).
     *
     * @param idTeam l'id del team
     * @return il team con i membri inizializzati
     */
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.membri WHERE t.idTeam = :idTeam")
    Optional<Team> findByIdFetchMembri(@Param("idTeam") String idTeam);
}
