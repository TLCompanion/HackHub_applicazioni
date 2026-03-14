package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.implementazione.Hackathon;

public class ValutazioneInCorso implements StatoHackathon {

    public static final ValutazioneInCorso INSTANCE = new ValutazioneInCorso();
    private ValutazioneInCorso(){}

    @Override
    public void verificaValutazioneConsentita(Hackathon hackathon) {
        // Consentita
    }

    @Override
    public void concludiHackathon(Hackathon hackathon) {
        hackathon.setStato(Concluso.INSTANCE);
        hackathon.setStatoEnum(Concluso.INSTANCE);
    }
}
