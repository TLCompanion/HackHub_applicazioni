package com.example.hackhub.domain.implementazione.FactoryPattern;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;

/**
 * Gestione degli inviti da parte dell'organizzatore allo staff per l'hackathon
 */
public class PayloadInvitoStaff extends Payload {

    //private String idHackathon
    private String nomeHackathon;
    private RuoloStaff ruoloOfferto;

    /**
     * Crea un'invito da inviare ad un'utente per farlo diventare parte dello Staff di un'hackathon
     * @param nomeHackathon l'hackathon a cui è associato l'invito
     * @param ruoloOfferto il ruolo offerto dall'organizzatore
     */
    public PayloadInvitoStaff(String nomeHackathon, RuoloStaff ruoloOfferto) {
        super(TipoNotifica.INVITO_STAFF);
        this.nomeHackathon = nomeHackathon;
        this.ruoloOfferto = ruoloOfferto;
    }

    @Override
    public TipoNotifica getTipo(){
        return TipoNotifica.INVITO_STAFF;
    }
}
