package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import jakarta.persistence.*;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
     */
    public Team(String nome) {
        this.nome = nome;
        this.membri = new ArrayList<>();
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco al team se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idTeam == null) {
            this.idTeam = "T-" + UUID.randomUUID();
        }
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
