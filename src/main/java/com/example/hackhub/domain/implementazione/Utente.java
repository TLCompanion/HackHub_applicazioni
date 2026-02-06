package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.Ruolo;
import com.example.hackhub.domain.Subscriber;
import com.example.hackhub.domain.TipoNotifica;
import jakarta.persistence.*;

/**
 * Un generico Utente che può avere ruoli diversi e che utilizza la piattaforma HackHub in base al ruolo
 * corrente, e dunque ai suoi scopi.
 */
@Entity
@Table(name = "utenti")
public class Utente implements Subscriber {

    @Id
    @Column(nullable = false, updatable = false)
    private String idUtente; // Identificativo univoco dell'Utente

    @Column(nullable = false, unique = true)
    private String nomeUtente; // Nome identificativo dell'Utente, unico in tutta la piattaforma

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo; // Ruolo corrente dell'Utente dentro la piattaforma

    public Utente() {}
    /**
     * Crea un Utente, dato un nome, un id e un ruolo.
     *
     * @param nome -> Il nomeUtente inserito dal nuovo Utente
     * @param id -> l'identificativo univoco di tale Utente
     * //@param r -> Il ruolo che l'Utente assume alla creazione
     */
    public Utente(String nome, String id, Ruolo r) {
        this.nomeUtente = nome;
        this.idUtente = id;
        this.ruolo = r;
    }

    /**
     * Metodo di aggiornamento per ricevere nuovi eventi inerenti l'Hackathon a cui il Team di questo Utente
     * è iscritto.
     */
    public void update(TipoNotifica evento) {
        // TODO implementare
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNomeUtente() { return this.nomeUtente; }

    public String getIdUtente() { return idUtente; }

    public Ruolo getRuolo() { return ruolo; }


}
