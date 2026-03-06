package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.implementazione.Hackathon;

public class InCorso implements StatoHackathon {
    public static final InCorso INSTANCE = new InCorso();
    private InCorso(){}

    @Override
    public void avviaValutazione(Hackathon hackathon) {
        hackathon.setStato(ValutazioneInCorso.INSTANCE);
    }

    @Override
    public void verificaInvioSottomissioneConsentito(Hackathon hackathon) {
        // consentito
    }
}
