package com.example.hackhub.domain.implementazione;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoTest {
/*
    @Test
    void getDataInizio() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(2);

        Periodo periodo = new Periodo(inizio, LocalTime.of(10,0), fine, LocalTime.of(18,0));

        assertEquals(inizio, periodo.getDataInizio());
    }

    @Test
    void getOraInizio() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(2);

        Periodo periodo = new Periodo(inizio, LocalTime.of(10,0), fine, LocalTime.of(18,0));

        assertEquals(LocalTime.of(10,0), periodo.getOraInizio());
    }

    @Test
    void getDataFine() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(2);

        Periodo periodo = new Periodo(inizio, LocalTime.of(10,0), fine, LocalTime.of(18,0));

        assertEquals(fine, periodo.getDataFine());
    }

    @Test
    void getOraFine() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(2);

        Periodo periodo = new Periodo(inizio, LocalTime.of(10,0), fine, LocalTime.of(18,0));

        assertEquals(LocalTime.of(18,0), periodo.getOraFine());
    }

    @Test
    void periodoConDateValide() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(1);

        Periodo periodo = new Periodo(inizio, LocalTime.of(9,0), fine, LocalTime.of(18,0));

        assertNotNull(periodo);
    }

    @Test
    void dataInizioNelPassatoLanciaEccezione() {
        LocalDate ieri = LocalDate.now().minusDays(1);
        LocalDate domani = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                new Periodo(ieri, LocalTime.of(10,0), domani, LocalTime.of(18,0))
        );
    }

    @Test
    void costruttoreConOrariDefault() {
        LocalDate inizio = LocalDate.now().plusDays(1);
        LocalDate fine = inizio.plusDays(1);

        Periodo periodo = new Periodo(inizio, fine);

        assertEquals(LocalTime.of(0,0), periodo.getOraInizio());
        assertEquals(LocalTime.of(23,59), periodo.getOraFine());
    }*/
}