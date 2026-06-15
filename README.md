# HackHub

HackHub è una piattaforma full-stack pensata per la gestione e la partecipazione di team ad Hackathon. 

Le funzionalità principali sono divise in backend e frontend. 

Il backend(precedentemente sviluppato) contiene le seguenti funznionalità

- Gestione Team: creazione, invito utenti, accettazione o rifiuto di inviti, visualizzazione e partecipazione ad hackathon

- Autenticazione: gestita tramite JWT (Bearer Token). Necessaria per tutte le funzionalità tranne la visualizzazione di informazioni pubbliche sugli hackathon.

> Gli organizzatori non possono creare Team e i leader dei team non possono creare hackathon.

- Gestione Staff: mentori e giudici possono essere attivamente cambiati dall'organizzatore. I giudici valutano le sottomissioni e i mentori gestiscono la necessità di assistenza dei team.

Il frontend implementa solo alcune di queste funzionalità, nel particolare:

- Registrazione e login: è possibile registrarsi nella piattaforma ed accedere con tali credenziali.

>  Per registrarsi con successo devono essere obbligatoriamente compilati tutti i campi, il nome può essere scelto liberamente (sono già presenti nel database i nomi: giada, utente1 fino ad utente10), l'email deve essere in formato testo@testo, la password deve contenere almeno 6 caratteri

- Visualizzazione di hackathon e team: la visualizzazione della pagina è lato organizzatore. I team sono già stati creati e possono solo essere visualizzati

- Creazione di hackathon

>  Per creare hackathon con successo devono essere obbligatoriamente creati tutti i campi. Da tenere in considerazione:
> - non possono essere creati due hackathon con lo stesso nome
> - la data di fine non può essere precedente o uguale a quella di inizio
> - la data di scadenza delle iscrizioni deve essere precedente o uguale a quella di inizio
> - il numero minimo e massimo di persone per team deve essere compreso tra 3 e 6
> - Sono già presenti nel database giudice e mentore con tale nome.

## Architettura e tecnologie usate
Il progetto implementa un'architettura containerizzata basata su servizi indipendenti ed è pensata per facilitare lo sviluppo locale, il deploy e la manutenzione. 

### Frontend
- React: Framework frontend usato per sviluppare l'interfaccia come Single Page Application.

- Vite: come strumento di build per il frontend React
  
- Nginx: usato per gestire i file generati dalla build React

### Backend
Il backend segue un modello MVC che divide la logica di implementazione delle entità da quelle dei servizi. Le boundary gestiscono le REST API, i servizi implementano la logica di business tramite l'accesso ai dati fornito dalle Repository e le entity generano la base del Database.
Le principali tecnologie usate sono:

- Java 21: linguaggio usato per sviluppare il backend grazie alla facile integrazione con Spring
  
- Spring Boot: usato per l'esposizione delle API REST, per semplificare e velocizzare lo sviluppo grazie alle numerose funzionalità presenti.
  
- Spring Security: usato per gestire l'autenticazione degli utenti
  
- JWT Authentication: usato per realizzare un sistema di autenticazione stateless che genera un token identificando l'utente.
  
- Spring Data JPA: usato per l'accesso ai dati e comunicazione con il database, diminuendo la quantità di query tramite le operazioni CRUD di sistema

### Database:
- MySQL: database relazionare usato per la persistenza dei vari dati

## AUTENTIICAZIONE STATELESS
Il programma utilizza un sistema di autenticazione basato su JSON Web Token. Dopo ogni login il server genera un tokem JWT che contiene le informazioni dell'utente che viene restituito e va inviato in ogni richiesta HTTP. Questo paradigma è stateless perchè il backend non mantiene attiva nessuna sessione in lato server perchè il token contiene già da sè tutte le informazioni necessarie per autenticare l'utente.

## CONTAINERIZZAZIONE
L'intera applicazione è divisa in tre container: frontend, backend, databse per garantirne la portabilità anche in fase di produzione, ambiente ospitato su Microsoft Azure.
Le strategie adottate sono:

- immagini dedicate per ogni servizio: Node.js per la compilazione del frontend, Nginx per la distribuzione dei file statici frontend, java jdk per l'esecuzione del backend e mysql per la persistenza dei dati
  
- Multi-stage build per il frontend: il Dockerfile del frontend utilizza sia una build stage iniziale che una runtime stage tramite nginx.
  
- orchestrazione tramite docker compose che gestisce l'avvio e la comunicazione tra i container

## PIPELINE CI/CD 
La pipeline automatizza tramite GitHub l'esecuzione dei test. Ad ogni commit vengono:
- avvia come lavoro quello di eseguire i test
- avvia il container docker con il database 
- configura il database secondo le variabili necessarie
- installa le tecnologie necessarie
- esegue i test

# SPECIFICHE DELLA MACCHINA VIRTUALE
L'applicazione è ospitata su una Virtual Machine Microsoft Azure che gestisce l'intero programma tramite Docker.

### Specifiche
Provider Cloud: Microsoft Azure
Sistema Operativo: Ubuntu Server 24.04 LTS
Accessi tramite: Porta 22 (SSH) per l'amministrazione remota della macchina, Porta 80 (HTTP) per rendere l'applicazione accessibile tramite browser

# Build from scratch
## Prerequisiti
Installare i seguenti strumenti:
- Git
- Docker
- Docker Compose

## Clonazione del repository
Clonare il repository: 

git clone https://github.com/TLCompanion/HackHub_applicazioni.git
cd HackHub_applicazioni

## Configurazione delle Variabili d'Ambiente
Creare un file .env nella directory principale del progetto.
Incollare il seguente testo:

MYSQL_DATABASE=hackhub
MYSQL_USER=hackhub
MYSQL_PASSWORD=1234abc
MYSQL_ROOT_PASSWORD=1234abc
MYSQL_PORT=3307

DB_HOST=mysql
DB_PORT=3306
DB_NAME=hackhub
DB_USERNAME=hackhub
DB_PASSWORD=1234abc
SERVER_PORT=8081

APP_JWT_SECRET=hackhub_local_dev_secret_change_me_please_12345
APP_JWT_EXPIRATION_MS=3600000

## Inizializzazione del Database
Il progetto include un file `data.sql` contenente dati iniziali utilizzati per la dimostrazione dell'applicazione
Durante il primo avvio il database viene popolato automaticamente con:

- utenti
- team
- hackathon

## Avvio
Per avviare tutti i servizi:

docker compose up --build

Per verificare che i servizi siano attivi:

docker ps

Per fermare l'applicazione:

docker compose down

# Deploy
L'applicazione è stata deployata su una macchina virtuale Ubuntu lts su Azure.

Link: http://158.158.10.19 

# Diagrammi




##  REST API Reference Backend
 ### 1) Autenticazione
- `POST /api/autenticazione/registrazione` - registra utente
- `POST /api/autenticazione/accesso` - login e JWT

 `RegisterRequest`:
- `nomeUtente` string
- `email` email
- `password` string (min 6)
- 
 ###  2) Hackathon - creazione/gestione
- `POST /api/hackathon` - crea hackathon (organizzatore autenticato)
- `POST /api/hackathon/{nomeHackathon}/iscrizioni` - iscrive il team del leader
- `DELETE /api/hackathon/{nomeHackathon}/iscrizioni/mia` - annulla iscrizione del proprio team
- `POST /api/hackathon/{nomeHackathon}/violazione?nomeTeam=...` - mentore segnala violazione
- `POST /api/hackathon/{nomeHackathon}/nomine-mentori?nomeUtenteDaInvitare=...` - organizzatore invita mentore
- `DELETE /api/hackathon/{nomeHackathon}` - elimina hackathon
- `POST /api/hackathon/{nomeHackathon}/team/{nomeTeam}/espulsione` - espelle team
- `POST /api/hackathon/{nomeHackathon}/vincitore?nomeTeam=...` - proclama vincitore
- `POST /api/hackathon/{nomeHackathon}/liquidazione-premio?nomeTeam=...` - liquida premio

 `HackathonRequest` (POST `/api/hackathon`):
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

###  3) Team
- `POST /api/team` - crea team (`TEXT`: nome team)
- `PATCH /api/team` - cambia nome team (`TEXT`: nuovo nome)
- `DELETE /api/team/membri/me` - esci dal team
- `DELETE /api/team/mio` - sciogli team
- `DELETE /api/team/membri/{nomeMembro}` - espelli membro
- `POST /api/team/leader?nomeMembro=...` - trasferimento ruolo leader immediato (senza richiesta)
- `POST /api/team/mio/invito?nomeUtenteDaInvitare=...` - invita utente nel team

### 4) Sottomissioni e valutazioni
- `POST /api/sottomissioni/{nomeHackathon}` - invia sottomissione (`TEXT`: link)
- `DELETE /api/sottomissioni/{nomeHackathon}` - rimuove sottomissione
- `POST /api/sottomissioni/{idSottomissione}/valutazione` - inserisce/aggiorna valutazione

`ValutazioneRequest`:
- `giudizio` string non vuota
- `punteggio` int [0..10]

 ### 5) Call e assistenza
- `POST /api/call/proposta` - mentore propone call
- `POST /api/assistenza/richiesta?nomeMentore=...&nomeHackathon=...` - leader richiede assistenza
- `POST /api/richieste-supporto/risposta?idNotifica=...` - mentore risponde a richiesta supporto

`PropostaCallRequest`:
- `idHackathon` string
- `idTeam` string
- `data` date (`yyyy-MM-dd`)
- `ora` time (`HH:mm:ss`)
  
 ### 6) Richieste
- `POST /api/richieste/{idRichiesta}/accetta` - accetta richiesta
- `POST /api/richieste/{idRichiesta}/rifiuta` - rifiuta richiesta

Le richieste gestite in questo blocco sono inviti team/staff e proposte call.
Il cambio leader del team avviene direttamente tramite `POST /api/team/leader`.

 ### 7) Visualizzazione
- `GET /api/hackathon/{nomeHackathon}/valutazioni`
- `GET /api/hackathon/{nomeHackathon}/sottomissioni`
- `GET /api/hackathon/{nomeHackathon}/iscrizioni`
- `GET /api/richieste`
- `GET /api/notifiche`
- `GET /api/hackathon` (pubblico, non richiede JWT)
- `GET /api/team`

## Credenziali di accesso
- username: demo
- password: 123456

##Autori(frontend):
- Giada Branchesi
  
##Autori(backend):
- Letizia Pistola
- Giada Branchesi
- Jhonatan Silenzi

