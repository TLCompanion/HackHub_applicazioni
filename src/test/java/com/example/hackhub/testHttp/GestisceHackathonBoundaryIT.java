package com.example.hackhub.testHttp;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.InvitoStaff;
import com.example.hackhub.domain.implementazione.Notifica;
import com.example.hackhub.domain.implementazione.Periodo;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.domain.implementazione.statePattern.InCorso;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryRichiesta;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GestisceHackathonBoundaryIT {


    private static final String BASE_URL = "/api/gestisciHackathon";
    private static final String NOME_UTENTE = "organizzatore";


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RepositoryUtenti repositoryUtenti;


    @Autowired
    private RepositoryStaff repositoryStaff;


    @Autowired
    private RepositoryTeam repositoryTeam;


    @Autowired
    private RepositoryHackathon repositoryHackathon;


    @Autowired
    private RepositoryNotifica repositoryNotifica;


    @Autowired
    private RepositoryRichiesta repositoryRichiesta;


    @Autowired
    private EntityManager entityManager;


    private Utente organizzatore;
    private Hackathon hackathon;


    @BeforeEach
    void setUp() {
        repositoryRichiesta.deleteAll();
        repositoryRichiesta.flush();


        repositoryNotifica.deleteAll();
        repositoryNotifica.flush();


        repositoryStaff.deleteAll();
        repositoryStaff.flush();


        repositoryTeam.deleteAll();
        repositoryTeam.flush();


        repositoryHackathon.deleteAll();
        repositoryHackathon.flush();


        repositoryUtenti.deleteAll();
        repositoryUtenti.flush();


        organizzatore = new Utente(NOME_UTENTE, "organizzatore@mail.com", "Password123!");
        repositoryUtenti.saveAndFlush(organizzatore);


        Periodo periodo = new Periodo(
                LocalDate.now().plusDays(10),
                LocalTime.of(9, 0),
                LocalDate.now().plusDays(12),
                LocalTime.of(18, 0)
        );


        hackathon = new Hackathon(
                "HackHub Challenge",
                periodo,
                BigDecimal.valueOf(1000),
                "Camerino",
                6,
                3,
                LocalDateTime.now().plusDays(5),
                "Regolamento test",
                10
        );
        repositoryHackathon.saveAndFlush(hackathon);


        Staff staffOrganizzatore = new Staff(organizzatore, RuoloStaff.ORGANIZZATORE);
        hackathon.aggiungiStaff(staffOrganizzatore);
        repositoryHackathon.saveAndFlush(hackathon);
        entityManager.flush();
    }


    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(
                NOME_UTENTE,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


    @Test
    void segnalaViolazione_ok() throws Exception {
        Team team = new Team("TeamAlpha");
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(BASE_URL + "/segnalaViolazione")
                        .with(authentication(auth()))
                        .param("nomeTeam", team.getNome()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));


        List<Notifica> notifiche = repositoryNotifica.findAll();
        assertEquals(1, notifiche.size());
        assertEquals(organizzatore.getIdUtente(), notifiche.getFirst().getDestinatario().getIdUtente());
        assertEquals(TipoNotifica.VIOLAZIONE_REGOLAMENTO, notifiche.getFirst().getTipo());
        assertEquals(
                "Il team " + team.getNome() + " ha violato il regolamento dell'hackathon",
                notifiche.getFirst().getPayload()
        );
    }


    @Test
    void segnalaViolazione_notFound() throws Exception {
        mockMvc.perform(post(BASE_URL + "/segnalaViolazione")
                        .with(authentication(auth()))
                        .param("nomeTeam", "T-inesistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team non trovato"));
    }


    @Test
    void nominaMentori_ok() throws Exception {
        Utente utenteDaInvitare = new Utente("mentorino", "mentorino@mail.com", "Password123!");
        repositoryUtenti.saveAndFlush(utenteDaInvitare);


        mockMvc.perform(put(BASE_URL + "/nominaMentori")
                        .with(authentication(auth()))
                        .param("nomeUtenteDaInvitare", "mentorino"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));


        List<?> richieste = repositoryRichiesta.findAll();
        assertEquals(1, richieste.size());
        assertInstanceOf(InvitoStaff.class, richieste.getFirst());


        InvitoStaff invitoStaff = (InvitoStaff) richieste.getFirst();
        assertEquals(NOME_UTENTE, invitoStaff.getMittente());
        assertEquals(utenteDaInvitare.getIdUtente(), invitoStaff.getDestinatario().getIdUtente());
        assertEquals(RuoloStaff.MENTORE, invitoStaff.getRuolo());
        assertEquals(hackathon.getIdHackathon(), invitoStaff.getHackathon().getIdHackathon());
    }


    @Test
    void nominaMentori_errore() throws Exception {
        Utente utenteDaInvitare = new Utente("mentorino", "mentorino@mail.com", "Password123!");
        repositoryUtenti.saveAndFlush(utenteDaInvitare);


        Staff staffGiaPresente = new Staff(utenteDaInvitare, RuoloStaff.MENTORE);
        hackathon.aggiungiStaff(staffGiaPresente);
        repositoryHackathon.saveAndFlush(hackathon);


        mockMvc.perform(put(BASE_URL + "/nominaMentori")
                        .with(authentication(auth()))
                        .param("nomeUtenteDaInvitare", "mentorino"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'utente da invitare è già nello staff"));
    }


    @Test
    void nominaMentori_notFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/nominaMentori")
                        .with(authentication(auth()))
                        .param("nomeUtenteDaInvitare", "utenteAssente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Utente da invitare non trovato"));
    }


    @Test
    void nominaMentori_errore_statoHackathonNonValido() throws Exception {
        Utente utenteDaInvitare = new Utente("mentorino", "mentorino@mail.com", "Password123!");
        repositoryUtenti.saveAndFlush(utenteDaInvitare);


        hackathon.setStato(InCorso.INSTANCE);
        hackathon.setStatoEnum(InCorso.INSTANCE);
        repositoryHackathon.saveAndFlush(hackathon);


        mockMvc.perform(put(BASE_URL + "/nominaMentori")
                        .with(authentication(auth()))
                        .param("nomeUtenteDaInvitare", "mentorino"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Non è possibile nominare mentori se le iscrizioni non sono aperte"));
    }
}

