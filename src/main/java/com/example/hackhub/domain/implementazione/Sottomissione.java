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
    private String link; //allegato al file con il progetto richiesto dall'hackathon

    @OneToOne
    @JoinColumn(name = "id_valutazione")
    private Valutazione valutazione;

    public Sottomissione() {}

    /**
     * Creazine di una nuova sottomissione di un team
     * @param link il file allegato
     */
    public Sottomissione(String link) {
        this.link = link;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco all'hackathon se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idSottomissione == null) this.idSottomissione = "S-" + UUID.randomUUID();
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

    public String getLink() { return link; }

    public Valutazione getValutazione() { return valutazione; }

    //mi serve per verificare velocemente se questa sottomissione ha già una valutazione o meno
    public boolean haValutazione() { return this.valutazione != null; }
}
