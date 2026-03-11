package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("INVITO_STAFF")
public class InvitoStaff extends Richiesta {

    @Transient
    private Hackathon hackathon;
    private RuoloStaff ruolo;

    public InvitoStaff() {}

    /**
     * Costruttore che instanzia un invito ad unirsi allo Staff di un Hackathon
     * @param nomeMittente
     * @param payload
     * @param destinatario
     * @param scadenza
     * @param hackathon
     * @param ruolo
     */
    public InvitoStaff(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza, Hackathon hackathon, RuoloStaff ruolo) {
        super(nomeMittente, payload, destinatario, scadenza);
        this.hackathon = hackathon;
        this.ruolo = ruolo;
    }

    @Override
    public void accetta() {
        this.setStato(StatoRichiesta.ACCETTATO);
        Staff staff = new Staff(this.getDestinatario(), hackathon, this.ruolo);
        hackathon.aggiungiStaff(staff);
    }


    // METODI GETTER

    public Hackathon getHackathon() { return hackathon; }

    public RuoloStaff getRuolo() { return ruolo; }
}
