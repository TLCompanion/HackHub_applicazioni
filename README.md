# HackHub

HackHub è una piattaforma full-stack pensata per la gestione e la partecipazione di team ad Hackathon. 

Le funzionalità principali sono divise in backend e frontend. 

Il backend(precedentemente sviluppato) contiene le seguenti funznionalità

- Gestione Team: creazione, invito utenti, accettazione o rifiuto di inviti, visualizzazione e partecipazione ad hackathon

- Autenticazione: gestita tramite JWT (Bearer Token). Necessaria per tutte le funzionalità tranne la visualizzazione di informazioni pubbliche sugli hackathon.

[!TIPO_NOTA] Gli organizzatori non possono creare Team e i leader dei team non possono creare hackathon.

- Gestione Staff: mentori e giudici possono essere attivamente cambiati dall'organizzatore. I giudici valutano le sottomissioni e i mentori gestiscono la necessità di assistenza dei team.

Il frontend implementa solo alcune di queste funzionalità, nel particolare:

- Registrazione e login: è possibile registrarsi nella piattaforma ed accedere con tali credenziali.

[!TIPO_NOTA] Per registrarsi con successo devono essere obbligatoriamente compilati tutti i campi, il nome può essere scelto liberamente (sono già presenti nel database i nomi: giada, utente1 fino ad utente10), l'email deve essere in formato testo@testo, la password deve contenere almeno 6 caratteri

- Visualizzazione di hackathon e team: la visualizzazione della pagina è lato organizzatore. I team sono già stati creati e possono solo essere visualizzati

- Creazione di hackathon

[!TIPO_NOTA] Per creare hackathon con successo devono essere obbligatoriamente creati tutti i campi. Da tenere in considerazione:
- non possono essere creati due hackathon con lo stesso nome
- la data di fine non può essere precedente o uguale a quella di inizio
- la data di scadenza delle iscrizioni deve essere precedente o uguale a quella di inizio
- il numero minimo e massimo di persone per team deve essere compreso tra 3 e 6
- Sono già presenti nel database giudice e mentore con tale nome.

[!TIPO_NOTA] REST API Reference Backend
> ### 1) Autenticazione
- `POST /api/autenticazione/registrazione` - registra utente
- `POST /api/autenticazione/accesso` - login e JWT

> `RegisterRequest`:
- `nomeUtente` string
- `email` email
- `password` string (min 6)
> ###  2) Hackathon - creazione/gestione
- `POST /api/hackathon` - crea hackathon (organizzatore autenticato)
- `POST /api/hackathon/{nomeHackathon}/iscrizioni` - iscrive il team del leader
- `DELETE /api/hackathon/{nomeHackathon}/iscrizioni/mia` - annulla iscrizione del proprio team
- `POST /api/hackathon/{nomeHackathon}/violazione?nomeTeam=...` - mentore segnala violazione
- `POST /api/hackathon/{nomeHackathon}/nomine-mentori?nomeUtenteDaInvitare=...` - organizzatore invita mentore
- `DELETE /api/hackathon/{nomeHackathon}` - elimina hackathon
- `POST /api/hackathon/{nomeHackathon}/team/{nomeTeam}/espulsione` - espelle team
- `POST /api/hackathon/{nomeHackathon}/vincitore?nomeTeam=...` - proclama vincitore
- `POST /api/hackathon/{nomeHackathon}/liquidazione-premio?nomeTeam=...` - liquida premio

> `HackathonRequest` (POST `/api/hackathon`):
- `nome` string
- `dataInizio` date (`yyyy-MM-dd`)
- `dataFine` date (`yyyy-MM-dd`)
- `luogo` string
- `premio` number
- `teamMin` int [3..6]
- `teamMax` int [3..6]
- `maxIscrizioni` int >= 1
- `regolamento` string
- `scadenzaIscrizioni` datetime (`yyyy-MM-dd'T'HH:mm:ss`)
- `nomeGiudice` string
- `nomeMentori` string[] (min 1)
> ###  3) Team
- `POST /api/team` - crea team (`TEXT`: nome team)
- `PATCH /api/team` - cambia nome team (`TEXT`: nuovo nome)
- `DELETE /api/team/membri/me` - esci dal team
- `DELETE /api/team/mio` - sciogli team
- `DELETE /api/team/membri/{nomeMembro}` - espelli membro
- `POST /api/team/leader?nomeMembro=...` - trasferimento ruolo leader immediato (senza richiesta)
- `POST /api/team/mio/invito?nomeUtenteDaInvitare=...` - invita utente nel team
> ### 4) Sottomissioni e valutazioni
- `POST /api/sottomissioni/{nomeHackathon}` - invia sottomissione (`TEXT`: link)
- `DELETE /api/sottomissioni/{nomeHackathon}` - rimuove sottomissione
- `POST /api/sottomissioni/{idSottomissione}/valutazione` - inserisce/aggiorna valutazione

`ValutazioneRequest`:
- `giudizio` string non vuota
- `punteggio` int [0..10]
> ### 5) Call e assistenza
- `POST /api/call/proposta` - mentore propone call
- `POST /api/assistenza/richiesta?nomeMentore=...&nomeHackathon=...` - leader richiede assistenza
- `POST /api/richieste-supporto/risposta?idNotifica=...` - mentore risponde a richiesta supporto

`PropostaCallRequest`:
- `idHackathon` string
- `idTeam` string
- `data` date (`yyyy-MM-dd`)
- `ora` time (`HH:mm:ss`)
> ### 6) Richieste
- `POST /api/richieste/{idRichiesta}/accetta` - accetta richiesta
- `POST /api/richieste/{idRichiesta}/rifiuta` - rifiuta richiesta

Le richieste gestite in questo blocco sono inviti team/staff e proposte call.
Il cambio leader del team avviene direttamente tramite `POST /api/team/leader`.
> ### 7) Visualizzazione
- `GET /api/hackathon/{nomeHackathon}/valutazioni`
- `GET /api/hackathon/{nomeHackathon}/sottomissioni`
- `GET /api/hackathon/{nomeHackathon}/iscrizioni`
- `GET /api/richieste`
- `GET /api/notifiche`
- `GET /api/hackathon` (pubblico, non richiede JWT)
- `GET /api/team`
