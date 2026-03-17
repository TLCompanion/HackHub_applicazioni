package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("INVITO_STAFF")
public class InvitoStaff extends Richiesta {

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_hackathon", nullable = false)
    private Hackathon hackathon;
    @Enumerated(EnumType.STRING)
    private RuoloStaff ruolo;

    public InvitoStaff() {}

    /**
     * Costruttore che instanzia un invito a unirsi allo Staff di un Hackathon
     * @param nomeMittente il nome del mittente
     * @param payload il messaggio
     * @param destinatario il destinatario
     * @param scadenza la scadenza dell'invito
     * @param hackathon l'hackathon associato
     * @param ruolo il ruolo che l'utente dovrebbe ricoprire
     */
    public InvitoStaff(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza, Hackathon hackathon, RuoloStaff ruolo) {
        super(nomeMittente, payload, destinatario, scadenza);
        this.hackathon = hackathon;
        this.ruolo = ruolo;
    }

    @Override
    public void accetta() {
        this.setStato(StatoRichiesta.ACCETTATO);
        Staff staff = new Staff(this.getDestinatario(), this.ruolo);
        hackathon.aggiungiStaff(staff);
    }


    // METODI GETTER

    public Hackathon getHackathon() { return hackathon; }

    public RuoloStaff getRuolo() { return ruolo; }
}
