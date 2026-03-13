package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.TipoNotifica;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce le notifiche da inviare alla fine dell'hackathon
 */
@Entity
public class Notifica {

    @Id
    private String idNotifica;
    @Transient
    private Utente destinatario;
    @Column(insertable=false, updatable=false)
    private TipoNotifica tipo;
    private String payload;

    public Notifica() {}
    /**
     * Creazione di una notifica
     * @param payload il payload associato
     * @param destinatario i destinatari
     * @param tipo il tipo di notifica
     */
    public Notifica(String payload, Utente destinatario, TipoNotifica tipo) {
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.payload = payload;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco alla notifica se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.

    /**
     * Assegna un id univoco ad ogni notifica
     */
    @PrePersist
    private void assegnaId() {
        if (this.idNotifica == null) {
            this.idNotifica = "N-" + UUID.randomUUID();
        }
    }

    // METODI GETTER


    public String getIdNotifica() {
        return idNotifica;
    }

    public Utente getDestinatario() {
        return destinatario;
    }

    public TipoNotifica getTipo() {
        return tipo;
    }

    public String getPayload() {
        return payload;
    }
}
