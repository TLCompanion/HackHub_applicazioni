package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    Team getTeamByUtente(String idUtente);

    MembroTeam getMembroTeam(String idUtente);

    boolean esisteTeam(String idUtente);

    void salva(MembroTeam membro);
}
