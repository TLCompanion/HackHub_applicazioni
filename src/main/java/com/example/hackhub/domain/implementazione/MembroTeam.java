package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import jakarta.persistence.*;

/**
 * Un utente registrato alla piattaforma che diventa parte di un team
 */
@Entity
@Table(name = "membroTeam", uniqueConstraints = @UniqueConstraint(columnNames = "idUtente"))
public class MembroTeam {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String idUtente; //identificativo unico del membro del team

    @Column(nullable = false)
    private String idTeam; //identificativo unico che associa il membro del team al suo team

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloTeam ruolo; //ruolo che il membro ricopre all'interno del Team

    public MembroTeam() {}

    /**
     * Creazione di un membro del team
     * @param id l'identificativo univoco per il membro del team
     * @param idUtente l'identificativo dell'utente
     * @param idTeam l'identificativo del team associato al membro
     * @param ruolo il ruolo del membro nel team
     */
    public MembroTeam(String id, String idUtente, String idTeam, RuoloTeam ruolo){
        this.id = id;
        this.idUtente = idUtente;
        this.idTeam = idTeam;
        this.ruolo = ruolo;
    }

    /**
     * Creazione di un membro del team
     * @param idUtente l'identificativo dell'utente
     * @param idTeam l'identificativo del team associato al membro
     * @param ruoloTeam il ruolo del membro del team
     */
    public MembroTeam(String idUtente, String idTeam, RuoloTeam ruoloTeam) {
        this.idUtente = idUtente;
        this.idTeam = idTeam;
        this.ruolo = ruoloTeam;
    }


    // METODI GETTER

    public String getId() { return id; }

    public String getIdUtente() { return idUtente; }

    public String getIdTeam() { return idTeam; }

    public RuoloTeam getRuolo() { return ruolo; }
}
