package com.example.hackhub.servizi;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Periodo;
import com.example.hackhub.domain.implementazione.statePattern.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class HackathonBuilderTest {

    private HackathonBuilder builder() {
        return new HackathonBuilder();
    }

    private Periodo periodoDiTest() {
        return new Periodo(
                LocalDate.now().plusDays(1), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0)
        );
    }

    @Test
    void impostaNome() {
        HackathonBuilder b = builder();
        b.impostaNome("HackFest");
        assertEquals("HackFest", b.getNome());
    }

    @Test
    void impostaPeriodo() {
        HackathonBuilder b = builder();
        Periodo p = periodoDiTest();
        b.impostaPeriodo(p);
        assertEquals(p, b.getPeriodo());
    }

    @Test
    void impostaPremio() {
        HackathonBuilder b = builder();
        BigDecimal premio = BigDecimal.valueOf(1000);
        b.impostaPremio(premio);
        assertEquals(premio, b.getPremio());
    }

    @Test
    void impostaLuogo() {
        HackathonBuilder b = builder();
        b.impostaLuogo("Milano");
        assertEquals("Milano", b.getLuogo());
    }

    @Test
    void impostaTeamMax() {
        HackathonBuilder b = builder();
        b.impostaTeamMax(6);
        assertEquals(6, b.getTeamMax());
    }

    @Test
    void impostaTeamMin() {
        HackathonBuilder b = builder();
        b.impostaTeamMin(3);
        assertEquals(3, b.getTeamMin());
    }

    @Test
    void impostaRegolamento() {
        HackathonBuilder b = builder();
        b.impostaRegolamento("Regolamento ufficiale");
        assertEquals("Regolamento ufficiale", b.getRegolamento());
    }

    @Test
    void impostaScadenzaIscrizioni() {
        HackathonBuilder b = builder();
        LocalDateTime scadenza = LocalDateTime.now().plusDays(1);
        b.impostaScadenzaIscrizioni(scadenza);
        assertEquals(scadenza, b.getScadenzaIscrizioni());
    }

    @Test
    void impostaMaxIscrizioni() {
        HackathonBuilder b = builder();
        b.impostaMaxIscrizioni(5);
        assertEquals(5, b.getMaxIscrizioni());
    }

    @Test
    void getRisultato() {
        HackathonBuilder b = builder();
        b.impostaNome("HackFest");
        Periodo p = periodoDiTest();
        b.impostaPeriodo(p);
        b.impostaPremio(BigDecimal.valueOf(1000));
        b.impostaLuogo("Milano");
        b.impostaTeamMax(6);
        b.impostaTeamMin(3);
        b.impostaRegolamento("Regolamento ufficiale");
        LocalDateTime scadenza = LocalDateTime.now().plusDays(1);
        b.impostaScadenzaIscrizioni(scadenza);
        b.impostaMaxIscrizioni(5);

        Hackathon h = b.getRisultato();

        assertEquals("HackFest", h.getNome());
        assertEquals(p, h.getPeriodo());
        assertEquals(BigDecimal.valueOf(1000), h.getPremio());
        assertEquals("Milano", h.getLuogo());
        assertEquals(6, h.getTeamMax());
        assertEquals(3, h.getTeamMin());
        assertEquals("Regolamento ufficiale", h.getRegolamento());
        assertEquals(scadenza, h.getScadenzaIscrizioni());
        assertEquals(5, h.getMaxIscrizioni());
        assertNotNull(h.getIscrizioni());
        assertNotNull(h.getStaff());
        assertTrue(h.getStato() instanceof IscrizioniAperte); // stato iniziale corretto
    }

    // ---- TEST GETTER DIRETTI DEL BUILDER ----
    @Test
    void getMaxIscrizioni() {
        HackathonBuilder b = builder();
        b.impostaMaxIscrizioni(7);
        assertEquals(7, b.getMaxIscrizioni());
    }

    @Test
    void getScadenzaIscrizioni() {
        HackathonBuilder b = builder();
        LocalDateTime sc = LocalDateTime.now().plusDays(2);
        b.impostaScadenzaIscrizioni(sc);
        assertEquals(sc, b.getScadenzaIscrizioni());
    }

    @Test
    void getRegolamento() {
        HackathonBuilder b = builder();
        b.impostaRegolamento("Regolamento");
        assertEquals("Regolamento", b.getRegolamento());
    }

    @Test
    void getTeamMin() {
        HackathonBuilder b = builder();
        b.impostaTeamMin(3);
        assertEquals(3, b.getTeamMin());
    }

    @Test
    void getTeamMax() {
        HackathonBuilder b = builder();
        b.impostaTeamMax(6);
        assertEquals(6, b.getTeamMax());
    }

    @Test
    void getLuogo() {
        HackathonBuilder b = builder();
        b.impostaLuogo("Roma");
        assertEquals("Roma", b.getLuogo());
    }

    @Test
    void getPremio() {
        HackathonBuilder b = builder();
        b.impostaPremio(BigDecimal.valueOf(1500));
        assertEquals(BigDecimal.valueOf(1500), b.getPremio());
    }

    @Test
    void getPeriodo() {
        HackathonBuilder b = builder();
        Periodo p = periodoDiTest();
        b.impostaPeriodo(p);
        assertEquals(p, b.getPeriodo());
    }

    @Test
    void getNome() {
        HackathonBuilder b = builder();
        b.impostaNome("CodeFest");
        assertEquals("CodeFest", b.getNome());
    }

    @Test
    void testBuilderIndipendenzaTraCostruzioni() {
        HackathonBuilder builder = new HackathonBuilder();

        // Primo Hackathon
        builder.impostaNome("Hackathon Uno");
        builder.impostaPeriodo(new Periodo(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
        builder.impostaPremio(BigDecimal.valueOf(1000));
        builder.impostaLuogo("Roma");
        builder.impostaTeamMax(5);
        builder.impostaTeamMin(3);
        builder.impostaRegolamento("Regolamento A");
        builder.impostaScadenzaIscrizioni(LocalDateTime.now().plusDays(1));
        builder.impostaMaxIscrizioni(10);

        Hackathon h1 = builder.getRisultato();

        // Secondo Hackathon con valori diversi
        builder.impostaNome("Hackathon Due");
        builder.impostaPeriodo(new Periodo(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)));
        builder.impostaPremio(BigDecimal.valueOf(2000));
        builder.impostaLuogo("Milano");
        builder.impostaTeamMax(6);
        builder.impostaTeamMin(4);
        builder.impostaRegolamento("Regolamento B");
        builder.impostaScadenzaIscrizioni(LocalDateTime.now().plusDays(3));
        builder.impostaMaxIscrizioni(12);

        Hackathon h2 = builder.getRisultato();

        // Verifica che i due Hackathon siano diversi e isolati
        assertNotEquals(h1.getNome(), h2.getNome());
        assertNotEquals(h1.getPremio(), h2.getPremio());
        assertNotEquals(h1.getPeriodo(), h2.getPeriodo());
        assertNotEquals(h1.getLuogo(), h2.getLuogo());
        assertNotEquals(h1.getRegolamento(), h2.getRegolamento());
        assertNotEquals(h1.getTeamMax(), h2.getTeamMax());
        assertNotEquals(h1.getTeamMin(), h2.getTeamMin());
        assertNotEquals(h1.getMaxIscrizioni(), h2.getMaxIscrizioni());
        assertNotEquals(h1.getScadenzaIscrizioni(), h2.getScadenzaIscrizioni());
    }

    @Test
    void resetAzzeramentoCampi() {
        // Chiamiamo reset
        HackathonBuilder builder = new HackathonBuilder();
        builder.reset();

        // Verifichiamo che tutti i campi siano tornati al valore di default
        assertNull(builder.getNome(), "Nome dovrebbe essere null dopo reset");
        assertNull(builder.getPeriodo(), "Periodo dovrebbe essere null dopo reset");
        assertNull(builder.getPremio(), "Premio dovrebbe essere null dopo reset");
        assertNull(builder.getLuogo(), "Luogo dovrebbe essere null dopo reset");
        assertEquals(0, builder.getTeamMax(), "TeamMax dovrebbe essere 0 dopo reset");
        assertEquals(0, builder.getTeamMin(), "TeamMin dovrebbe essere 0 dopo reset");
        assertNull(builder.getRegolamento(), "Regolamento dovrebbe essere null dopo reset");
        assertNull(builder.getScadenzaIscrizioni(), "ScadenzaIscrizioni dovrebbe essere null dopo reset");
        assertEquals(0, builder.getMaxIscrizioni(), "MaxIscrizioni dovrebbe essere 0 dopo reset");
    }

    @Test
    void resetPuòEssereRichiamatoPiùVolte() {
        // Chiamata multipla del reset non deve generare eccezioni e lasciare i campi a default
        HackathonBuilder builder = new HackathonBuilder();
        builder.reset();
        builder.reset();

        assertNull(builder.getNome());
        assertNull(builder.getPeriodo());
        assertNull(builder.getPremio());
        assertNull(builder.getLuogo());
        assertEquals(0, builder.getTeamMax());
        assertEquals(0, builder.getTeamMin());
        assertNull(builder.getRegolamento());
        assertNull(builder.getScadenzaIscrizioni());
        assertEquals(0, builder.getMaxIscrizioni());
    }

    @Test
    void resetBloccaGetRisultato() {
        HackathonBuilder builder = new HackathonBuilder();
        // impostiamo valori validi
        builder.impostaNome("Hackathon Test");
        builder.impostaPeriodo(new Periodo(LocalDate.now().plusDays(1), LocalTime.of(9,0),
                LocalDate.now().plusDays(2), LocalTime.of(18,0)));
        builder.impostaPremio(BigDecimal.valueOf(1000));
        builder.impostaLuogo("Milano");
        builder.impostaTeamMax(6);
        builder.impostaTeamMin(3);
        builder.impostaRegolamento("Regolamento test");
        builder.impostaScadenzaIscrizioni(LocalDateTime.now().plusDays(10));
        builder.impostaMaxIscrizioni(10);

        // resettare
        builder.reset();

        // getRisultato ora deve fallire per valori mancanti
        assertThrows(NullPointerException.class, builder::getRisultato);
    }

    @Test
    void resetSeguitoDaNuovaCreazioneFunziona() {
        HackathonBuilder builder = new HackathonBuilder();

        // impostiamo valori validi
        builder.impostaNome("Hackathon Nuovo");
        builder.impostaPeriodo(new Periodo(LocalDate.now().plusDays(3), LocalTime.of(10,0),
                LocalDate.now().plusDays(4), LocalTime.of(18,0)));
        builder.impostaPremio(BigDecimal.valueOf(500));
        builder.impostaLuogo("Roma");
        builder.impostaTeamMax(6);
        builder.impostaTeamMin(3);
        builder.impostaRegolamento("Nuovo regolamento");
        builder.impostaScadenzaIscrizioni(LocalDateTime.now().plusDays(5));
        builder.impostaMaxIscrizioni(5);

        // Reset
        builder.reset();

        // impostiamo nuovamente valori validi
        builder.impostaNome("Hackathon Nuovo");
        builder.impostaPeriodo(new Periodo(LocalDate.now().plusDays(3), LocalTime.of(10,0),
                LocalDate.now().plusDays(4), LocalTime.of(18,0)));
        builder.impostaPremio(BigDecimal.valueOf(500));
        builder.impostaLuogo("Roma");
        builder.impostaTeamMax(6);
        builder.impostaTeamMin(3);
        builder.impostaRegolamento("Nuovo regolamento");
        builder.impostaScadenzaIscrizioni(LocalDateTime.now().plusDays(5));
        builder.impostaMaxIscrizioni(5);

        Hackathon hackathon = builder.getRisultato();

        assertNotNull(hackathon);
        assertEquals("Hackathon Nuovo", hackathon.getNome());
        assertEquals(BigDecimal.valueOf(500), hackathon.getPremio());
        assertEquals("Roma", hackathon.getLuogo());
        assertEquals(6, hackathon.getTeamMax());
        assertEquals(3, hackathon.getTeamMin());
        assertEquals("Nuovo regolamento", hackathon.getRegolamento());
        assertEquals(5, hackathon.getMaxIscrizioni());
        assertNotNull(hackathon.getPeriodo());
    }
}