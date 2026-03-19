package com.example.hackhub.testHttp;

import com.example.hackhub.boundary.dto.*;
import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.handler.VisualizzaHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO classe test da sistemare, non riesco a farla funzionare porco cane
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VisualizzaBoundaryIT {

    private static final String BASE_URL = "/api/visualizzaListe";

    private static final String UTENTE = "francesca";
    private static final String HACKATHON_ID = "hack1";

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private VisualizzaHandler handler;

    @Test
    void viewTeam_ok() throws Exception {

        List<TeamDTO> risposta = List.of();
        when(handler.viewTeam(UTENTE, HACKATHON_ID)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/team")
                        .param("idHackathon", HACKATHON_ID)
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("team1"))
                .andExpect(jsonPath("$[1].id").value("team2"));
    }

    @Test
    void viewValutazioni_ok() throws Exception {

        List<ValutazioneRequest> risposta = List.of();

        when(handler.viewValutazioni(UTENTE, HACKATHON_ID)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/valutazioni")
                        .param("idHackathon", HACKATHON_ID)
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamId").value("team1"))
                .andExpect(jsonPath("$[0].punteggio").value(8))
                .andExpect(jsonPath("$[1].teamId").value("team2"))
                .andExpect(jsonPath("$[1].punteggio").value(9));
    }

    @Test
    void viewSottomissioni_ok() throws Exception {

        List<SottomissioneDTO> risposta = List.of();

        when(handler.viewSottomissioni(UTENTE, HACKATHON_ID)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/sottomissioni")
                        .param("idHackathon", HACKATHON_ID)
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("sub1"))
                .andExpect(jsonPath("$[1].id").value("sub2"));
    }

    @Test
    void viewIscrizioni_ok() throws Exception {

        List<IscrizioneTeamDTO> risposta = List.of();

        when(handler.viewIscrizioni(UTENTE, HACKATHON_ID)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/iscrizioni")
                        .param("idHackathon", HACKATHON_ID)
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamId").value("team1"))
                .andExpect(jsonPath("$[1].teamId").value("team2"));
    }

    @Test
    void viewRichieste_ok() throws Exception {

        List<RichiestaDTO> risposta = List.of(
                new RichiestaDTO("req1", "nulla"),
                new RichiestaDTO("req2", "niente")
        );

        when(handler.viewRichieste(UTENTE)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/richieste")
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("req1"))
                .andExpect(jsonPath("$[1].id").value("req2"));
    }

    @Test
    void viewNotifiche_ok() throws Exception {

        List<NotificaDTO> risposta = List.of(
                new NotificaDTO("not1"),
                new NotificaDTO("not2")
        );

        when(handler.viewNotifiche(UTENTE)).thenReturn(risposta);

        mockMvc.perform(get(BASE_URL + "/notifiche")
                        .with(authentication(auth(UTENTE))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("not1"))
                .andExpect(jsonPath("$[1].id").value("not2"));
    }

    // =========================
    // AUTH helper
    // =========================

    private UsernamePasswordAuthenticationToken auth(String username) {
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
