package com.example.hackhub.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.hackhub.controller.CreaTeamHandler;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;


class CreaTeamHandlerTest {

    private RepositoryTeam repoTeam;
    private RepositoryMembriTeam repoMembriTeam;
    private RepositoryUtenti repoUtenti;
    private CreaTeamHandler handler;

    @BeforeEach
    void setUp() {
        repoTeam = mock(RepositoryTeam.class);
        repoMembriTeam = mock(RepositoryMembriTeam.class);
        repoUtenti = mock(RepositoryUtenti.class);

        handler = new CreaTeamHandler(repoTeam, repoMembriTeam, repoUtenti);
    }

    @Test
    void avviaCreazioneTeam() {
        Utente utente = new Utente("Mario");

        when(repoUtenti.findById("U1")).thenReturn(Optional.of(utente));
        when(repoMembriTeam.existsByUtente(utente)).thenReturn(false);
        when(repoTeam.existsByNome("TeamAlpha")).thenReturn(false);

        handler.avviaCreazioneTeam("U1", "TeamAlpha");

        verify(repoTeam).save(any(Team.class));
        verify(repoMembriTeam).save(any(MembroTeam.class));
    }

    @Test
    void avviaCreazioneTeam_utenteNonTrovato() {
        when(repoUtenti.findById("U1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaCreazioneTeam("U1", "TeamAlpha"));
    }

    @Test
    void avviaCreazioneTeam_utenteGiaMembroDiUnTeam() {
        Utente utente = new Utente("Mario");

        when(repoUtenti.findById("U1")).thenReturn(Optional.of(utente));
        when(repoMembriTeam.existsByUtente(utente)).thenReturn(true);

        assertThrows(ForbiddenException.class,
                () -> handler.avviaCreazioneTeam("U1", "TeamAlpha"));
    }

    @Test
    void avviaCreazioneTeam_nomeTeamGiaEsistente() {
        Utente utente = new Utente("Mario");

        when(repoUtenti.findById("U1")).thenReturn(Optional.of(utente));
        when(repoMembriTeam.existsByUtente(utente)).thenReturn(false);
        when(repoTeam.existsByNome("TeamAlpha")).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> handler.avviaCreazioneTeam("U1", "TeamAlpha"));
    }
}
