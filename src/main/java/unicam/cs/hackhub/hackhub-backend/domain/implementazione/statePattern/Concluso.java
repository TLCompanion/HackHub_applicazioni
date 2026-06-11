package unicam.cs.hackhub.backend.domain.implementazione.statePattern;

import unicam.cs.hackhub.backend.domain.implementazione.Hackathon;

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
