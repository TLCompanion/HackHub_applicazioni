package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.StatoRichiesta;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("INVITO_TEAM")
public class InvitoTeam extends Richiesta {

    @Transient
    private Team team;

    public InvitoTeam() {}
    /**
     * Costruttore che inizializza un invito ad entrare in un team
     * @param nomeMittente
     * @param payload
     * @param destinatario
     * @param scadenza
     * @param team
     */
    public InvitoTeam(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza, Team team) {
        super(nomeMittente, payload, destinatario, scadenza);
        this.team = team;
    }

    @Override
    public void accetta() {
        this.setStato(StatoRichiesta.ACCETTATO);
        MembroTeam membro = new MembroTeam(this.getDestinatario(), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(membro);
    }


    // METODI GETTER

    public Team getTeam() { return team; }
}
