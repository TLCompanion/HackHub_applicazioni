package com.example.hackhub.domain.implementazione;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class UtenteTest {

    @Test
    void getNomeUtente() {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");

        assertEquals("Mario", utente.getNomeUtente());
    }

    @Test
    void getIdUtente() throws Exception {
        Utente utente = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");

        // Richiamo manuale del metodo @PrePersist
        Method method = Utente.class.getDeclaredMethod("assegnaId");
        method.setAccessible(true);
        method.invoke(utente);

        String id = utente.getIdUtente();

        assertNotNull(id);
        assertTrue(id.startsWith("U-"));
    }
}