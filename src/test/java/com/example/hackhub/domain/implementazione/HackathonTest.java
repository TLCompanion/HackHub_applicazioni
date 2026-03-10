package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.statePattern.*;
import com.example.hackhub.eccezioni.ConflictException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HackathonTest {

    // Helper per creare un Hackathon valido
    private Hackathon creaHackathonDiTest() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        return new Hackathon(
                "HackFest", periodo, BigDecimal.valueOf(5000),
                "Milano", 6, 3,
                LocalDateTime.now().plusDays(1),
                "Regolamento ufficiale", 5
        );
    }

    @Test
    void assegnaId_generatoDaPrePersist() throws Exception {
        Hackathon h = creaHackathonDiTest();
        Method m = Hackathon.class.getDeclaredMethod("assegnaId");
        m.setAccessible(true);
        m.invoke(h);

        assertNotNull(h.getIdHackathon());
        assertTrue(h.getIdHackathon().startsWith("H-"));
    }

    @Test
    void getterPrincipali() {
        Hackathon h = creaHackathonDiTest();
        assertEquals("HackFest", h.getNome());
        assertNotNull(h.getPeriodo());
        assertEquals(BigDecimal.valueOf(5000), h.getPremio());
        assertEquals("Milano", h.getLuogo());
        assertEquals(3, h.getTeamMin());
        assertEquals(6, h.getTeamMax());
        assertEquals("Regolamento ufficiale", h.getRegolamento());
        assertEquals("Regolamento ufficiale", h.getInfo());
        assertEquals(5, h.getMaxIscrizioni());
        assertNotNull(h.getScadenzaIscrizioni());
        assertTrue(h.getStato() instanceof IscrizioniAperte);
        assertTrue(h.getStaff().isEmpty());
        assertTrue(h.getIscrizioni().isEmpty());
    }

    @Test
    void setStato_cambiaStati() {
        Hackathon h = creaHackathonDiTest();

        // Stato iniziale
        assertTrue(h.getStato() instanceof IscrizioniAperte);

        // Cambio stato
        h.setStato(Concluso.INSTANCE);
        assertTrue(h.getStato() instanceof Concluso);

        h.setStato(ValutazioneInCorso.INSTANCE);
        assertTrue(h.getStato() instanceof ValutazioneInCorso);

        h.setStato(InCorso.INSTANCE);
        assertTrue(h.getStato() instanceof InCorso);

        h.setStato(IscrizioniChiuse.INSTANCE);
        assertTrue(h.getStato() instanceof IscrizioniChiuse);
    }

    @Test
    void aggiungiStaff_eGetter() {
        Hackathon h = creaHackathonDiTest();
        Utente u = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Staff s = new Staff(u, h, RuoloStaff.MENTORE);

        h.aggiungiStaff(s);
        List<Staff> staffList = h.getStaff();
        assertTrue(staffList.contains(s));
        assertEquals(1, staffList.size());
    }

    @Test
    void aggiungiIscrizioneTeam_funzionaSeStatoApertoEMaxNonRaggiunto() {
        Hackathon h = creaHackathonDiTest();
        Team t = new Team("TeamAlpha");
        IscrizioneTeam i = new IscrizioneTeam(t, h);

        h.aggiungiIscrizione(i);
        List<IscrizioneTeam> iscrizioni = h.getIscrizioni();
        assertTrue(iscrizioni.contains(i));
        assertEquals(1, iscrizioni.size());
    }

    @Test
    void aggiungiIscrizioneTeam_lanciaEccezioneSeMaxRaggiunto() {
        Hackathon h = creaHackathonDiTest();
        for (int j = 0; j < h.getMaxIscrizioni(); j++) {
            h.aggiungiIscrizione(new IscrizioneTeam(new Team("Team" + j), h));
        }
        assertThrows(ConflictException.class, () ->
                h.aggiungiIscrizione(new IscrizioneTeam(new Team("TeamExtra"), h))
        );
    }

    @Test
    void aggiungiIscrizioneTeam_lanciaEccezioneSeStatoNonAperto() {
        Hackathon h = creaHackathonDiTest();
        h.setStato(IscrizioniChiuse.INSTANCE);
        Team t = new Team("TeamBeta");
        IscrizioneTeam i = new IscrizioneTeam(t, h);

        assertThrows(ConflictException.class, () -> h.aggiungiIscrizione(i));
    }

    @Test
    void listeInizialmenteVuote() {
        Hackathon h = creaHackathonDiTest();
        assertTrue(h.getStaff().isEmpty());
        assertTrue(h.getIscrizioni().isEmpty());
    }

    @Test
    void validazione_nomeCorto() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("Hi", periodo, BigDecimal.valueOf(1000), "Milano", 4, 3, LocalDateTime.now().plusDays(1), "Regolamento", 5)
        );
    }

    @Test
    void validazione_premioNegativo() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("HackFest", periodo, BigDecimal.valueOf(0), "Milano", 4, 3, LocalDateTime.now().plusDays(1), "Regolamento", 5)
        );
    }

    @Test
    void validazione_luogoCorto() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("HackFest", periodo, BigDecimal.valueOf(1000), "Mi", 4, 3, LocalDateTime.now().plusDays(1), "Regolamento", 5)
        );
    }

    @Test
    void validazione_teamMinInferiore3() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("HackFest", periodo, BigDecimal.valueOf(1000), "Milano", 4, 2, LocalDateTime.now().plusDays(1), "Regolamento", 5)
        );
    }

    @Test
    void validazione_teamMaxMinoreTeamMin() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("HackFest", periodo, BigDecimal.valueOf(1000), "Milano", 3, 4, LocalDateTime.now().plusDays(1), "Regolamento", 5)
        );
    }

    @Test
    void validazione_scadenzaNelPassato() {
        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Hackathon("HackFest", periodo, BigDecimal.valueOf(1000), "Milano", 4, 3, LocalDateTime.now().minusHours(1), "Regolamento", 5)
        );
    }
}