package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.Ruolo;
import com.example.hackhub.domain.Subscriber;

/**
 * Un generico Utente che può avere ruoli diversi e che utilizza la piattaforma HackHub in base al ruolo
 * corrente, e dunque ai suoi scopi.
 */
public class Utente implements Subscriber {
    private String nomeUtente; // Nome identificativo dell'Utente, unico in tutta la piattaforma
    private String idUtente; // Identificativo univoco dell'Utente
    //private Ruolo ruolo; // Ruolo corrente dell'Utente dentro la piattaforma

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
        //this.ruolo = r;
    }

    /**
     * Metodo di aggiornamento per ricevere nuovi eventi inerenti l'Hackathon a cui il Team di questo Utente
     * è iscritto.
     */
    public void update() {
        // TODO implementare
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNomeUtente() { return this.nomeUtente; }

    public String getIdUtente() { return idUtente; }

    //public Ruolo getRuolo() { return ruolo; }
}
