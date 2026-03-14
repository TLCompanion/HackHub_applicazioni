package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.implementazione.Hackathon;

public class IscrizioniAperte implements StatoHackathon {

    public static final IscrizioniAperte INSTANCE = new IscrizioniAperte();
    //Le iscrizioni vengono aperte quando sia il gi
    private IscrizioniAperte(){}

    @Override
    public void verificaIscrizioneConsentita(Hackathon hackathon) {
        //Consentita
    }

    @Override
    public void chiudiIscrizioni(Hackathon hackathon) {
        hackathon.setStato(IscrizioniChiuse.INSTANCE);
    }
}
