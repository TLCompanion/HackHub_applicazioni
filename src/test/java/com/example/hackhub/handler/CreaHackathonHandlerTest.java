package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.HackathonRequest;
import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.eccezioni.ForbiddenException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.servizi.ServizioNotifiche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.hackhub.domain.implementazione.*;
import com.example.hackhub.repository.*;

class CreaHackathonHandlerTest {

    private RepositoryUtenti repoUtenti;
    private RepositoryHackathon repoHackathon;
    private RepositoryStaff repoStaff;
    private ServizioNotifiche servizioNotifiche;
    private CreaHackathonHandler handler;

    @BeforeEach
    void setUp() {
        repoUtenti = mock(RepositoryUtenti.class);
        repoHackathon = mock(RepositoryHackathon.class);
        repoStaff = mock(RepositoryStaff.class);
        servizioNotifiche = mock(ServizioNotifiche.class);

        //handler = new CreaHackathonHandler(repoUtenti, repoHackathon, repoStaff, servizioNotifiche);
    }

    private HackathonRequest requestValida() {
        return new HackathonRequest(
                "HackFest",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                "Milano",
                BigDecimal.valueOf(1000),
                3,
                6,
                5,
                "Regolamento",
                LocalDateTime.now().plusDays(1),
                "Anna",
                List.of("Luigi")
        );
    }

    private void invocaMetodoPrivato(Object obj, String nomeMetodo, Class<?>[] parametri, Object... args) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(nomeMetodo, parametri);
        m.setAccessible(true);
        m.invoke(obj, args);
    }

    @Test
    void avviaCreazioneHackathon() {

        HackathonRequest request = requestValida();

        Utente organizzatore = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");
        Utente giudice = new Utente("Anna", "anna@gmail.com", "walevns08");

        when(repoHackathon.existsByNome("HackFest")).thenReturn(false);
        when(repoUtenti.findById("U1")).thenReturn(Optional.of(organizzatore));
        when(repoUtenti.findByNomeUtente("Luigi")).thenReturn(Optional.of(mentore));
        when(repoUtenti.findByNomeUtente("Anna")).thenReturn(Optional.of(giudice));

        handler.avviaCreazioneHackathon(request, "U1");

        verify(repoHackathon).save(any(Hackathon.class));
        verify(repoStaff).save(any(Staff.class));
    }

    @Test
    void avviaCreazioneHackathon_nomeGiaEsistente() {

        HackathonRequest request = requestValida();

        when(repoHackathon.existsByNome("HackFest")).thenReturn(true);

        assertThrows(ForbiddenException.class,
                () -> handler.avviaCreazioneHackathon(request, "U1"));
    }

    @Test
    void gestisciOrganizzatore() throws Exception {

        Utente organizzatore = new Utente("Mario", "mario@gmail.com", "huvsonvsui5");
        Hackathon hackathon = new Hackathon();

        when(repoUtenti.findById("U1")).thenReturn(Optional.of(organizzatore));

        invocaMetodoPrivato(handler,
                "gestisciOrganizzatore",
                new Class[]{String.class, Hackathon.class},
                "U1",
                hackathon);

        assertEquals(1, hackathon.getStaff().size());
        assertEquals(RuoloStaff.ORGANIZZATORE, hackathon.getStaff().get(0).getRuolo());

        verify(repoStaff).save(any(Staff.class));
    }

    @Test
    void gestisciOrganizzatore_utenteNonTrovato() throws Exception {

        Hackathon hackathon = new Hackathon();

        when(repoUtenti.findById("U1")).thenReturn(Optional.empty());

        Method m = CreaHackathonHandler.class
                .getDeclaredMethod("gestisciOrganizzatore", String.class, Hackathon.class);

        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, "U1", hackathon));

        assertTrue(ex.getCause() instanceof NotFoundException);
        assertEquals("L' utente non esiste: U1", ex.getCause().getMessage());
    }


    @Test
    void gestisciStaff() throws Exception {

        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");
        Utente giudice = new Utente("Anna", "anna@gmail.com", "walevns08");

        when(repoUtenti.findByNomeUtente("Luigi")).thenReturn(Optional.of(mentore));
        when(repoUtenti.findByNomeUtente("Anna")).thenReturn(Optional.of(giudice));

        Method m = CreaHackathonHandler.class
                .getDeclaredMethod("gestisciStaff", List.class, String.class);

        m.setAccessible(true);

        Map<Utente, RuoloStaff> risultato =
                (Map<Utente, RuoloStaff>) m.invoke(handler, List.of("Luigi"), "Anna");

        assertEquals(2, risultato.size());
        assertEquals(RuoloStaff.MENTORE, risultato.get(mentore));
        assertEquals(RuoloStaff.GIUDICE, risultato.get(giudice));
    }

    @Test
    void gestisciStaff_mentoreNonTrovato() throws Exception {

        when(repoUtenti.findByNomeUtente("Luigi")).thenReturn(Optional.empty());

        Method m = CreaHackathonHandler.class
                .getDeclaredMethod("gestisciStaff", List.class, String.class);

        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, List.of("Luigi"), "Anna"));

        assertTrue(ex.getCause() instanceof NotFoundException);
        assertEquals("Il mentore specificato non esiste: Luigi", ex.getCause().getMessage());
    }


    @Test
    void gestisciStaff_giudiceNonTrovato() throws Exception {

        Utente mentore = new Utente("Luigi", "luigi@gmail.com", "vndla7o0");

        when(repoUtenti.findByNomeUtente("Luigi")).thenReturn(Optional.of(mentore));
        when(repoUtenti.findByNomeUtente("Anna")).thenReturn(Optional.empty());

        Method m = CreaHackathonHandler.class
                .getDeclaredMethod("gestisciStaff", List.class, String.class);

        m.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> m.invoke(handler, List.of("Luigi"), "Anna"));

        assertTrue(ex.getCause() instanceof NotFoundException);
        assertEquals("Il utente non esiste: Anna", ex.getCause().getMessage());
    }
}


