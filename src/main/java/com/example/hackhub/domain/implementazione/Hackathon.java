package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.*;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// TODO persistenza sospesa

/**
 * Classe che gestisce un'hackathon e tutti i suoi elementi
 */
@Entity
@Table(name = "hackathon", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Hackathon implements Publisher {

    @Id
    private String idHackathon;

    @NotBlank
    private String nome;

    //Valid serve per fare le valutazioni con le annotazioni di validazione presenti nella classe Periodo,
    // ad esempio per assicurarsi che la data di inizio sia precedente a quella di fine.
    @Embedded @Valid @NotNull
    private Periodo periodo;

    @NotNull
    private BigDecimal premio;

    @Transient
    private String luogo;

    @Max(6)
    private int teamMax;

    @Min(3)
    private int teamMin;

    @Lob @NotBlank
    private String regolamento;

    @Min(1)
    private int maxIscrizioni;

    //TODO valutare se transient è giusto in questo caso, chat dice: "Se vuoi davvero State pattern con classi: allora
    // in persistenza memorizzi almeno un “codice stato” e ricostruisci l’oggetto stato a runtime (factory dello stato)."
    @Transient
    private StatoHackathon stato;

    private LocalDateTime scadenzaIscrizioni;

    @Transient
    private List<Subscriber> subscriber;

    //TODO valutare se transient è giusto in questo caso
    @Transient
    private List<Staff> staff;

    //private List<String>
    //TODO valutare se transient è giusto in questo caso
    @Transient
    private List<IscrizioneTeam> iscrizioni;

    public Hackathon() {
    }

    /**
     * Creazione di un hackathon con tutti i suoi elementi, con valori di default per scadenza iscrizioni e stato iniziale
     * @param nome nome dell'hackathon, deve essere univoco
     * @param periodo periodo di svolgimento dell'hackathon
     * @param premio premio in denaro per il team vincitore, deve essere positivo
     * @param luogo luogo in cui si svolge l'hackathon
     * @param teamMax numero massimo di team che possono partecipare all'hackathon, deve essere positivo
     * @param teamMin numero minimo di team che devono partecipare all'hackathon, deve essere positivo e minore o uguale a teamMax
     * @param regolamento
     */
    public Hackathon(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin, String regolamento) {
        validazione(nome, periodo, premio, luogo, teamMax, teamMin, regolamento);
        this.nome = nome;
        this.periodo = periodo;
        this.premio = premio;
        this.luogo = luogo;
        this.teamMax = teamMax;
        this.teamMin = teamMin;
        this.regolamento = regolamento;
        // valori di default / inizializzazioni
        // esempio: scadenza iscrizioni 1 giorno prima della fine dell'hackathon
        this.scadenzaIscrizioni = periodo.getDataFine().minusDays(1).atStartOfDay();
        this.stato = IscrizioniAperte.INSTANCE; // stato iniziale, ad esempio "Iscrizioni Aperte"
        this.subscriber = new ArrayList<>();
        this.staff = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idHackathon == null) {
            this.idHackathon = "H-" + UUID.randomUUID();
        }
    }

    //TODO da fare
    private boolean validazione(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin, String regolamento) {
    return true;
    }

    //metodi da implementare

    public void setStato(StatoHackathon stato) {
        this.stato = stato;
    }

    public int getTeamMax() {
        return teamMax;
    }

    public int getTeamMin() {
        return teamMin;
    }

    public void aggiungiIscrizione(IscrizioneTeam iscrizione) {
        this.iscrizioni.add(iscrizione);
    }

    public String getInfo() {
        return this.regolamento;
    }

    public void attach(Subscriber subscriber) {
        //TODO IMPLEMENTARE
    }

    public void detach(Subscriber subscriber) {
        //TODO IMPLEMENTARE
    }

    public void notify(TipoNotifica evento) {
        //TODO IMPLEMENTARE
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

    public Periodo getPeriodo() { return  this.periodo; }

    public List<IscrizioneTeam> getIscrizioni() { return this.iscrizioni; }

}
