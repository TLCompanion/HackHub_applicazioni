package com.example.hackhub.testHttp;


import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.*;
import com.example.hackhub.servizi.ServizioNotifiche;
import com.example.hackhub.servizi.esterni.CalendarioMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class GestisciRichiesteBoundaryIT {


    private static final String ENDPOINT = "/api/richieste";
    private static final String DESTinatARIO = "dest";
    private static final String MITTENTE = "mittente";
    private static final String ORGANIZZATORE = "org";


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RepositoryUtenti repositoryUtenti;


    @Autowired
    private RepositoryRichiesta repositoryRichiesta;


    @Autowired
    private RepositoryHackathon repositoryHackathon;


    @Autowired
    private RepositoryStaff repositoryStaff;


    @Autowired
    private RepositoryMembriTeam repositoryMembriTeam;


    @Autowired
    private RepositoryTeam repositoryTeam;


    @Autowired
    private RepositoryNotifica repositoryNotifica;


    @MockitoSpyBean
    private ServizioNotifiche servizioNotifiche;


    @MockitoSpyBean
    private CalendarioMock calendario;


    private Utente destinatario;


    @BeforeEach
    void setUp() {
        repositoryNotifica.deleteAllInBatch();
        repositoryRichiesta.deleteAllInBatch();
        repositoryStaff.deleteAllInBatch();
        repositoryMembriTeam.deleteAllInBatch();
        repositoryTeam.deleteAllInBatch();
        repositoryHackathon.deleteAllInBatch();
        repositoryUtenti.deleteAllInBatch();


        repositoryNotifica.flush();
        repositoryRichiesta.flush();
        repositoryStaff.flush();
        repositoryMembriTeam.flush();
        repositoryTeam.flush();
        repositoryHackathon.flush();
        repositoryUtenti.flush();


        repositoryUtenti.saveAndFlush(creaUtente(DESTinatARIO));
        repositoryUtenti.saveAndFlush(creaUtente(MITTENTE));
        repositoryUtenti.saveAndFlush(creaUtente(ORGANIZZATORE));


        destinatario = utente(DESTinatARIO);


        clearInvocations(servizioNotifiche);
        clearInvocations(calendario);
    }


    @Test
    void accettaInvitoStaff_ok() throws Exception {
        Hackathon hackathon = creaHackathonValido();
        // organizzatote
        Utente org = utente(ORGANIZZATORE);
        hackathon.aggiungiStaff(new Staff(org, RuoloStaff.ORGANIZZATORE));
        repositoryHackathon.saveAndFlush(hackathon);


        InvitoStaff inv = new InvitoStaff(MITTENTE, "msg", destinatario, LocalDateTime.now().plusDays(1), hackathon, RuoloStaff.GIUDICE);
        repositoryRichiesta.saveAndFlush(inv);


        mockMvc.perform(post(ENDPOINT + "/%s/accetta".formatted(inv.getIdRichiesta()))
                        .with(authentication(auth())))
                .andExpect(status().isAccepted());


        Hackathon h = repositoryHackathon.findByIdFetchStaff(hackathon.getIdHackathon()).orElseGet(() -> repositoryHackathon.findById(hackathon.getIdHackathon()).orElseThrow());
        assertTrue(h.getStaff().stream().anyMatch(s -> s.getRuolo() == RuoloStaff.GIUDICE));


        List<Notifica> not = repositoryNotifica.findAll();
        assertEquals(1, not.size());
        verify(servizioNotifiche, times(1)).creaNotifica(any(), eq(TipoNotifica.ACCETTA_RICHIESTA), any());
    }


    @Test
    void accettaInvitoTeam_ok() throws Exception {
        Team team = new Team("TeamX");
        repositoryTeam.saveAndFlush(team);


        // Ensure the team has a leader (domain invariant: teams should have a leader)
        Utente leaderUser = utente(ORGANIZZATORE);
        MembroTeam leader = new MembroTeam(leaderUser, team, com.example.hackhub.domain.RuoloTeam.LEADER);
        repositoryMembriTeam.saveAndFlush(leader);


        // ensure the team is persisted before creating the request
        InvitoTeam inv = new InvitoTeam(MITTENTE, "msg", destinatario, LocalDateTime.now().plusDays(1), repositoryTeam.findById(team.getIdTeam()).orElseThrow());
        repositoryRichiesta.saveAndFlush(inv);


        // sanity check: destinatario should exist in DB and be retrievable
        assertTrue(repositoryUtenti.findByNomeUtente(DESTinatARIO).isPresent(), "destinatario must exist before request");


        mockMvc.perform(post(ENDPOINT + "/%s/accetta".formatted(inv.getIdRichiesta()))
                        .with(authentication(auth())))
                .andExpect(status().isAccepted());


        // verify the request was accepted and a notification was created
        Richiesta saved = repositoryRichiesta.findById(inv.getIdRichiesta()).orElseThrow();
        assertEquals(com.example.hackhub.domain.StatoRichiesta.ACCETTATO, saved.getStato());
        verify(servizioNotifiche, times(1)).creaNotifica(any(), eq(TipoNotifica.ACCETTA_RICHIESTA), any());
    }


    @Test
    void accettaPropostaLeader_ok() throws Exception {
        Team team = new Team("TeamLeader");
        repositoryTeam.saveAndFlush(team);
        // No pre-existing leader to avoid state merge issues; handler should set destinatario as leader
        PropostaLeader prop = new PropostaLeader(MITTENTE, "msg", destinatario, LocalDateTime.now().plusDays(1), team);
        repositoryRichiesta.saveAndFlush(prop);


        mockMvc.perform(post(ENDPOINT + "/%s/accetta".formatted(prop.getIdRichiesta()))
                        .with(authentication(auth())))
                .andExpect(status().isAccepted());


        Team t = repositoryTeam.findByIdFetchMembri(team.getIdTeam()).orElseGet(() -> repositoryTeam.findById(team.getIdTeam()).orElseThrow());
        assertTrue(t.getMembri().stream().anyMatch(m -> m.getRuolo() == com.example.hackhub.domain.RuoloTeam.LEADER && m.getUtente().getNomeUtente().equals(DESTinatARIO)));
    }


    @Test
    void accettaPropostaCall_ok() throws Exception {
        // Set up team and members
        Team team = new Team("CallTeam");
        repositoryTeam.saveAndFlush(team);
        // destinatario is member of team
        MembroTeam membro = new MembroTeam(destinatario, team, com.example.hackhub.domain.RuoloTeam.MEMBRO);
        repositoryMembriTeam.saveAndFlush(membro);


        // create mentor staff: fetch existing user created in setUp and attach as staff
        assertTrue(repositoryUtenti.findByNomeUtente(MITTENTE).isPresent(), "mentore (mittente) must exist");
        Utente mentore = repositoryUtenti.findByNomeUtente(MITTENTE).orElseThrow();
        Staff s = new Staff(mentore, RuoloStaff.MENTORE);
        // staff must be attached to some hackathon; create hackathon and add staff
        Hackathon h = creaHackathonValido();
        h.aggiungiStaff(s);
        repositoryHackathon.saveAndFlush(h);


        // re-fetch managed references to avoid transient association issues
        Utente destinatarioManaged = repositoryUtenti.findByNomeUtente(DESTinatARIO).orElseThrow();


        PropostaCall pc = new PropostaCall(MITTENTE, "payload", destinatarioManaged, LocalDateTime.now().plusDays(1), new Periodo(
                java.time.LocalDate.now().plusDays(1), java.time.LocalTime.of(10,0),
                java.time.LocalDate.now().plusDays(1), java.time.LocalTime.of(11,0)
        ));
        repositoryRichiesta.saveAndFlush(pc);


        mockMvc.perform(post(ENDPOINT + "/%s/accetta".formatted(pc.getIdRichiesta()))
                        .with(authentication(auth())))
                .andExpect(status().isAccepted());


        // calendario should have been invoked
        verify(calendario, times(1)).salvaCall(any());
        verify(servizioNotifiche, times(1)).creaNotifica(any(), eq(TipoNotifica.ACCETTA_RICHIESTA), any());
    }


    @Test
    void rifiutaRichiesta_ok() throws Exception {
        Team t = new Team("TReject");
        repositoryTeam.saveAndFlush(t);
        Team tManaged = repositoryTeam.findById(t.getIdTeam()).orElseThrow();
        InvitoTeam inv = new InvitoTeam(MITTENTE, "msg", destinatario, LocalDateTime.now().plusDays(1), tManaged);
        repositoryRichiesta.saveAndFlush(inv);


        mockMvc.perform(post(ENDPOINT + "/%s/rifiuta".formatted(inv.getIdRichiesta()))
                        .with(authentication(auth())))
                .andExpect(status().isOk());


        Richiesta r = repositoryRichiesta.findById(inv.getIdRichiesta()).orElseThrow();
        assertEquals(com.example.hackhub.domain.StatoRichiesta.RIFIUTATO, r.getStato());
        verify(servizioNotifiche, times(1)).creaNotifica(any(), eq(TipoNotifica.RIFIUTO_RICHIESTA), any());
    }


    @Test
    void accettaRichiesta_userNotFound_notFound() throws Exception {
        Team t = new Team("T2");
        repositoryTeam.saveAndFlush(t);
        Team tManaged = repositoryTeam.findById(t.getIdTeam()).orElseThrow();
        InvitoTeam inv = new InvitoTeam(MITTENTE, "msg", destinatario, LocalDateTime.now().plusDays(1), tManaged);
        repositoryRichiesta.saveAndFlush(inv);


        mockMvc.perform(post(ENDPOINT + "/%s/accetta".formatted(inv.getIdRichiesta()))
                        .with(authentication(new UsernamePasswordAuthenticationToken("utenteInesistente", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))))))
                .andExpect(status().isNotFound());
    }


    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(
                GestisciRichiesteBoundaryIT.DESTinatARIO,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


    // helper methods
    private Utente creaUtente(String nomeUtente) {
        return new Utente(nomeUtente, nomeUtente + "@example.com", "pwd");
    }


    private Utente utente(String nomeUtente) {
        return repositoryUtenti.findByNomeUtente(nomeUtente)
                .orElseThrow(() -> new AssertionError("Utente non trovato: " + nomeUtente));
    }


    private Hackathon creaHackathonValido() {
        return repositoryHackathon.saveAndFlush(new Hackathon(
                "HackTest-" + System.nanoTime(),
                new Periodo(java.time.LocalDate.now().plusDays(1), java.time.LocalTime.of(9,0), java.time.LocalDate.now().plusDays(2), java.time.LocalTime.of(18,0)),
                java.math.BigDecimal.valueOf(1000),
                "Loc",
                5,
                3,
                java.time.LocalDateTime.now().plusDays(1),
                "reg",
                10
        ));
    }
}
