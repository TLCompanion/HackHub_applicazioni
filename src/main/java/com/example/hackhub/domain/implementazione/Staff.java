package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Classe che gestisce un singolo membro dello staff per un'hackathon e lo associa all'hackathon
 * in cui lavora
 */
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @Column(nullable = false, updatable = false)
    private String idStaff;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id_utente", nullable = false)
    private Utente utente;

    @ManyToOne(optional = true)
    @JoinColumn(name = "hackathon_id_hackathon", nullable = true)
    private Hackathon hackathon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloStaff ruolo;

    /**
     * Creazione di un membro dello staff
     * @param utente l'utente associato allo staff
     * @param ruolo il ruolo ricoperto
     */
    // N.B. Non è necessario passare l'hackathon come parametro, poiché lo staff viene associato all'hackathon tramite
    // il metodo setHackathon() dopo la creazione dello staff, in questo modo si evita di creare un ciclo di dipendenze
    // tra le classi Staff e Hackathon, che potrebbe complicare la gestione delle entità e delle relazioni tra di esse.
    public Staff(Utente utente,  RuoloStaff ruolo) {
        this.utente = utente;
        this.ruolo = ruolo;
    }

    public Staff() {}

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idStaff == null) {
            this.idStaff = "MS-" + UUID.randomUUID();
        }
    }

    //metodi getter

    public RuoloStaff getRuolo() {return ruolo;}

    public String getIdHackathon() {return hackathon.getIdHackathon();}

    public String getIdUtente() {return utente.getIdUtente();}

    public Utente getUtente() {
        return utente;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public String getIdStaff() {
        return idStaff;
    }
}
