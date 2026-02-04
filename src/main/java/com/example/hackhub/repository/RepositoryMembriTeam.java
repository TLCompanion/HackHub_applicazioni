package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    boolean esisteTeam(String idUtente);
    /*
    Team getTeamByIdUtente(String idUtente);

    MembroTeam getMembroTeam(String idUtente);

    void salva(MembroTeam membro);*/
}
