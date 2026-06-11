package unicam.cs.hackhub.backend.servizi;

import unicam.cs.hackhub.backend.handler.EventiTemporaliHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerHackathon {
    private final EventiTemporaliHandler eventiTemporaliHandler;

    public SchedulerHackathon(EventiTemporaliHandler eventiTemporaliHandler) {
        this.eventiTemporaliHandler = eventiTemporaliHandler;
    }

    @Scheduled(fixedRate = 60000)
    public void eseguiControlloScadenze() {
        eventiTemporaliHandler.gestisciScadenzeTemporali();
    }
}
