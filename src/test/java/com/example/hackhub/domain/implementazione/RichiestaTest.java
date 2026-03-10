package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;
import com.example.hackhub.domain.TipoRichiesta;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RichiestaTest {

    @Test
    void getIdRichiesta_generatoDaPrePersist() throws Exception {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        Method m = Richiesta.class.getDeclaredMethod("assegnaId");
        m.setAccessible(true);
        m.invoke(richiesta);

        assertNotNull(richiesta.getIdRichiesta());
        assertTrue(richiesta.getIdRichiesta().startsWith("R-"));
    }

    @Test
    void getMittente() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        assertEquals("Luca", richiesta.getMittente());
    }

    @Test
    void getDestinatario() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        assertEquals(destinatario, richiesta.getDestinatario());
    }

    @Test
    void getTipo_copreEntrambiITipi() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");

        Richiesta r1 = new Richiesta("Luca", "p", TipoRichiesta.PROPOSTA_CALL, destinatario, null);
        Richiesta r2 = new Richiesta("Anna", "p2", TipoRichiesta.INVITO_STAFF, destinatario, null);

        assertEquals(TipoRichiesta.PROPOSTA_CALL, r1.getTipo());
        assertEquals(TipoRichiesta.INVITO_STAFF, r2.getTipo());
    }

    @Test
    void getPeriodo() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Periodo periodo = new Periodo(LocalDate.now().plusDays(1), LocalTime.of(9,0),
                LocalDate.now().plusDays(2), LocalTime.of(18,0));

        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, periodo);

        assertEquals(periodo, richiesta.getPeriodo());
    }

    @Test
    void getPayload() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        assertEquals("payloadData", richiesta.getPayload());
    }

    @Test
    void getStato_inizialmenteInviato() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        assertEquals(StatoRichiesta.INVIATO, richiesta.getStato());
    }

    @Test
    void setStato_modificaStato() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.PROPOSTA_CALL, destinatario, null);

        richiesta.setStato(StatoRichiesta.ACCETTATO);
        assertEquals(StatoRichiesta.ACCETTATO, richiesta.getStato());

        richiesta.setStato(StatoRichiesta.RIFIUTATO);
        assertEquals(StatoRichiesta.RIFIUTATO, richiesta.getStato());
    }

    @Test
    void costruttoreInizializzaTuttiICampi() {
        Utente destinatario = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Periodo periodo = new Periodo(LocalDate.now().plusDays(1), LocalTime.of(9,0),
                LocalDate.now().plusDays(2), LocalTime.of(18,0));
        Richiesta richiesta = new Richiesta("Luca", "payloadData", TipoRichiesta.INVITO_STAFF, destinatario, periodo);

        assertAll(
                () -> assertEquals("Luca", richiesta.getMittente()),
                () -> assertEquals(TipoRichiesta.INVITO_STAFF, richiesta.getTipo()),
                () -> assertEquals(destinatario, richiesta.getDestinatario()),
                () -> assertEquals(periodo, richiesta.getPeriodo()),
                () -> assertEquals("payloadData", richiesta.getPayload()),
                () -> assertEquals(StatoRichiesta.INVIATO, richiesta.getStato())
        );
    }
}