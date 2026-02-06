package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import jakarta.persistence.*;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Un Team registrato nella piattaforma, di cui fanno parte un gruppo di Utenti, di cui uno è il Leader,
 * ovvero l'Utente che ha creato il Team.
 */
@Entity
@Table(name = "team")
public class Team {

    @Id
    @Column(nullable = false, updatable = false)
    private String idTeam; // identificativo univoco del team

    @Column(nullable = false, unique = true)
    private String nome; // nome del team, unico nella piattaforma

    @Transient
    private Collection<MembroTeam> membri; //insieme degli utenti che fanno parte del team

    public Team() {} // Costruttore vuoto richiesto per la persistenza nel DB
    /**
     * Metodo che crea un nuovo Team.
     *
     * @param nome il nome del Team
     * @param id l'identificativo del team
     */
    public Team(String nome, String id) {
        this.nome = nome;
        this.idTeam = id;
        this.membri = new ArrayList<>();
    }

    public Team(String nome) {
        this.nome = nome;
    }

    /**
     * Metodo che ritorna il numero di membri del team
     *
     * @return il numero di membri del team
     */
    public int getNumMembri() { return this.membri.size(); }

    /**
     * Metodo che aggiunge un membro a questo Team
     *
     * @param membro il membro da aggiungere
     *
     * @throws Exception se il membro da aggiungere risulta LEADER
     */
    // TODO che ne pensate di questa implementazione? Exception è puramente indicativo
    public void aggiungiMembro(MembroTeam membro) throws Exception {
        if (membro.getRuolo().equals(RuoloTeam.LEADER)) throw new Exception("Tentativo di aggiungere un" +
                " Leader a un Team");

        membri.add(membro);
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNome() { return this.nome; }

    public String getIdTeam() { return idTeam; }

    public Collection<MembroTeam> getMembri() { return membri;}
}
