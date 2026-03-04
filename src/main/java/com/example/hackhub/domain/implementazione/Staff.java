package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Classe che gestisce un singolo membro dello staff per un'hackathon e lo associa all'hackathon
 * in cui lavora
 */
@Entity
@Table(name = "staff", uniqueConstraints = @UniqueConstraint(columnNames = {"idUtente", "idHackathon"}))
public class Staff {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_hackathon")
    private Hackathon hackathon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloStaff ruolo;

    /**
     * Creazione di un membro dello staff
     * @param utente l'utente associato allo staff
     * @param hackathon l'hackathon associato allo staff
     * @param ruolo il ruolo ricoperto
     */
    public Staff(Utente utente, Hackathon hackathon, RuoloStaff ruolo) {
        this.utente = utente;
        this.hackathon = hackathon;
        this.ruolo = ruolo;
    }

    public Staff() {}

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.id == null) {
            this.id = "MS-" + UUID.randomUUID();
        }
    }

    //metodi getter

    public RuoloStaff getRuolo() {return ruolo;}

    public String getIdHackathon() {return hackathon.getIdHackathon();}

    public String getIdUtente() {return utente.getIdUtente();}

    public Utente getUtente() {
        return utente;
    }
}
