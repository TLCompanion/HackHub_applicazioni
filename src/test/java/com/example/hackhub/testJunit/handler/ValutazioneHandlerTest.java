package com.example.hackhub.testJunit.handler;

import com.example.hackhub.boundary.dto.ValutazioneRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.domain.implementazione.statePattern.ValutazioneInCorso;
import com.example.hackhub.eccezioni.ConflictException;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValutazioneHandlerTest {
/*
    private RepositorySottomissioni repoSottomissioni;
    private RepositoryHackathon repoHackathon;
    private RepositoryStaff repoStaff;
    private ServizioNotifiche servizioNotifiche;
    private RepositoryIscrizioniTeam repoIscrizioniTeam;
    private RepositoryValutazioni repoValutazioni;
    private ValutazioneHandler handler;

    @BeforeEach
    void setUp() {
        repoSottomissioni = mock(RepositorySottomissioni.class);
        repoHackathon = mock(RepositoryHackathon.class);
        repoStaff = mock(RepositoryStaff.class);
        servizioNotifiche = mock(ServizioNotifiche.class);
        repoIscrizioniTeam = mock(RepositoryIscrizioniTeam.class);
        repoValutazioni = mock(RepositoryValutazioni.class);

        handler = new ValutazioneHandler(
                repoSottomissioni,
                repoHackathon,
                repoStaff,
                servizioNotifiche,
                repoIscrizioniTeam,
                repoValutazioni
        );
    }

    private void assegnaId(Object obj, String metodo) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(metodo);
        m.setAccessible(true);
        m.invoke(obj);
    }

    private void impostaCampo(Object obj, String nomeCampo, Object valore) throws Exception {
        Field field = obj.getClass().getDeclaredField(nomeCampo);
        field.setAccessible(true);
        field.set(obj, valore);
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

    private ValutazioneRequest requestValida() {
        return new ValutazioneRequest("Ottimo progetto", 8);
    }

    @Test
    void avviaInserimentoValutazione() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();
        hackathon.setStato(ValutazioneInCorso.INSTANCE);

        Utente giudice = new Utente("Anna", "anna@gmail.com", "walevns08");
        assegnaId(giudice, "assegnaId");

        Staff staff = new Staff(giudice, hackathon, RuoloStaff.GIUDICE);
        hackathon.aggiungiStaff(staff);

        Sottomissione sottomissione = new Sottomissione("file.zip");
        Staff staffRepository = new Staff(giudice, hackathon, RuoloStaff.GIUDICE);

        when(repoSottomissioni.findById("S1")).thenReturn(Optional.of(sottomissione));
        when(repoStaff.findById(giudice.getIdUtente())).thenReturn(Optional.of(staffRepository));
        when(repoIscrizioniTeam.findAllByHackathon(hackathon)).thenReturn(List.of());

        handler.avviaInserimentoValutazione("S1", giudice.getIdUtente(), requestValida());

        assertNotNull(sottomissione.getValutazione());
        assertEquals(8, sottomissione.getValutazione().getVoto());
        assertEquals("Ottimo progetto", sottomissione.getValutazione().getDescrizione());

        verify(repoValutazioni).save(any(Valutazione.class));
        verify(repoSottomissioni).save(sottomissione);
    }

    @Test
    void avviaInserimentoValutazione_sottomissioneNonTrovata() {
        when(repoSottomissioni.findById("S1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaInserimentoValutazione("S1", "U1", requestValida()));
    }

    @Test
    void avviaInserimentoValutazione_giudiceNonTrovato() {
        Sottomissione sottomissione = new Sottomissione("file.zip");

        when(repoSottomissioni.findById("S1")).thenReturn(Optional.of(sottomissione));
        when(repoStaff.findById("U1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> handler.avviaInserimentoValutazione("S1", "U1", requestValida()));
    }

    @Test
    void avviaInserimentoValutazione_statoNonConsenteValutazione() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();
        hackathon.setStato(IscrizioniAperte.INSTANCE);

        Utente giudice = new Utente("Anna", "anna@gmail.com", "walevns08");
        assegnaId(giudice, "assegnaId");

        Sottomissione sottomissione = new Sottomissione("file.zip");
        Staff staffRepository = new Staff(giudice, hackathon, RuoloStaff.GIUDICE);

        when(repoSottomissioni.findById("S1")).thenReturn(Optional.of(sottomissione));
        when(repoStaff.findById(giudice.getIdUtente())).thenReturn(Optional.of(staffRepository));

        assertThrows(ConflictException.class,
                () -> handler.avviaInserimentoValutazione("S1", giudice.getIdUtente(), requestValida()));
    }

    @Test
    void verificaGiudiceAutorizzato() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();

        Utente giudice = new Utente("Anna", "anna@gmail.com", "walevns08");
        assegnaId(giudice, "assegnaId");

        Staff staff = new Staff(giudice, hackathon, RuoloStaff.GIUDICE);
        hackathon.aggiungiStaff(staff);

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("verificaGiudiceAutorizzato", Hackathon.class, String.class);
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(handler, hackathon, giudice.getIdUtente()));
    }

    @Test
    void verificaGiudiceAutorizzato_utenteNonAutorizzato() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("verificaGiudiceAutorizzato", Hackathon.class, String.class);
        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, hackathon, "U1"));

        assertTrue(ex.getCause() instanceof ForbiddenException);
        assertEquals("Utente non autorizzato a valutare questa sottomissione", ex.getCause().getMessage());
    }

    @Test
    void creaOAggiornaValutazione_creaNuovaValutazione() throws Exception {
        Sottomissione sottomissione = new Sottomissione("file.zip");

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("creaOAggiornaValutazione", Sottomissione.class, int.class, String.class);
        m.setAccessible(true);

        m.invoke(handler, sottomissione, 9, "Molto bello");

        assertNotNull(sottomissione.getValutazione());
        assertEquals(9, sottomissione.getValutazione().getVoto());
        assertEquals("Molto bello", sottomissione.getValutazione().getDescrizione());

        verify(repoValutazioni).save(any(Valutazione.class));
    }

    @Test
    void creaOAggiornaValutazione_aggiornaValutazioneEsistente() throws Exception {
        Sottomissione sottomissione = new Sottomissione("file.zip");
        Valutazione valutazione = new Valutazione(5, "Discreto");
        sottomissione.impostaValutazione(valutazione);

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("creaOAggiornaValutazione", Sottomissione.class, int.class, String.class);
        m.setAccessible(true);

        m.invoke(handler, sottomissione, 10, "Perfetto");

        assertEquals(10, sottomissione.getValutazione().getVoto());
        assertEquals("Perfetto", sottomissione.getValutazione().getDescrizione());

        verify(repoValutazioni).save(valutazione);
    }

    @Test
    void concludiHackathonSeTutteValutate() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();
        hackathon.setStato(ValutazioneInCorso.INSTANCE);

        Team team = new Team("TeamAlpha");
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(membro);

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        Sottomissione sottomissione = new Sottomissione("file.zip");
        sottomissione.impostaValutazione(new Valutazione(8, "Ottimo"));

        impostaCampo(iscrizione, "sottomissione", sottomissione);

        when(repoIscrizioniTeam.findAllByHackathon(hackathon)).thenReturn(List.of(iscrizione));

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("concludiHackathonSeTutteValutate", Hackathon.class);
        m.setAccessible(true);

        m.invoke(handler, hackathon);

        verify(repoHackathon).save(hackathon);
        verify(servizioNotifiche).creaNotifica(
                anyList(),
                eq(TipoNotifica.VALUTAZIONE_CONCLUSA),
                eq("L'hackathon è stato concluso, valutazioni terminate")
        );
    }

    @Test
    void concludiHackathonSeTutteValutate_nonConcludeSeNonTutteValutate() throws Exception {
        Hackathon hackathon = creaHackathonDiTest();
        hackathon.setStato(ValutazioneInCorso.INSTANCE);

        Team team = new Team("TeamAlpha");
        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);
        Sottomissione sottomissione = new Sottomissione("file.zip");

        impostaCampo(iscrizione, "sottomissione", sottomissione);

        when(repoIscrizioniTeam.findAllByHackathon(hackathon)).thenReturn(List.of(iscrizione));

        Method m = ValutazioneHandler.class
                .getDeclaredMethod("concludiHackathonSeTutteValutate", Hackathon.class);
        m.setAccessible(true);

        m.invoke(handler, hackathon);

        verify(repoHackathon, never()).save(hackathon);
        verify(servizioNotifiche, never()).creaNotifica(anyList(), any(), anyString());
    }*/
}
