package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PROPOSTA_LEADER")
public class PropostaLeader extends Richiesta {

    @ManyToOne
    @JoinColumn(name = "team_id_team")
    private Team team;

    public void setTeam(Team team) {
        this.team = team;
    }

    public PropostaLeader() {}
    /**
     * Costruttore che inizializza un invito a entrare in un team
     * @param nomeMittente il nome del mittente
     * @param payload il messaggio
     * @param destinatario il destinatario
     * @param scadenza la scadenza dell'invito
     * @param team il team
     */
    public PropostaLeader(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza, Team team) {
        super(nomeMittente, payload, destinatario, scadenza);
        this.team = team;
    }

    @Override
    public void accetta() {
        this.setStato(StatoRichiesta.ACCETTATO);
        for (MembroTeam membro : team.getMembri()) {
            if (membro.getRuolo() == RuoloTeam.LEADER) {
                membro.setRuolo(RuoloTeam.MEMBRO);
                break;
            }
        }
        MembroTeam leader = new MembroTeam(this.getDestinatario(), team, RuoloTeam.LEADER);
        team.setLeader(leader);
    }


    // METODI GETTER

    public Team getTeam() { return team; }
}
