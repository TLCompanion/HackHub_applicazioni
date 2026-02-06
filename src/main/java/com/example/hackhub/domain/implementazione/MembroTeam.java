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

    //private String idUtente
    @OneToOne(optional = false)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    //private String idTeam
    @OneToOne(optional = false)
    @JoinColumn(name = "id_team")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloTeam ruolo; //ruolo che il membro ricopre all'interno del Team

    public MembroTeam() {}

    /**
     * Creazione di un membro del team
     * @param id l'identificativo univoco per il membro del team
     * @param utente l'utente che diventa membro del team
     * @param team il team a cui appartiene
     * @param ruolo il ruolo del membro nel team
     */
    public MembroTeam(String id, Utente utente, Team team, RuoloTeam ruolo){
        this.id = id;
        this.utente = utente;
        this.team = team;
        this.ruolo = ruolo;
    }

    /**
     * Creazione di un membro del team
     * @param utente l'utente che diventa membro del team
     * @param team il team a cui appartiene
     * @param ruoloTeam il ruolo del membro del team
     */
    public MembroTeam(Utente utente, Team team, RuoloTeam ruoloTeam) {
        this.utente = utente;
        this.team = team;
        this.ruolo = ruoloTeam;
    }


    // METODI GETTER

    public String getId() { return id; }

    public String getIdUtente() { return utente.getIdUtente(); }

    public String getIdTeam() { return team.getIdTeam(); }

    public RuoloTeam getRuolo() { return ruolo; }
}
