package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoHackathon;

public class InCorso implements StatoHackathon {
    public static final InCorso INSTANCE = new InCorso();
    private InCorso(){}

    @Override
    public void avviaValutazione(Hackathon hackathon) {
        hackathon.setStato(ValutazioneInCorso.INSTANCE);
        // notifica nel service che gestisce l’evento
    }

    @Override
    public void verificaInvioSottomissioneConsentito(Hackathon hackathon) {
        // consentito
    }
}
