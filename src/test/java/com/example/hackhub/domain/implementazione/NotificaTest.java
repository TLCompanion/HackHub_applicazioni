package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.TipoNotifica;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class NotificaTest {
/*
    @Test
    void getIdNotifica_generatoDaPrePersist() throws Exception {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Notifica notifica = new Notifica("Messaggio importante", destinatario, TipoNotifica.VALUTAZIONE_CONCLUSA);

        Method m = Notifica.class.getDeclaredMethod("assegnaId");
        m.setAccessible(true);
        m.invoke(notifica);

        assertNotNull(notifica.getIdNotifica());
        assertTrue(notifica.getIdNotifica().startsWith("N-"));
    }

    @Test
    void getDestinatario() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Notifica notifica = new Notifica("Messaggio", destinatario, TipoNotifica.VALUTAZIONE_CONCLUSA);

        assertEquals(destinatario, notifica.getDestinatario());
    }

    @Test
    void getTipo() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Notifica notifica = new Notifica("Messaggio", destinatario, TipoNotifica.VALUTAZIONE_CONCLUSA);

        assertEquals(TipoNotifica.VALUTAZIONE_CONCLUSA, notifica.getTipo());
    }

    @Test
    void getPayload() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Notifica notifica = new Notifica("Messaggio", destinatario, TipoNotifica.VALUTAZIONE_CONCLUSA);

        assertEquals("Messaggio", notifica.getPayload());
    }

    @Test
    void costruttoreInizializzaTuttiICampi() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Notifica notifica = new Notifica("Valutazione conclusa!", destinatario, TipoNotifica.VALUTAZIONE_CONCLUSA);

        assertAll(
                () -> assertEquals(destinatario, notifica.getDestinatario()),
                () -> assertEquals(TipoNotifica.VALUTAZIONE_CONCLUSA, notifica.getTipo()),
                () -> assertEquals("Valutazione conclusa!", notifica.getPayload())
        );
    }*/
}