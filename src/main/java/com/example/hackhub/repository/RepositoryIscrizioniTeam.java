package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.IscrizioneTeam;
import com.example.hackhub.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryIscrizioniTeam extends JpaRepository<IscrizioneTeam, String> {

    Optional<IscrizioneTeam> findByTeamAndHackathon(Team team, Hackathon hackathon);
}
