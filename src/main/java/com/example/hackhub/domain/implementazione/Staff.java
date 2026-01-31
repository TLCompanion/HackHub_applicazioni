package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import jakarta.persistence.*;

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

    @Column(nullable = false)
    private String idUtente;

    @Column(nullable = false)
    private String idHackathon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloStaff ruolo;

    /**
     * Creazione di un membro dello staff
     * @param idUtente l'identificativo dell'utente
     * @param idHackathon l'identificativo dell'hackathon associato
     * @param ruolo il ruolo ricoperto
     */
    public Staff(String idUtente, String idHackathon, RuoloStaff ruolo) {
        this.idUtente = idUtente;
        this.idHackathon = idHackathon;
        this.ruolo = ruolo;
    }

    public Staff() {}


    //metodi getter

    public RuoloStaff getRuolo() {return ruolo;}

    public String getIdHackathon() {return idHackathon;}

    public String getIdUtente() {return idUtente;}
}
