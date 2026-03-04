package com.example.hackhub.domain.implementazione.FactoryPattern;


import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.Periodo;

/**
 * Classe che gestisce una proposta di call tra mentori di un'hackathon e team che hanno bisogno di assistenza
 */
public class PayloadPropostaCall extends Payload {

    //private String idHackathon
    private String nomeHackathon;
    private Periodo periodo;

    /**
     * Crea una proposta di chiamata da parte di un mentore per un team
     * @param nomeHackathon l'hackathon associato alla call
     * @param periodo il periodo di tempo proposto
     */
    public PayloadPropostaCall(String nomeHackathon, Periodo periodo) {
        super(TipoNotifica.PROPOSTA_CALL);
        this.nomeHackathon= nomeHackathon;
        this.periodo = periodo;
    }

    @Override
    public TipoNotifica getTipo(){
        return TipoNotifica.PROPOSTA_CALL;
    }
}
