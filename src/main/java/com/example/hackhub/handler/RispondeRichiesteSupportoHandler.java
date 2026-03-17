package com.example.hackhub.handler;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.Notifica;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

@Service
public class RispondeRichiesteSupportoHandler {

    private final RepositoryNotifica repositoryNotifica;
    private final RepositoryStaff repositoryStaff;
    private final ServizioNotifiche servizioNotifiche;

    /**
     * Metodo che inizializza questo handler
     * @param servizioNotifiche singleton del ServizioNotifiche
     */
    public RispondeRichiesteSupportoHandler(RepositoryNotifica repositoryNotifica, RepositoryStaff repositoryStaff, ServizioNotifiche servizioNotifiche) {
        this.repositoryNotifica = repositoryNotifica;
        this.repositoryStaff = repositoryStaff;
        this.servizioNotifiche = servizioNotifiche;
    }

    /**
     * Metodo che permette a un mentore di un hackathon di rispondere a una notifica che richiede supporto,
     * e con cui il mentore risponde con una proposta di call oppure con una notifica in risposta
     * @param nomeUtente il nome utente del mentore che risponde alla richiesta di supporto
     * @param idNotifica l'id della notifica considerata
     */
    public void rispondiRichiestaSupportoConNotifica(String nomeUtente, String idNotifica) {
        Notifica notifica = repositoryNotifica.findByIdNotifica(idNotifica)
                .orElseThrow(() -> new RuntimeException("Notifica non trovata"));
        Staff staff = repositoryStaff.findByUtente_NomeUtente(nomeUtente)
                .orElseThrow(() -> new RuntimeException("Staff non trovato"));
        servizioNotifiche.creaNotifica(notifica.getDestinatario(), TipoNotifica.RICHIESTA_SUPPORTO,
                staff.getUtente().getNomeUtente() + " ha risposto alla tua richiesta di supporto: Consigli per superare il problema");
    }
}
