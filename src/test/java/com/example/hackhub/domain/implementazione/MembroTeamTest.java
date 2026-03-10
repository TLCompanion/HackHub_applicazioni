package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MembroTeamTest {

    private void assegnaId(Object obj, String metodo) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(metodo);
        m.setAccessible(true);
        m.invoke(obj);
    }

    @Test
    void getId() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assegnaId(membro, "assegnaId");

        assertNotNull(membro.getId());
        assertTrue(membro.getId().startsWith("MT-"));
    }

    @Test
    void getIdUtente() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(utente.getIdUtente(), membro.getIdUtente());
    }

    @Test
    void getIdTeam() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        assegnaId(utente, "assegnaId");
        assegnaId(team, "assegnaId");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(team.getIdTeam(), membro.getIdTeam());
    }

    @Test
    void getRuolo() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(RuoloTeam.MEMBRO, membro.getRuolo());
    }

    @Test
    void getUtente() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(utente, membro.getUtente());
    }

    @Test
    void getTeam() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(team, membro.getTeam());
    }

    @Test
    void addAndSetRuolo() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("TeamX");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        assertEquals(RuoloTeam.MEMBRO, membro.getRuolo());

        membro.setRuolo(RuoloTeam.LEADER);

        assertEquals(RuoloTeam.LEADER, membro.getRuolo());
    }
}