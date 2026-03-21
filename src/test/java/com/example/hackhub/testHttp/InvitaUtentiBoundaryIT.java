package com.example.hackhub.testHttp;

import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InvitaUtentiBoundaryIT {


    private static final String ENDPOINT = "/api/team/mio/invito";
    private static final String LEADER = "leader_user";
    private static final String MEMBER = "member_user";
    private static final String TARGET = "target_user";


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RepositoryUtenti repositoryUtenti;


    @Autowired
    private RepositoryTeam repositoryTeam;


    @Autowired
    private RepositoryMembriTeam repositoryMembriTeam;


    @MockitoSpyBean
    private ServizioNotifiche servizioNotifiche;


    @BeforeEach
    void setUp(){
        repositoryMembriTeam.deleteAllInBatch();
        repositoryTeam.deleteAllInBatch();
        repositoryUtenti.deleteAllInBatch();


        repositoryMembriTeam.flush();
        repositoryTeam.flush();
        repositoryUtenti.flush();


        repositoryUtenti.saveAndFlush(creaUtente(LEADER));
        repositoryUtenti.saveAndFlush(creaUtente(MEMBER));
        repositoryUtenti.saveAndFlush(creaUtente(TARGET));


        clearInvocations(servizioNotifiche);
    }


    @Test
    void invitaUtenti_ok() throws Exception {
        Team team = new Team("TeamA");
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(ENDPOINT)
                        .with(authentication(autenticazione(LEADER)))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomeUtenteDaInvitare", TARGET))
                .andExpect(status().isNoContent());


        // verify notification created
        verify(servizioNotifiche, times(1)).creaInvitoTeam(eq(LEADER), any(), eq(team));
    }


    @Test
    void invitaUtenti_nonLeader_conflict() throws Exception {
        Team team = new Team("TeamA");
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(ENDPOINT)
                        .with(authentication(autenticazione(MEMBER)))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomeUtenteDaInvitare", TARGET))
                .andExpect(status().isConflict());
    }


    @Test
    void invitaUtenti_targetNotFound_notFound() throws Exception {
        Team team = new Team("TeamA");
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(ENDPOINT)
                        .with(authentication(autenticazione(LEADER)))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomeUtenteDaInvitare", "non_esiste"))
                .andExpect(status().isNotFound());
    }


    @Test
    void invitaUtenti_targetAlreadyMember_conflict() throws Exception {
        Team team = new Team("TeamA");
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        // try inviting MEMBER who is already in a team
        mockMvc.perform(post(ENDPOINT)
                        .with(authentication(autenticazione(LEADER)))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomeUtenteDaInvitare", MEMBER))
                .andExpect(status().isConflict());
    }


    private UsernamePasswordAuthenticationToken autenticazione(String nomeUtente) {
        return new UsernamePasswordAuthenticationToken(
                nomeUtente,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


    private Utente creaUtente(String nomeUtente){
        return new Utente(nomeUtente, nomeUtente + "@example.com", "pwd");
    }


    private Utente utente(String nomeUtente){
        return repositoryUtenti.findByNomeUtente(nomeUtente).orElseThrow(() -> new AssertionError("Utente non trovato: " + nomeUtente));
    }
}

