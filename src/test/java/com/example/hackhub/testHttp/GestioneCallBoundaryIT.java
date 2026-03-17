package com.example.hackhub.testHttp;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GestioneCallBoundaryIT {

    private static final String ENDPOINT = "/api/call/propostaCall";

    private static final String MENTORE = "giuseppe";
    private static final String LEADER = "francesca";
    private static final String MEMBRO = "mario";
    private Hackathon hackathon;
    private Team team;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryHackathon repositoryHackathon;

    @Autowired
    private RepositoryUtenti repositoryUtenti;

    @Autowired
    private RepositoryTeam repositoryTeam;

    @Autowired
    private RepositoryMembriTeam repositoryMembriTeam;

    @MockitoSpyBean
    private ServizioNotifiche servizioNotifiche;

    @BeforeEach
    void setUp() {
        repositoryMembriTeam.deleteAllInBatch();
        repositoryTeam.deleteAllInBatch();
        repositoryHackathon.deleteAllInBatch();
        repositoryUtenti.deleteAllInBatch();

        repositoryMembriTeam.flush();
        repositoryTeam.flush();
        repositoryHackathon.flush();
        repositoryUtenti.flush();

        repositoryUtenti.saveAndFlush(creaUtente(MENTORE));
        repositoryUtenti.saveAndFlush(creaUtente(LEADER));
        repositoryUtenti.saveAndFlush(creaUtente(MEMBRO));

        clearInvocations(servizioNotifiche);
        hackathon = creaHackathonValido();
        team = creaTeamValido();
    }

    @Test
    void avviaPropostaCall_noContent() throws Exception {
        Utente utenteMentore = utente(MENTORE);
        Utente utenteLeader = utente(LEADER);

        hackathon.aggiungiStaff(new Staff(utenteMentore, RuoloStaff.MENTORE));
        hackathon.aggiungiIscrizione(new IscrizioneTeam(team, hackathon));

        repositoryHackathon.saveAndFlush(hackathon);

        repositoryMembriTeam.saveAndFlush(new MembroTeam(utenteLeader, team, RuoloTeam.LEADER));
        repositoryMembriTeam.saveAndFlush(new MembroTeam(utente(MEMBRO), team, RuoloTeam.MEMBRO));

        String body = jsonPropostaCall(hackathon.getIdHackathon(), team.getIdTeam(), "2026-06-21", "15:30:00");

        eseguiProposta(body, MENTORE)
                .andExpect(status().isNoContent());

        verify(servizioNotifiche, times(1))
                .creaPropostaCall(
                        eq(MENTORE),
                        eq(utenteLeader),
                        any(Periodo.class)
                );

        verifyNoMoreInteractions(servizioNotifiche);
    }

    @Test
    void avviaPropostaCall_utenteNonMentore_forbidden() throws Exception {
        hackathon.aggiungiIscrizione(new IscrizioneTeam(team, hackathon));
        repositoryHackathon.saveAndFlush(hackathon);

        repositoryMembriTeam.saveAndFlush(new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER));

        String body = jsonPropostaCall(hackathon.getIdHackathon(), team.getIdTeam(), "2026-06-21", "15:30:00");

        eseguiProposta(body, LEADER)
                .andExpect(status().isForbidden());

        verify(servizioNotifiche, never()).creaPropostaCall(any(), any(), any());
    }

    @Test
    void avviaPropostaCall_hackathonNonEsistente_notFound() throws Exception {
        String body = jsonPropostaCall("hack_inesistente", team.getIdTeam(), "2026-06-21", "15:30:00");

        eseguiProposta(body, MENTORE)
                .andExpect(status().isNotFound());

        verify(servizioNotifiche, never()).creaPropostaCall(any(), any(), any());
    }

    @Test
    void avviaPropostaCall_teamNonIscritto_conflict() throws Exception {
        hackathon.aggiungiStaff(new Staff(utente(MENTORE), RuoloStaff.MENTORE));
        repositoryHackathon.saveAndFlush(hackathon);

        String body = jsonPropostaCall(hackathon.getIdHackathon(), "team_non_iscritto", "2026-06-21", "15:30:00");

        eseguiProposta(body, MENTORE)
                .andExpect(status().isConflict());

        verify(servizioNotifiche, never()).creaPropostaCall(any(), any(), any());
    }

    @Test
    void avviaPropostaCall_dopoFineHackathon_conflict() throws Exception {
        hackathon.aggiungiStaff(new Staff(utente(MENTORE), RuoloStaff.MENTORE));
        hackathon.aggiungiIscrizione(new IscrizioneTeam(team, hackathon));
        repositoryHackathon.saveAndFlush(hackathon);

        repositoryMembriTeam.saveAndFlush(new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER));

        String body = jsonPropostaCall(hackathon.getIdHackathon(), team.getIdTeam(), "2026-06-23", "15:30:00");

        eseguiProposta(body, MENTORE)
                .andExpect(status().isConflict());

        verify(servizioNotifiche, never()).creaPropostaCall(any(), any(), any());
    }

    @Test
    void avviaPropostaCall_bodyVuoto_badRequest() throws Exception {
        String body = """
                {
                }
                """;

        eseguiProposta(body, MENTORE)
                .andExpect(status().isBadRequest());

        verify(servizioNotifiche, never()).creaPropostaCall(any(), any(), any());
    }

    private ResultActions eseguiProposta(String body, String nomeUtente) throws Exception {
        return mockMvc.perform(post(ENDPOINT)
                .with(authentication(autenticazione(nomeUtente)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private UsernamePasswordAuthenticationToken autenticazione(String nomeUtente) {
        return new UsernamePasswordAuthenticationToken(
                nomeUtente,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private String jsonPropostaCall(String idHackathon, String idTeam, String data, String ora) {
        return """
                {
                  "idHackathon": "%s",
                  "idTeam": "%s",
                  "data": "%s",
                  "ora": "%s"
                }
                """.formatted(idHackathon, idTeam, data, ora);
    }

    private Utente creaUtente(String nomeUtente) {
        return new Utente(nomeUtente, nomeUtente + "@example.com", "password123");
    }

    private Utente utente(String nomeUtente) {
        return repositoryUtenti.findByNomeUtente(nomeUtente)
                .orElseThrow(() -> new AssertionError("Utente non trovato: " + nomeUtente));
    }

    private Hackathon creaHackathonValido() {

        return new Hackathon(
                "Hackathon Call Test",
                new Periodo(
                        LocalDate.of(2026, 6, 20),
                        LocalDate.of(2026, 6, 22)
                ),
                new BigDecimal("1000.00"),
                "Camerino",
                5,
                3,
                java.time.LocalDateTime.of(2026, 6, 19, 23, 59),
                "Regolamento di prova",
                20
        );
    }

    private Team creaTeamValido() {
        Team team = new Team("Team Alpha");
        return repositoryTeam.saveAndFlush(team);
    }
}