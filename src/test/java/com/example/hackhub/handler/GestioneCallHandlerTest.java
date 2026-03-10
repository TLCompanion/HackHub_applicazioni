package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.PropostaCallRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoRichiesta;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.Concluso;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestioneCallHandlerTest {

    private RepositoryMembriTeam repoMembriTeam;
    private RepositoryHackathon repoHackathon;
    private ServizioNotifiche servizioNotifiche;
    private GestioneCallHandler handler;

    @BeforeEach
    void setUp() {
        repoMembriTeam = mock(RepositoryMembriTeam.class);
        repoHackathon = mock(RepositoryHackathon.class);
        servizioNotifiche = mock(ServizioNotifiche.class);

        handler = new GestioneCallHandler(repoMembriTeam, repoHackathon, servizioNotifiche);
    }

    private void assegnaId(Object obj, String metodo) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(metodo);
        m.setAccessible(true);
        m.invoke(obj);
    }

    private Hackathon hackathonValido() {
        return new Hackathon(
                "HackFest",
                new Periodo(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(9, 0),
                        LocalDate.now().plusDays(2),
                        LocalTime.of(18, 0)
                ),
                java.math.BigDecimal.valueOf(1000),
                "Milano",
                6,
                3,
                LocalDateTime.now().plusHours(12),
                "Regolamento",
                5
        );
    }

    @Test
    void avviaPropostaCall() throws Exception {
        Hackathon hackathon = hackathonValido();

        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");
        Utente leader = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamAlpha");

        assegnaId(mentore, "assegnaId");
        assegnaId(leader, "assegnaId");
        assegnaId(team, "assegnaId");

        Staff staff = new Staff(mentore, hackathon, RuoloStaff.MENTORE);
        hackathon.aggiungiStaff(staff);

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizione(iscrizione);

        MembroTeam membroLeader = new MembroTeam(leader, team, RuoloTeam.LEADER);

        PropostaCallRequest request = new PropostaCallRequest(
                hackathon.getIdHackathon(),
                team.getIdTeam(),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0)
        );

        when(repoHackathon.findById(hackathon.getIdHackathon())).thenReturn(Optional.of(hackathon));
        when(repoMembriTeam.findMembroTeamByRuolo(RuoloTeam.LEADER)).thenReturn(Optional.of(membroLeader));

        handler.avviaPropostaCall(mentore.getIdUtente(), request);

        verify(servizioNotifiche).creaRichiesta(
                eq(mentore.getIdUtente()),
                eq(List.of(leader)),
                eq(TipoRichiesta.PROPOSTA_CALL),
                eq("Proposta di call"),
                any(Periodo.class)
        );
    }

    @Test
    void avviaPropostaCall_hackathonNonTrovato() {
        PropostaCallRequest request = new PropostaCallRequest(
                "H-1",
                "T-1",
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0)
        );

        when(repoHackathon.findById("H-1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaPropostaCall("U-1", request));
    }

    @Test
    void avviaPropostaCall_leaderNonTrovato() throws Exception {
        Hackathon hackathon = hackathonValido();

        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");
        Team team = new Team("TeamAlpha");

        assegnaId(mentore, "assegnaId");
        assegnaId(team, "assegnaId");

        Staff staff = new Staff(mentore, hackathon, RuoloStaff.MENTORE);
        hackathon.aggiungiStaff(staff);

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizione(iscrizione);

        PropostaCallRequest request = new PropostaCallRequest(
                hackathon.getIdHackathon(),
                team.getIdTeam(),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0)
        );

        when(repoHackathon.findById(hackathon.getIdHackathon())).thenReturn(Optional.of(hackathon));
        when(repoMembriTeam.findMembroTeamByRuolo(RuoloTeam.LEADER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaPropostaCall(mentore.getIdUtente(), request));
    }

    @Test
    void verificaMentoreAutorizzato() throws Exception {
        Hackathon hackathon = hackathonValido();
        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");

        assegnaId(mentore, "assegnaId");

        Staff staff = new Staff(mentore, hackathon, RuoloStaff.MENTORE);
        hackathon.aggiungiStaff(staff);

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("verificaMentoreAutorizzato", Hackathon.class, String.class);
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(handler, hackathon, mentore.getIdUtente()));
    }

    @Test
    void verificaMentoreAutorizzato_utenteNonAutorizzato() throws Exception {
        Hackathon hackathon = hackathonValido();

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("verificaMentoreAutorizzato", Hackathon.class, String.class);
        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, hackathon, "U-1"));

        assertTrue(ex.getCause() instanceof ForbiddenException);
        assertEquals("Utente non autorizzato a inviare call", ex.getCause().getMessage());
    }

    @Test
    void validazione() throws Exception {
        Hackathon hackathon = hackathonValido();
        Team team = new Team("TeamAlpha");

        assegnaId(team, "assegnaId");

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizione(iscrizione);

        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30)
        );

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("validazione", Periodo.class, Hackathon.class, String.class);
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(handler, periodo, hackathon, team.getIdTeam()));
    }

    @Test
    void validazione_hackathonConcluso() throws Exception {
        Hackathon hackathon = hackathonValido();
        hackathon.setStato(Concluso.INSTANCE);

        Team team = new Team("TeamAlpha");
        assegnaId(team, "assegnaId");

        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30)
        );

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("validazione", Periodo.class, Hackathon.class, String.class);
        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, periodo, hackathon, team.getIdTeam()));

        assertTrue(ex.getCause() instanceof ConflictException);
        assertEquals("Hackathon concluso, non è possibile proporre una call", ex.getCause().getMessage());
    }

    @Test
    void validazione_teamNonIscritto() throws Exception {
        Hackathon hackathon = hackathonValido();
        Team team = new Team("TeamAlpha");

        assegnaId(team, "assegnaId");

        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30)
        );

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("validazione", Periodo.class, Hackathon.class, String.class);
        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, periodo, hackathon, team.getIdTeam()));

        assertTrue(ex.getCause() instanceof ConflictException);
        assertEquals("Il team non è iscritto all'hackathon", ex.getCause().getMessage());
    }

    @Test
    void validazione_callDopoFineHackathon() throws Exception {
        Hackathon hackathon = hackathonValido();
        Team team = new Team("TeamAlpha");

        assegnaId(team, "assegnaId");

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        hackathon.aggiungiIscrizione(iscrizione);

        Periodo periodo = new Periodo(
                hackathon.getPeriodo().getDataFine().plusDays(1),
                LocalTime.of(10, 0),
                hackathon.getPeriodo().getDataFine().plusDays(1),
                LocalTime.of(10, 30)
        );

        Method m = GestioneCallHandler.class
                .getDeclaredMethod("validazione", Periodo.class, Hackathon.class, String.class);
        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, periodo, hackathon, team.getIdTeam()));

        assertTrue(ex.getCause() instanceof ConflictException);
        assertEquals("La call non può essere dopo la fine dell'hackathon", ex.getCause().getMessage());
    }
}
