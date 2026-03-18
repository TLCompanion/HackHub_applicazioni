package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RepositoryTeam extends JpaRepository<Team, String> {

    boolean existsByNome(String nomeTeam);

    Optional<Team> findByNome(String nomeTeam);

    Team getTeamByIdTeam(String idTeam);
}
