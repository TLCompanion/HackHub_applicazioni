package com.example.hackhub.handler;

import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.springframework.stereotype.Service;

import static com.example.hackhub.domain.TipoNotifica.VIOLAZIONE_REGOLAMENTO;

@Service
public class GestisceHackathonHandler {

    private final ServizioNotifiche servizioNotifiche;
    private final RepositoryStaff repositoryStaff;

    public GestisceHackathonHandler(ServizioNotifiche servizioNotifiche, RepositoryStaff repositoryStaff) {
        this.servizioNotifiche = servizioNotifiche;
        this.repositoryStaff = repositoryStaff;
    }

    public void segnalaViolazione(String idOrganizzatore, Team team){
        Staff organizzatore = repositoryStaff.getStaffById(idOrganizzatore);
        servizioNotifiche.creaNotifica(organizzatore.getUtente(), VIOLAZIONE_REGOLAMENTO,"Il team " + team.getNome() + " ha violato il regolamento dell'hackathon");
    }
}
