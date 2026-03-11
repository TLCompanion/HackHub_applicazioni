package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import jakarta.persistence.*;

import java.util.UUID;

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
     * @param utente l'utente che diventa membro del team
     * @param team il team a cui appartiene
     * @param ruoloTeam il ruolo del membro del team
     */
    public MembroTeam(Utente utente, Team team, RuoloTeam ruoloTeam) {
        this.utente = utente;
        this.team = team;
        this.ruolo = ruoloTeam;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco al membro team se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.

    /**
     * Assegna un id univoco ad ogni membro di un team
     */
    @PrePersist
    private void assegnaId() {
        if (this.id == null) {
            this.id = "MT-" + UUID.randomUUID();
        }
    }

    // METODI GETTER

    public String getId() { return id; }

    public String getIdUtente() { return utente.getIdUtente(); }

    public String getIdTeam() { return team.getIdTeam(); }

    public RuoloTeam getRuolo() { return ruolo; }

    public Utente getUtente() { return utente; }

    public Team getTeam() { return team; }

    // METODO SETTER

    public void setRuolo(RuoloTeam r) { this.ruolo = r; }
}
