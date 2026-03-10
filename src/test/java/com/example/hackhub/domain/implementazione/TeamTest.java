package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private void assegnaId(Object obj) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("assegnaId");
        m.setAccessible(true);
        m.invoke(obj);
    }

    @Test
    void getNome() {
        Team team = new Team("HackMasters");

        assertEquals("HackMasters", team.getNome());
    }

    @Test
    void getIdTeam() throws Exception {
        Team team = new Team("HackMasters");

        assegnaId(team);

        assertNotNull(team.getIdTeam());
        assertTrue(team.getIdTeam().startsWith("T-"));
    }

    @Test
    void getNumMembriIniziale() {
        Team team = new Team("HackMasters");

        assertEquals(0, team.getNumMembri());
    }

    @Test
    void aggiungiMembro() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("HackMasters");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        team.aggiungiMembro(membro);

        assertEquals(1, team.getNumMembri());
        assertTrue(team.getMembri().contains(membro));
    }

    @Test
    void aggiungiLeaderLanciaEccezione() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("HackMasters");

        MembroTeam leader = new MembroTeam(utente, team, RuoloTeam.LEADER);

        assertThrows(Exception.class, () -> team.aggiungiMembro(leader));
    }

    @Test
    void getMembri() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("HackMasters");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        team.aggiungiMembro(membro);

        assertEquals(1, team.getMembri().size());
        assertEquals(membro, team.getMembri().get(0));
    }

    @Test
    void aggiungiImpostaLeader() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Team team = new Team("HackMasters");

        MembroTeam membro = new MembroTeam(utente, team, RuoloTeam.MEMBRO);

        team.aggiungiMembro(membro);

        assertEquals(1, team.getNumMembri());
        assertTrue(team.getMembri().contains(membro));

        team.setLeader(membro);

        assertEquals(1, team.getNumMembri());
        assertEquals(RuoloTeam.LEADER, membro.getRuolo());
    }
}