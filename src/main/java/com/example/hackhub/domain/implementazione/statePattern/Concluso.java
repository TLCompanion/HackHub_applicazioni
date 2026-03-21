package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.implementazione.Hackathon;

public class Concluso implements StatoHackathon {
    public static final Concluso INSTANCE = new Concluso();

    private Concluso() {}

    @Override
    public void verificaProclamazioneConsentita(Hackathon h) {
    }

    @Override
    public void verificaLiquidazionePremioConsentita(Hackathon h) {
    }
}
