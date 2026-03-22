package com.example.hackhub.testHttp;


import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryTeam;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GestisciTeamBoundaryIT extends BaseHttpIT {


    private static final String ENDPOINT = "/api/team";
    private static final String LEADER = "leader_user";
    private static final String MEMBER = "member_user";
    private static final String OTHER = "other_user";
    private static final String TEAM_NAME = "TeamTest";


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RepositoryTeam repositoryTeam;


    @Autowired
    private RepositoryMembriTeam repositoryMembriTeam;


    @Autowired
    private RepositoryUtenti repositoryUtenti;


    @Autowired
    private RepositoryIscrizioniTeam repositoryIscrizioniTeam;


    @MockitoSpyBean
    private ServizioNotifiche servizioNotifiche;


    @BeforeEach
    void setUp(){
        repositoryIscrizioniTeam.deleteAllInBatch();
        repositoryMembriTeam.deleteAllInBatch();
        repositoryTeam.deleteAllInBatch();
        repositoryUtenti.deleteAllInBatch();


        repositoryIscrizioniTeam.flush();
        repositoryMembriTeam.flush();
        repositoryTeam.flush();
        repositoryUtenti.flush();


        repositoryUtenti.saveAndFlush(creaUtente(LEADER));
        repositoryUtenti.saveAndFlush(creaUtente(MEMBER));
        repositoryUtenti.saveAndFlush(creaUtente(OTHER));
    }


    @Test
    void cambiaNome_ok() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        String nuovoNome = "NewName";


        mockMvc.perform(patch(ENDPOINT)
                        .with(authentication(autenticazione(LEADER)))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(nuovoNome))
                .andExpect(status().isOk());


        Team persisted = repositoryTeam.findByNome(nuovoNome).orElseThrow(() -> new AssertionError("Team non aggiornato"));
        assertEquals(nuovoNome, persisted.getNome());
        // notifiche inviate ai membri (leader non escluso)
        verify(servizioNotifiche, atLeastOnce()).creaNotifica(any(), eq(TipoNotifica.CAMBIO_NOME_TEAM), any());
    }


    @Test
    void cambiaNome_nonLeader_conflict() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(patch(ENDPOINT)
                        .with(authentication(autenticazione(MEMBER)))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Whatever"))
                .andExpect(status().isConflict());
    }


    @Test
    void esciDalTeam_ok() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(delete(ENDPOINT + "/membri/me")
                        .with(authentication(autenticazione(MEMBER))))
                .andExpect(status().isOk());


        assertFalse(repositoryMembriTeam.existsByUtente(utente(MEMBER)));
        verify(servizioNotifiche, atLeastOnce()).creaNotifica(any(), eq(TipoNotifica.USCITA), any());
    }


    @Test
    void esciDalTeam_leaderWithoutNewLeader_conflict() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        // leader tries to exit but team has more than 1 member -> conflict
        mockMvc.perform(delete(ENDPOINT + "/membri/me")
                        .with(authentication(autenticazione(LEADER))))
                .andExpect(status().isConflict());
    }


    @Test
    void sciogliTeam_ok() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(delete(ENDPOINT + "/mio")
                        .with(authentication(autenticazione(LEADER))))
                .andExpect(status().isOk());


        assertFalse(repositoryTeam.findByNome(TEAM_NAME).isPresent());
        verify(servizioNotifiche, atLeastOnce()).creaNotifica(any(), eq(TipoNotifica.SCIOGLIMENTO_TEAM), any());
    }


    @Test
    void sciogliTeam_notLeader_conflict() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(delete(ENDPOINT + "/mio")
                        .with(authentication(autenticazione(MEMBER))))
                .andExpect(status().isConflict());
    }


    @Test
    void espelliMembro_ok() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(delete(ENDPOINT + "/membri/{nomeMembro}", MEMBER)
                        .with(authentication(autenticazione(LEADER))))
                .andExpect(status().isOk());


        assertFalse(repositoryMembriTeam.existsByUtente(utente(MEMBER)));
        verify(servizioNotifiche, atLeastOnce()).creaNotifica(any(), eq(TipoNotifica.ESPULSIONE_TEAM), any());
    }


    @Test
    void espelliMembro_notLeader_conflict() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(delete(ENDPOINT + "/membri/{nomeMembro}", MEMBER)
                        .with(authentication(autenticazione(MEMBER))))
                .andExpect(status().isConflict());
    }


    @Test
    void trasferisceRuoloLeader_ok() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        MembroTeam member = new MembroTeam(utente(MEMBER), team, RuoloTeam.MEMBRO);
        team.aggiungiMembro(member);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(ENDPOINT + "/leader")
                        .with(authentication(autenticazione(LEADER)))
                        .param("nomeMembro", MEMBER))
                .andExpect(status().isOk());


        // creaPropostaLeader should be invoked
        verify(servizioNotifiche, atLeastOnce()).creaPropostaLeader(eq(LEADER), any(), any());
    }


    @Test
    void trasferisceRuoloLeader_memberNotFound_notFound() throws Exception {
        Team team = new Team(TEAM_NAME);
        MembroTeam leader = new MembroTeam(utente(LEADER), team, RuoloTeam.LEADER);
        team.setLeader(leader);
        repositoryTeam.saveAndFlush(team);


        mockMvc.perform(post(ENDPOINT + "/leader")
                        .with(authentication(autenticazione(LEADER)))
                        .param("nomeMembro", "non_esiste"))
                .andExpect(status().isNotFound());
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
