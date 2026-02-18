package com.example.hackhub.domain.implementazione;


import com.example.hackhub.domain.StatoRichiesta;

/**
 * Classe che gestisce una proposta di call tra mentori di un'hackathon e team che hanno bisogno di assistenza
 */
public class PropostaCall<T> extends Richiesta {

    //private String idHackathon
    private Hackathon hackathon;
    private Periodo periodo;

    /**
     * Crea una proposta di chiamata da parte di un mentore per un team
     * @param idRichiesta l'identificativo della richiesta
     * @param nomeMittente il mittente
     * @param stato lo stato della richiesta
     * @param hackathon l'hackathon associato alla call
     * @param periodo il periodo di tempo proposto
     */
    public PropostaCall(String idRichiesta, String nomeMittente, StatoRichiesta stato, Hackathon hackathon, Periodo periodo) {
        super(nomeMittente);
        this.hackathon= hackathon;
        this.periodo = periodo;
    }
}
