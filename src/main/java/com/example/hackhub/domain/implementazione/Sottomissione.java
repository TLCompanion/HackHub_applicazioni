package com.example.hackhub.domain.implementazione;

import com.example.hackhub.repository.RepositorySottomissioni;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Sottomissione creata dai team per un'hackathon
 */
@Entity
@Table(name = "sottomissioni")
public class Sottomissione {

    @Id
    @Column(nullable = false, updatable = false)
    private String idSottomissione;

    @Column(nullable = false)
    private String riferimentoFile; //allegato al file con il progetto richiesto dall'hackathon

    //private String idValutazione
    @OneToOne
    @JoinColumn(name = "id_valutazione")
    private Valutazione valutazione;

    /*
    Mi serve per verificare se ValutazioneHandler funziona o no, penso sia comunque comodo da aggiungere anche in UML
    ma non so se è necessario, se non è necessario lo tolgo, ditemi voi
    TODO: se decidiamo di tenerlo, aggiungere anche in UML e sistemare errore
     */
    @ManyToOne
    @JoinColumn(name = "id_hackathon", nullable = false)
    private Hackathon hackathon;

    public Sottomissione() {}

    /**
     * Creazine di una nuova sottomissione di un team
     * @param riferimentoFile il file allegato
     */
    public Sottomissione(String riferimentoFile) {
        this.riferimentoFile = riferimentoFile;
        this.valutazione = null; //all'inizio la valutazione non c'è
        this.idSottomissione = "v" + UUID.randomUUID();
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idSottomissione == null) {
            this.idSottomissione = "S-" + UUID.randomUUID();
        }
    }

    /**
     * Metodo che assegna una valutazione a questa sottomissione
     *
     * @param valutazione la valutazione da assegnare
     */
    //String idValutazione
    public void impostaValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }



    // METODI GETTER

    public String getIdSottomissione() { return idSottomissione; }

    public String getRiferimentoFile() { return riferimentoFile; }

    public Valutazione getValutazione() { return valutazione; }

    //mi serve per verificare velocemente se questa sottomissione ha già una valutazione o meno
    public boolean haValutazione() { return this.valutazione != null; }

    //metodo getter per l'hackathon, se decidiamo di tenerlo
    public Hackathon getHackathon() { return hackathon; }
}
