package unicam.cs.hackhub.backend.domain.implementazione.statePattern;

import unicam.cs.hackhub.backend.domain.implementazione.Hackathon;

public class IscrizioniAperte implements StatoHackathon {

    public static final IscrizioniAperte INSTANCE = new IscrizioniAperte();

    private IscrizioniAperte() {}

    @Override
    public void verificaIscrizioneConsentita(Hackathon hackathon) {
    }

    @Override
    public void verificaNominaMentoriConsentita(Hackathon hackathon) {
    }

    @Override
    public void verificaEliminazioneConsentita(Hackathon hackathon) {
    }

    @Override
    public void verificaAnnullamentoIscrizioneConsentito(Hackathon hackathon) {
    }

    @Override
    public void chiudiIscrizioni(Hackathon hackathon) {
        hackathon.setStato(IscrizioniChiuse.INSTANCE);
    }
}
