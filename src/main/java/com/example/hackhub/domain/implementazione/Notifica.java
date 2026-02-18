package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.TipoNotifica;
import jakarta.persistence.PrePersist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce le notifiche da inviare alla fine dell'hackathon
 */
public class Notifica {

    private String idNotifica;
    private String idMittente;
    private List<String> idDestinatari;
    private String messaggio;
    private TipoNotifica tipo;

    /**
     * Creazione di una nuova notifica da inviare
     * @param idMittente l'identificativo del mittente
     * @param messaggio il messaggio da mostrare ai destinatari
     * @param tipo il tipo di notifica
     */
    public Notifica(String idMittente, String messaggio, TipoNotifica tipo) {
        this.idMittente = idMittente;
        this.idDestinatari = new ArrayList<>();
        this.messaggio = messaggio;
        this.tipo = tipo;
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


    // METODI GETTER

    public String getMessaggio() { return messaggio; }
}
