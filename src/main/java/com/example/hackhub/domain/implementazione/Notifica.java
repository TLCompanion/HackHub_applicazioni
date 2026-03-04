package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.FactoryPattern.Payload;
import jakarta.persistence.Embedded;
import jakarta.persistence.PrePersist;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce le notifiche da inviare alla fine dell'hackathon
 */
public class Notifica {

    private String idNotifica;
    private String idMittente;
    private List<Utente> destinatari;
    private TipoNotifica tipo;
    @Embedded
    private Payload payload;

    /**
     * Creazione di una notifica
     * @param idMittente il mittente
     * @param payload il payload associato
     * @param destinatari i destinatari
     * @param tipo il tipo di notifica
     */
    public Notifica(String idMittente, Payload payload, List<Utente> destinatari, TipoNotifica tipo) {
        this.idMittente = idMittente;
        this.destinatari = destinatari;
        this.tipo = tipo;
        this.payload = payload;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco alla notifica se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idNotifica == null) {
            this.idNotifica = "N-" + UUID.randomUUID();
        }
    }
}
