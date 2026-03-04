package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.*;
import com.example.hackhub.domain.implementazione.statePattern.IscrizioniAperte;
import com.example.hackhub.eccezioni.ConflictException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotBlank
    private String luogo;

    @Max(6)
    private int teamMax;

    @Min(3)
    private int teamMin;

    @Lob @NotBlank
    private String regolamento;

    @Min(1)
    private int maxIscrizioni; //Massimo numero di iscrizioni (team) che possono partecipare all'hackathon

    //TODO valutare se transient è giusto in questo caso, chat dice: "Se vuoi davvero State pattern con classi: allora
    // in persistenza memorizzi almeno un “codice stato” e ricostruisci l’oggetto stato a runtime (factory dello stato)."
    @Transient
    private StatoHackathon stato;

    @NotNull
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
     * @param regolamento il regolamento associato all'hackathon
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
    private boolean validazione(String nome, Periodo periodo, BigDecimal premio, String luogo, int teamMax, int teamMin, String regolamento, LocalDateTime scadenzaIscrizioni) {
    return true;
    }

    public void aggiungiIscrizioneTeam(IscrizioneTeam iscrizione) {
        if (iscrizioni.size() == maxIscrizioni) {
            throw new ConflictException("Numero massimo di iscrizioni raggiunto");
        }
        if (stato != IscrizioniAperte.INSTANCE) {
            throw new ConflictException("Non è possibile iscrivere un team, le iscrizioni non sono aperte");
        }
        this.iscrizioni.add(iscrizione);
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

    /**
     * Aggiunge un subscriber alla lista dei subscriber se non è già presente
     * @param subscriber il subscriber da aggiungere
     */
    public void attach(Subscriber subscriber) {
        if (subscriber == null){
            // TODO qui servirebbe un'eccezione diversa?
            throw new NullPointerException("Il subscriber non può essere nullo");
        }

        if (!this.subscriber.contains(subscriber))
            this.subscriber.add(subscriber);
    }

    /**
     * Toglie un subscriber dalla lista dei subscriber
     * @param subscriber il subscriber da rimuovere
     */
    public void detach(Subscriber subscriber) {
        if (subscriber == null){
            throw new NullPointerException("Il subscriber non può essere nullo");
        }

        this.subscriber.remove(subscriber);
    }

    /**
     * Notifica tutti i subscriber dei cambiamenti
     * @param evento l'evento da notificare
     */
    public void notify(TipoNotifica evento) {
        for (Subscriber s : this.subscriber) {
            s.update(evento);
        }
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

    public BigDecimal getPremio() { return this.premio; }

     public String getLuogo() { return this.luogo; }

     public String getRegolamento() { return this.regolamento; }

     public LocalDateTime getScadenzaIscrizioni() { return this.scadenzaIscrizioni; }

    public int getMaxIscrizioni() { return this.maxIscrizioni; }

}
