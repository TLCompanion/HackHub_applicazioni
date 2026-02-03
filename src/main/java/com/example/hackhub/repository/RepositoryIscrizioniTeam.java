package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.IscrizioneTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryIscrizioniTeam extends JpaRepository<IscrizioneTeam, String> {

    //void salva(IscrizioneTeam iscrizione);

}
