package com.example.hackhub.domain.implementazione;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Un generico Utente che può avere ruoli diversi e che utilizza la piattaforma HackHub in base al ruolo
 * corrente, e dunque ai suoi scopi.
 */
@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @Column(nullable = false, updatable = false)
    private String idUtente; // Identificativo univoco dell'Utente

    @Column(nullable = false, unique = true)
    private String nomeUtente; // Nome identificativo dell'Utente, unico in tutta la piattaforma

    /**
     * Crea un Utente, dato un nome, un id e un ruolo.
     *
     * @param nome -> Il nomeUtente inserito dal nuovo Utente
     */
    public Utente(String nome) {
        this.nomeUtente = nome;
    }

    public Utente() {

    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'utente se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idUtente == null) {
            this.idUtente = "U-" + UUID.randomUUID();
        }
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNomeUtente() { return this.nomeUtente; }

    public String getIdUtente() { return idUtente; }


}
