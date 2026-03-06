package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    @Test
    void getRuolo() {
        Utente utente = new Utente();
        Hackathon hackathon = new Hackathon();
        Staff staff = new Staff(utente, hackathon, RuoloStaff.ORGANIZZATORE);

        assertEquals(RuoloStaff.ORGANIZZATORE, staff.getRuolo());
    }

    @Test
    void getIdHackathon() {
        Hackathon hackathon = new Hackathon();
        Utente utente = new Utente();

        Staff staff = new Staff(utente, hackathon, RuoloStaff.ORGANIZZATORE);

        assertEquals(hackathon.getIdHackathon(), staff.getIdHackathon());
    }

    @Test
    void getIdUtente() {
        Utente utente = new Utente();
        Hackathon hackathon = new Hackathon();

        Staff staff = new Staff(utente, hackathon, RuoloStaff.ORGANIZZATORE);

        assertEquals(utente.getIdUtente(), staff.getIdUtente());
    }

    @Test
    void getUtente() {
        Utente utente = new Utente();
        Hackathon hackathon = new Hackathon();

        Staff staff = new Staff(utente, hackathon, RuoloStaff.ORGANIZZATORE);

        assertEquals(utente, staff.getUtente());
    }

    @Test
    void getHackathon() {
        Utente utente = new Utente();
        Hackathon hackathon = new Hackathon();

        Staff staff = new Staff(utente, hackathon, RuoloStaff.ORGANIZZATORE);

        assertEquals(hackathon, staff.getHackathon());
    }

    // utile per verificare che il costruttore colleghi correttamente tutto
    @Test
    void costruttoreImpostaCorrettamenteIValori() {
        Utente utente = new Utente();
        Hackathon hackathon = new Hackathon();

        Staff staff = new Staff(utente, hackathon, RuoloStaff.GIUDICE);

        assertAll(
                () -> assertEquals(utente, staff.getUtente()),
                () -> assertEquals(hackathon, staff.getHackathon()),
                () -> assertEquals(RuoloStaff.GIUDICE, staff.getRuolo())
        );
    }
}