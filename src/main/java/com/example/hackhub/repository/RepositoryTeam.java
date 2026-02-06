package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTeam extends JpaRepository<Team, String> {

    boolean esisteNomeTeam(String nomeTeam);
/*
    void salva(Team team);

    //String idTeam
    Team getTeamByIdTeam(Team team);*/
}
