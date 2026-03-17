package com.example.hackhub.testJunit.implementazione;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class IscrizioneTeamTest {
/*
    @Test
    void getTeam() {
        Team team = new Team("Team Alfa");
        Hackathon hackathon = new Hackathon();

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);

        assertEquals(team, iscrizione.getTeam());
    }

    @Test
    void getHackathon() {
        Team team = new Team("Team Alfa");
        Hackathon hackathon = new Hackathon();

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);

        assertEquals(hackathon, iscrizione.getHackathon());
    }

    @Test
    void assegnaIdGeneraIdSeNull() throws Exception {
        Team team = new Team("Team Alfa");
        Hackathon hackathon = new Hackathon();
        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);

        Method m = IscrizioneTeam.class.getDeclaredMethod("assegnaId");
        m.setAccessible(true);
        m.invoke(iscrizione);

        assertNotNull(iscrizione.getTeam()); // attenzione: costruttore vuoto non inizializza team
        assertNotNull(iscrizione.getHackathon()); // idem
        assertNotNull(iscrizione.getId());
        assertTrue(iscrizione.getId().startsWith("I-"));
    }

    // metodo helper per ottenere l'id privato via reflection
    private String getId(IscrizioneTeam iscrizione) throws Exception {
        Method getter = IscrizioneTeam.class.getDeclaredMethod("getIdIscrizione");
        getter.setAccessible(true);
        return (String) getter.invoke(iscrizione);
    }

    @Test
    void costruttoreInizializzaTeamEHackathon() {
        Team team = new Team("Team Beta");
        Hackathon hackathon = new Hackathon();

        IscrizioneTeam iscrizione = new IscrizioneTeam(team, hackathon);

        assertAll(
                () -> assertEquals(team, iscrizione.getTeam()),
                () -> assertEquals(hackathon, iscrizione.getHackathon())
        );
    }*/
}