package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.eccezioni.ForbiddenException;
import jakarta.persistence.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembroTeam> membri; //insieme degli utenti che fanno parte del team

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
    public void aggiungiMembro(MembroTeam membro) {
        if (membro.getRuolo().equals(RuoloTeam.LEADER))
            throw new IllegalArgumentException("Tentativo di aggiungere un Leader a un Team");
        membri.add(membro);
        membro.setTeam(this);
    }

    public void setLeader(MembroTeam membro) throws ForbiddenException {
        if (this.hasLeader()) throw new ForbiddenException("Il team ha già un leader");
        membri.add(membro);
    }

    private boolean hasLeader() {
        for (MembroTeam m : membri)
            if (m.getRuolo() == RuoloTeam.LEADER) return true;
        return false;
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNome() { return this.nome; }

    public String getIdTeam() { return idTeam; }

    public List<MembroTeam> getMembri() { return membri;}

    public void setNome(String nome) {
        this.nome = nome;
    }
}
