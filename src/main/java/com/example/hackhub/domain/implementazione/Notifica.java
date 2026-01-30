package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.TipoNotifica;

import java.util.ArrayList;
import java.util.List;

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
     * @param idNotifica l'identificativo della notifica
     * @param idMittente l'identificativo del mittente
     * @param messaggio il messaggio da mostrare ai destinatari
     * @param tipo il tipo di notifica
     */
    public Notifica(String idNotifica, String idMittente, String messaggio, TipoNotifica tipo) {
        this.idNotifica = idNotifica;
        this.idMittente = idMittente;
        this.idDestinatari = new ArrayList<>();
        this.messaggio = messaggio;
        this.tipo = tipo;
    }
}
