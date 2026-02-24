package com.example.hackhub.repository;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    boolean existsByUtente(Utente utente);

    //Serve optional per gestire i null
    Optional<MembroTeam> findMembroTeamByRuolo(RuoloTeam ruolo);

    Optional<MembroTeam> findByUtente_IdUtente(String idUtente);

}
