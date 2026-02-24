package com.example.hackhub.domain.implementazione.statePattern;

import com.example.hackhub.domain.StatoHackathon;

public class IscrizioniChiuse implements StatoHackathon {
    public static final IscrizioniChiuse INSTANCE = new IscrizioniChiuse();
    public IscrizioniChiuse() {
    }
}
