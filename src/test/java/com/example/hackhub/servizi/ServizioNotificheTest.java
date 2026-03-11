package com.example.hackhub.servizi;

class ServizioNotificheTest {
/*
    private RepositoryNotifica mockRepoNotifica;
    private RepositoryRichiesta mockRepoRichiesta;
    private ServizioNotifiche servizio;

    @BeforeEach
    void setUp() {
        mockRepoNotifica = mock(RepositoryNotifica.class);
        mockRepoRichiesta = mock(RepositoryRichiesta.class);
        servizio = new ServizioNotifiche(mockRepoNotifica, mockRepoRichiesta);
    }

    @Test
    void creaNotifica() {
        Utente u1 = new Utente("user1", "mario@gmail.com", "huvsonvsui5");
        Utente u2 = new Utente("user2", "luigi@gmail.com", "vndla7o0");

        servizio.creaNotifica(List.of(u1, u2), TipoNotifica.VALUTAZIONE_CONCLUSA, "Messaggio di prova");

        // Verifica che save sia chiamato due volte
        verify(mockRepoNotifica, times(2)).save(any(Notifica.class));

        // Controlliamo che il contenuto della notifica sia corretto
        ArgumentCaptor<Notifica> captor = ArgumentCaptor.forClass(Notifica.class);
        verify(mockRepoNotifica, times(2)).save(captor.capture());
        List<Notifica> notificheSalvate = captor.getAllValues();

        assertEquals("Messaggio di prova", notificheSalvate.get(0).getPayload());
        assertEquals(u1, notificheSalvate.get(0).getDestinatario());
        assertEquals(TipoNotifica.VALUTAZIONE_CONCLUSA, notificheSalvate.get(0).getTipo());

        assertEquals("Messaggio di prova", notificheSalvate.get(1).getPayload());
        assertEquals(u2, notificheSalvate.get(1).getDestinatario());
        assertEquals(TipoNotifica.VALUTAZIONE_CONCLUSA, notificheSalvate.get(1).getTipo());
    }

    @Test
    void creaRichiesta() {
        Utente u1 = new Utente("user1", "mario@gmail.com", "huvsonvsui5");
        Utente u2 = new Utente("user2", "luigi@gmail.com", "vndla7o0");

        Periodo periodo = new Periodo(LocalDate.now().plusDays(1), LocalTime.of(10, 0),
                LocalDate.now().plusDays(2), LocalTime.of(18, 0));

        servizio.creaRichiesta("Mentore1", List.of(u1, u2), TipoRichiesta.PROPOSTA_CALL,
                "Richiesta di partecipazione", periodo);

        // Verifica che save sia chiamato due volte
        verify(mockRepoRichiesta, times(2)).save(any(Richiesta.class));

        // Controlliamo che il contenuto della richiesta sia corretto
        ArgumentCaptor<Richiesta> captor = ArgumentCaptor.forClass(Richiesta.class);
        verify(mockRepoRichiesta, times(2)).save(captor.capture());
        List<Richiesta> richiesteSalvate = captor.getAllValues();

        assertEquals("Richiesta di partecipazione", richiesteSalvate.get(0).getPayload());
        assertEquals(u1, richiesteSalvate.get(0).getDestinatario());
        assertEquals("Mentore1", richiesteSalvate.get(0).getMittente());
        assertEquals(TipoRichiesta.PROPOSTA_CALL, richiesteSalvate.get(0).getTipo());
        assertEquals(periodo, richiesteSalvate.get(0).getPeriodo());

        assertEquals("Richiesta di partecipazione", richiesteSalvate.get(1).getPayload());
        assertEquals(u2, richiesteSalvate.get(1).getDestinatario());
        assertEquals("Mentore1", richiesteSalvate.get(1).getMittente());
        assertEquals(TipoRichiesta.PROPOSTA_CALL, richiesteSalvate.get(1).getTipo());
        assertEquals(periodo, richiesteSalvate.get(1).getPeriodo());
    }*/
}