package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;
import com.example.hackhub.servizi.esterni.Calendario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PROPOSTA_CALL")
public class PropostaCall extends Richiesta {

    private Periodo periodo; // Periodo che indica l'arco di tempo di durata della call

    public PropostaCall() {}

    /**
     * Costruttore che istanzia una Proposta di call
     * @param nomeMittente
     * @param payload
     * @param destinatario
     * @param scadenza
     * @param periodo
     */
    public PropostaCall(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza, Periodo periodo) {
        super(nomeMittente, payload, destinatario, scadenza);
        this.periodo = periodo;
    }
}
