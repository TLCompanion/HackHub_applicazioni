package com.example.hackhub.testHttp;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.RuoloTeam;
import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.IscrizioneTeam;
import com.example.hackhub.domain.implementazione.MembroTeam;
import com.example.hackhub.domain.implementazione.Notifica;
import com.example.hackhub.domain.implementazione.Periodo;
import com.example.hackhub.domain.implementazione.Staff;
import com.example.hackhub.domain.implementazione.Team;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryIscrizioniTeam;
import com.example.hackhub.repository.RepositoryMembriTeam;
import com.example.hackhub.repository.RepositoryNotifica;
import com.example.hackhub.repository.RepositoryStaff;
import com.example.hackhub.repository.RepositoryUtenti;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;




import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GestisceAssistenzaBoundaryIT {




    private static final String BASE_URL = "/api/assistenza/richiesta";
    private static final String NOME_UTENTE = "leaderTeam";




    @Autowired
    private MockMvc mockMvc;




    @Autowired
    private RepositoryUtenti repositoryUtenti;




    @Autowired
    private RepositoryMembriTeam repositoryMembriTeam;




    @Autowired
    private RepositoryIscrizioniTeam repositoryIscrizioniTeam;




    @Autowired
    private RepositoryHackathon repositoryHackathon;




    @Autowired
    private RepositoryStaff repositoryStaff;




    @Autowired
    private RepositoryNotifica repositoryNotifica;




    @Autowired
    private EntityManager entityManager;


    @Autowired
    private com.example.hackhub.repository.RepositoryTeam repositoryTeam;




    @BeforeEach
    void setUp() {
        repositoryNotifica.deleteAll();
        repositoryStaff.deleteAll();
        repositoryIscrizioniTeam.deleteAll();
        repositoryMembriTeam.deleteAll();
        repositoryHackathon.deleteAll();
        repositoryUtenti.deleteAll();
        // use repository delete to keep persistence context consistent instead of executing a bulk JPQL delete
        // which can leave managed entities in an inconsistent state and cause OptimisticLockException on flush
        repositoryTeam.deleteAll();
        repositoryTeam.flush();
    }




    @Test
    void richiediAssistenza_ok() throws Exception {
        Utente leaderUtente = repositoryUtenti.saveAndFlush(new Utente(NOME_UTENTE, "leader@test.com", "password"));
        Utente mentoreUtente = repositoryUtenti.saveAndFlush(new Utente("mentore1", "mentore@test.com", "password"));




        Team team = new Team("TeamAlpha");
        entityManager.persist(team);
        entityManager.flush();




        MembroTeam leader = new MembroTeam(leaderUtente, team, RuoloTeam.LEADER);
        repositoryMembriTeam.saveAndFlush(leader);




        Hackathon hackathonRichiesto = creaHackathon("HackathonUno");
        Hackathon hackathonMentore = creaHackathon("HackathonDue");




        repositoryHackathon.saveAll(List.of(hackathonRichiesto, hackathonMentore));
        repositoryHackathon.flush();




        IscrizioneTeam iscrizioneTeam = new IscrizioneTeam(team, hackathonRichiesto);
        repositoryIscrizioniTeam.saveAndFlush(iscrizioneTeam);




        Staff mentore = new Staff(mentoreUtente, RuoloStaff.MENTORE);
        mentore.setHackathon(hackathonRichiesto);
        mentore = repositoryStaff.saveAndFlush(mentore);




        mockMvc.perform(post(BASE_URL)
                        .with(authentication(auth()))
                        .param("nomeMentore", mentoreUtente.getNomeUtente())
                        .param("nomeHackathon", hackathonRichiesto.getNome()))
                .andExpect(status().isOk());




        List<Notifica> notifiche = repositoryNotifica.findAll();
        assertEquals(1, notifiche.size());
        assertEquals("Richiesta di assistenza", notifiche.getFirst().getPayload());
        assertEquals(TipoNotifica.ASSISTENZA, notifiche.getFirst().getTipoNotifica());
        assertEquals(mentoreUtente.getNomeUtente(), notifiche.getFirst().getDestinatario().getNomeUtente());
    }




    @Test
    void richiediAssistenza_errore() throws Exception {
        Utente leaderUtente = repositoryUtenti.saveAndFlush(new Utente(NOME_UTENTE, "leader@test.com", "password"));
        Utente mentoreUtente = repositoryUtenti.saveAndFlush(new Utente("mentore1", "mentore@test.com", "password"));




        Team team = new Team("TeamAlpha");
        entityManager.persist(team);
        entityManager.flush();




        MembroTeam leader = new MembroTeam(leaderUtente, team, RuoloTeam.LEADER);
        repositoryMembriTeam.saveAndFlush(leader);




        Hackathon hackathon = creaHackathon("HackathonUno");
        repositoryHackathon.saveAndFlush(hackathon);




        IscrizioneTeam iscrizioneTeam = new IscrizioneTeam(team, hackathon);
        repositoryIscrizioniTeam.saveAndFlush(iscrizioneTeam);




        Staff mentore = new Staff(mentoreUtente, RuoloStaff.MENTORE);
        mentore.setHackathon(hackathon);
        repositoryStaff.saveAndFlush(mentore);




        // pass a username that exists but is not a mentor (should result in NotFound)
        mockMvc.perform(post(BASE_URL)
                        .with(authentication(auth()))
                        .param("nomeMentore", leaderUtente.getNomeUtente())
                        .param("nomeHackathon", hackathon.getNome()))
                .andExpect(status().isNotFound());




        assertFalse(repositoryNotifica.findAll().stream()
                .anyMatch(n -> n.getDestinatario().getNomeUtente().equals(mentoreUtente.getNomeUtente())));
    }




    @Test
    void richiediAssistenza_notFound() throws Exception {
        Utente leaderUtente = repositoryUtenti.saveAndFlush(new Utente(NOME_UTENTE, "leader@test.com", "password"));




        Team team = new Team("TeamAlpha");
        entityManager.persist(team);
        entityManager.flush();




        MembroTeam leader = new MembroTeam(leaderUtente, team, RuoloTeam.LEADER);
        repositoryMembriTeam.saveAndFlush(leader);




        Hackathon hackathon = creaHackathon("HackathonUno");
        repositoryHackathon.saveAndFlush(hackathon);




        IscrizioneTeam iscrizioneTeam = new IscrizioneTeam(team, hackathon);
        repositoryIscrizioniTeam.saveAndFlush(iscrizioneTeam);




        mockMvc.perform(post(BASE_URL)
                        .with(authentication(auth()))
                        .param("nomeMentore", "mentore_inesistente")
                        .param("nomeHackathon", hackathon.getNome()))
                .andExpect(status().isNotFound());




        assertEquals(0, repositoryNotifica.findAll().size());
    }




    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(
                NOME_UTENTE,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }




    private Hackathon creaHackathon(String nome) {
        return new Hackathon(
                nome,
                new Periodo(
                        LocalDate.now().plusDays(10),
                        LocalTime.of(10, 0),
                        LocalDate.now().plusDays(12),
                        LocalTime.of(18, 0)
                ),
                BigDecimal.valueOf(1000),
                "Roma",
                6,
                3,
                LocalDateTime.now().plusDays(5),
                "Regolamento test",
                10
        );
    }
}


