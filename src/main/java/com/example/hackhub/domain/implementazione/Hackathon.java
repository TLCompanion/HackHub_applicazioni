package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.domain.implementazione.statePattern.StatoHackathon;
import com.example.hackhub.eccezioni.ConflictException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce un'hackathon e tutti i suoi elementi
 */
@Entity
@Table(name = "hackathon", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Hackathon {

    @Id
    private String idHackathon;

    @NotBlank
    private String nome;

    //Valid serve per fare le valutazioni con le annotazioni di validazione presenti nella classe Periodo,
    // ad esempio per assicurarsi che la data di inizio sia precedente a quella di fine.
    @Embedded
    @Valid
    @NotNull
    private Periodo periodo;

    @NotNull
    private BigDecimal premio;

    @NotBlank
    private String luogo;

    @Max(6)
    private int teamMax;

    @Min(3)
    private int teamMin;

    @Lob
    @NotBlank
    private String regolamento;

    @Min(1)
    private int maxIscrizioni; //Massimo numero di iscrizioni (team) che possono partecipare all'hackathon

    @Transient
    private StatoHackathon stato;

    @NotNull
    private LocalDateTime scadenzaIscrizioni;

    @Transient
    private List<Staff> staff;

    //private List<String>
    @Transient
    private List<IscrizioneTeam> iscrizioni;

    public Hackathon() {
        this.staff = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
    }

    /**
     * Creazione di un hackathon con tutti i suoi elementi, con valori di default per scadenza iscrizioni e stato iniziale
     *
     * @param nome               nome dell'hackathon, deve essere univoco
     * @param periodo            periodo di svolgimento dell'hackathon
     * @param premio             premio in denaro per il team vincitore, deve essere positivo
     * @param luogo              luogo in cui si svolge l'hackathon
     * @param teamMax            numero massimo di team che possono partecipare all'hackathon, deve essere positivo
     * @param teamMin            numero minimo di team che devono partecipare all'hackathon, deve essere positivo e minore o uguale a teamMax
     * @param regolamento        il regolamento associato all'hackathon
     * @param scadenzaIscrizioni data e ora di scadenza per le iscrizioni all'hackathon, deve essere una data valida e futura
     */
    public Hackathon(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin,
                     LocalDateTime scadenzaIscrizioni, String regolamento, int maxIscrizioni) {
        validazione(nome, periodo, premio, luogo, teamMax, teamMin, regolamento, scadenzaIscrizioni);
        this.nome = nome;
        this.periodo = periodo;
        this.premio = premio;
        this.luogo = luogo;
        this.teamMax = teamMax;
        this.teamMin = teamMin;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.maxIscrizioni = maxIscrizioni;
        // valori di default / inizializzazioni
        this.stato = IscrizioniAperte.INSTANCE; // stato iniziale, ad esempio "Iscrizioni Aperte"
        this.staff = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.

    /**
     * Assegna un id univoco ad un hackathon
     */
    @PrePersist
    private void assegnaId() {
        if (this.idHackathon == null) {
            this.idHackathon = "H-" + UUID.randomUUID();
        }
    }

    // Metodo che lancia eccezioni se ci sono incongruenze nei campi passati alla creazione, altrimenti non
    // fa niente

    /**
     * Lancia eccezioni se ci sono dei parametri sbagliati dell'hackathon
     * @param nome il nome
     * @param periodo il periodo di svolgimento
     * @param premio il premio
     * @param luogo il luogo dove si svolge
     * @param teamMax il numero massimo dei membri che un team deve avere per iscriversi
     * @param teamMin il numero minimo di membri che un team deve avere per iscriversi
     * @param regolamento il regolamente
     * @param scadenzaIscrizioni la scadenza delle iscrizioni
     * @throws IllegalArgumentException se alcuni dati non sono validi
     * @throws NullPointerException se alcuni dati non sono stati inseriti
     */
    private void validazione(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin, String regolamento, LocalDateTime scadenzaIscrizioni) throws IllegalArgumentException, NullPointerException {

        if (nome == null || periodo == null || premio == null || luogo == null || regolamento == null || scadenzaIscrizioni == null)
            throw new NullPointerException("Non sono ammessi valori nulli");

        if (nome.length() < 3) throw new IllegalArgumentException("Il nome deve avere almeno 3 caratteri di lunghezza");

        if (premio.longValue() <= 0) throw new IllegalArgumentException("Il premio deve avere valore positivo");

        if (luogo.length() < 3) throw new IllegalArgumentException("Il luogo deve avere almeno 3 caratteri");

        if (teamMin < 3) throw new IllegalArgumentException("Il numero minimo di membri per team deve essere almeno 3");
        if (teamMax < teamMin) throw new IllegalArgumentException("Il numero massimo di membri deve essere almeno il numero minimo");

        if (scadenzaIscrizioni.isEqual(LocalDateTime.now()) || scadenzaIscrizioni.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Data oppure orario inseriti non validi");
    }

    /**
     * Aggiunge un iscrizione se i parametri di iscrizione sono validi
     * @param iscrizione l'iscrizione
     */
    public void aggiungiIscrizione(IscrizioneTeam iscrizione) {
        if (iscrizioni.size() == maxIscrizioni) {
            throw new ConflictException("Numero massimo di iscrizioni raggiunto");
        }
        if (stato != IscrizioniAperte.INSTANCE) {
            throw new ConflictException("Non è possibile iscrivere un team, le iscrizioni non sono aperte");
        }
        this.iscrizioni.add(iscrizione);
    }

    //metodi getter

    public void setStato(StatoHackathon stato) {
        this.stato = stato;
    }

    public void conludiHackathon() {
        this.stato.concludiHackathon(this);
    }

    public void avviaHackathon(){
        this.stato.avviaHackathon(this);
    }

    public int getTeamMax() {
        return teamMax;
    }

    public int getTeamMin() {
        return teamMin;
    }

    public String getInfo() {
        return this.regolamento;
    }

    public StatoHackathon getStato() {
        return this.stato;
    }

    public String getIdHackathon() {
        return this.idHackathon;
    }

    public String getNome() {
        return this.nome;
    }

    // mi serve per ottenerlo nell'handler delle valutazioni per verificare che il giudice è un giudice di quello specifico hackathon e non un giudice di un altro hacakthon
    public List<Staff> getStaff() {
        return this.staff;
    }

    //mi serve per l'handler CreaHackathonHandler
    public void aggiungiStaff(Staff staff) {
        this.staff.add(staff);
    }

    public Periodo getPeriodo() {
        return this.periodo;
    }

    public List<IscrizioneTeam> getIscrizioni() {
        return this.iscrizioni;
    }

    public BigDecimal getPremio() {
        return this.premio;
    }

    public String getLuogo() {
        return this.luogo;
    }

    public String getRegolamento() {
        return this.regolamento;
    }

    public LocalDateTime getScadenzaIscrizioni() {
        return this.scadenzaIscrizioni;
    }

    public int getMaxIscrizioni() {
        return this.maxIscrizioni;
    }
}
