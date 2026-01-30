package com.example.hackhub.domain.implementazione;


import com.example.hackhub.domain.StatoRichiesta;

/**
 * Classe che gestisce una proposta di call tra mentori di un'hackathon e team che hanno bisogno di assistenza
 */
public class PropostaCall extends Richiesta {

    private String idHackathon;
    private Periodo periodo;

    /**
     * Crea una proposta di chiamata da parte di un mentore per un team
     * @param idRichiesta l'identificativo della richiesta
     * @param idMittente l'identificativo del mittente
     * @param stato lo stato della richiesta
     * @param idHackathon l'identificativo dell'hackathon
     * @param periodo il periodo di tempo proposto
     */
    public PropostaCall(String idRichiesta, String idMittente, StatoRichiesta stato, String idHackathon, Periodo periodo) {
        super(idRichiesta, idMittente, stato);
        this.idHackathon = idHackathon;
        this.periodo = periodo;
    }
}
