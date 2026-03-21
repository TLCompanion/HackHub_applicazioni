package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.implementazione.Hackathon;

public class IscrizioniChiuse implements StatoHackathon {
    public static final IscrizioniChiuse INSTANCE = new IscrizioniChiuse();

    private IscrizioniChiuse() {}

    @Override
    public void avviaHackathon(Hackathon hackathon) {
        hackathon.setStato(InCorso.INSTANCE);
    }
}