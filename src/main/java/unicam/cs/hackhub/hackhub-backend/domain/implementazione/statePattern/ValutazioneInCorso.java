package unicam.cs.hackhub.backend.domain.implementazione.statePattern;

import unicam.cs.hackhub.backend.domain.implementazione.Hackathon;

public class ValutazioneInCorso implements StatoHackathon {

    public static final ValutazioneInCorso INSTANCE = new ValutazioneInCorso();

    private ValutazioneInCorso() {}

    @Override
    public void verificaValutazioneConsentita(Hackathon hackathon) {
    }

    @Override
    public void verificaPropostaDiCallConsentita(Hackathon hackathon) {
    }

    @Override
    public void concludiHackathon(Hackathon hackathon) {
        hackathon.setStato(Concluso.INSTANCE);
    }
}
