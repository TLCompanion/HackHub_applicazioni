package com.example.hackhub.repository;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    boolean existsByUtente(Utente utente);

    boolean existsByIdUtente(String idUtente);

    //Serve optional per gestire i null
    Optional<Utente> findUtenteByRuolo(RuoloTeam ruolo);

    /*Team getTeamByIdUtente(String idUtente);

    MembroTeam getMembroTeam(String idUtente);

    void salva(MembroTeam membro);*/
}
