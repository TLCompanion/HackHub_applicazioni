package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce gli elementi generali di una richiesta
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Richiesta {

    @Id
    private String idRichiesta;
    private String nomeMittente;
    @OneToOne
    private Utente destinatario;
    private StatoRichiesta stato;
    private String payload;
    private LocalDateTime scadenza;

    public Richiesta(){}

    /**
     * Creazione di una nuova richiesta
     * @param nomeMittente il mittente della richiesta
     */
    public Richiesta(String nomeMittente, String payload, Utente destinatario, LocalDateTime scadenza) {
        this.nomeMittente = nomeMittente;
        this.destinatario = destinatario;
        this.stato = StatoRichiesta.INVIATO; //all'inizio quando ancora la richiesta non è stata valutata lo stato è sempre inviato
        this.payload = payload;
        this.scadenza = scadenza;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco alla richiesta se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.

    /**
     * Assegna un id univoco ad ogni richiesta
     */
    @PrePersist
    private void assegnaId() {
        if (this.idRichiesta == null) {
            this.idRichiesta = "R-" + UUID.randomUUID();
        }
    }

    /**
     * Metodo vuoto che consente di accettare quanto indicato nella richiesta
     */
    public void accetta() {
        this.setStato(StatoRichiesta.ACCETTATO);
    }

    /**
     * Metodo vuoto che consente di rifiutare quando indicato nella richiesta
     */
    public void rifiuta() {
        this.setStato(StatoRichiesta.RIFIUTATO);
    }

    // METODI GETTER

    public String getIdRichiesta() { return idRichiesta; }

    public String getMittente() { return nomeMittente; }

    public Utente getDestinatario() { return destinatario; }

    public StatoRichiesta getStato() { return stato; }

    public String getPayload() { return payload; }

    public LocalDateTime getScadenza() { return scadenza; }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }
}
