package com.example.hackhub.controller;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniChiuse;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class IscriviTeamHandlerTest {

    private RepositoryMembriTeam repoMembriTeam;
    private RepositoryHackathon repoHackathon;
    private RepositoryIscrizioniTeam repoIscrizioniTeam;
    private IscriviTeamHandler handler;

    @BeforeEach
    void setUp() {
        repoMembriTeam = mock(RepositoryMembriTeam.class);
        repoHackathon = mock(RepositoryHackathon.class);
        repoIscrizioniTeam = mock(RepositoryIscrizioniTeam.class);

        handler = new IscriviTeamHandler(repoMembriTeam, repoHackathon, repoIscrizioniTeam);
    }

    private void assegnaId(Object obj, String metodo) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(metodo);
        m.setAccessible(true);
        m.invoke(obj);
    }

    private Hackathon creaHackathonDiTest() {
        return new Hackathon(
                "HackFest",
                new Periodo(
                        LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                        LocalDate.now().plusDays(2), LocalTime.of(18, 0)
                ),
                BigDecimal.valueOf(1000),
                "Milano",
                6,
                3,
                LocalDateTime.now().plusDays(1),
                "Regolamento",
                5
        );
    }

    @Test
    void avviaIscrizioneHackathon() throws Exception {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        Hackathon hackathon = creaHackathonDiTest();

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(leader);
        team.setLeader(leader);
        MembroTeam membro1 = new MembroTeam(new Utente("Luigi"), team, RuoloTeam.MEMBRO);
        MembroTeam membro2 = new MembroTeam(new Utente("Anna"), team, RuoloTeam.MEMBRO);

        team.aggiungiMembro(membro1);
        team.aggiungiMembro(membro2);

        when(repoMembriTeam.findByUtente_IdUtente(utente.getIdUtente())).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.of(hackathon));
        when(repoIscrizioniTeam.findByTeamAndHackathon(team, hackathon)).thenReturn(Optional.empty());

        handler.avviaIscrizioneHackathon(utente.getIdUtente(), "HackFest");

        verify(repoHackathon).save(hackathon);
        verify(repoIscrizioniTeam).save(any(IscrizioneTeam.class));
        assertTrue(hackathon.getIscrizioni().size() == 1);
    }

    @Test
    void avviaIscrizioneHackathon_utenteNonMembro() {
        when(repoMembriTeam.findByUtente_IdUtente("U1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaIscrizioneHackathon("U1", "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_utenteNonLeader() {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        when(repoMembriTeam.findByUtente_IdUtente("U1")).thenReturn(Optional.of(membro));

        assertThrows(ForbiddenException.class,
                () -> handler.avviaIscrizioneHackathon("U1", "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_hackathonNonTrovato() {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);

        when(repoMembriTeam.findByUtente_IdUtente("U1")).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaIscrizioneHackathon("U1", "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_numeroMembriNonCompatibile() throws Exception {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        Hackathon hackathon = creaHackathonDiTest();

        assegnaId(utente, "assegnaId");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);

        when(repoMembriTeam.findByUtente_IdUtente(utente.getIdUtente())).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.of(hackathon));

        assertThrows(ConflictException.class,
                () -> handler.avviaIscrizioneHackathon(utente.getIdUtente(), "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_teamGiaIscritto() throws Exception {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        Hackathon hackathon = creaHackathonDiTest();

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);
        team.aggiungiMembro(new MembroTeam(new Utente("Luigi"), team, RuoloTeam.MEMBRO));
        team.aggiungiMembro(new MembroTeam(new Utente("Anna"), team, RuoloTeam.MEMBRO));

        IscrizioneTeam iscrizioneEsistente = new IscrizioneTeam(team, hackathon);

        when(repoMembriTeam.findByUtente_IdUtente(utente.getIdUtente())).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.of(hackathon));
        when(repoIscrizioniTeam.findByTeamAndHackathon(team, hackathon)).thenReturn(Optional.of(iscrizioneEsistente));

        assertThrows(ConflictException.class,
                () -> handler.avviaIscrizioneHackathon(utente.getIdUtente(), "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_maxIscrizioniRaggiunto() throws Exception {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        Hackathon hackathon = creaHackathonDiTest();

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);
        team.aggiungiMembro(new MembroTeam(new Utente("Luigi"), team, RuoloTeam.MEMBRO));
        team.aggiungiMembro(new MembroTeam(new Utente("Anna"), team, RuoloTeam.MEMBRO));

        for (int i = 0; i < hackathon.getMaxIscrizioni(); i++) {
            hackathon.aggiungiIscrizione(new IscrizioneTeam(new Team("Team" + i), hackathon));
        }

        when(repoMembriTeam.findByUtente_IdUtente(utente.getIdUtente())).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.of(hackathon));
        when(repoIscrizioniTeam.findByTeamAndHackathon(team, hackathon)).thenReturn(Optional.empty());

        assertThrows(ConflictException.class,
                () -> handler.avviaIscrizioneHackathon(utente.getIdUtente(), "HackFest"));
    }

    @Test
    void avviaIscrizioneHackathon_chiudeIscrizioniAlRaggiungimentoDelMassimo() throws Exception {
        Utente utente = new Utente("Mario");
        Team team = new Team("TeamAlpha");
        Hackathon hackathon = creaHackathonDiTest();

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(leader);
        team.setLeader(leader);
        team.aggiungiMembro(new MembroTeam(new Utente("Luigi"), team, RuoloTeam.MEMBRO));
        team.aggiungiMembro(new MembroTeam(new Utente("Anna"), team, RuoloTeam.MEMBRO));

        for (int i = 0; i < hackathon.getMaxIscrizioni() - 1; i++) {
            hackathon.aggiungiIscrizione(new IscrizioneTeam(new Team("Team" + i), hackathon));
        }

        when(repoMembriTeam.findByUtente_IdUtente(utente.getIdUtente())).thenReturn(Optional.of(leader));
        when(repoHackathon.findByNome("HackFest")).thenReturn(Optional.of(hackathon));
        when(repoIscrizioniTeam.findByTeamAndHackathon(team, hackathon)).thenReturn(Optional.empty());

        handler.avviaIscrizioneHackathon(utente.getIdUtente(), "HackFest");

        assertTrue(hackathon.getStato() instanceof IscrizioniChiuse);
    }
}

