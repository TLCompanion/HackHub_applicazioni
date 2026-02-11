package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.IscrizioneTeam;
import com.example.hackhub.domain.implementazione.Sottomissione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RepositoryIscrizioniTeam extends JpaRepository<IscrizioneTeam, String> {

    //void salva(IscrizioneTeam iscrizione);
}
