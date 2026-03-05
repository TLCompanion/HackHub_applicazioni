package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.StatoRichiesta;
import com.example.hackhub.domain.TipoRichiesta;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

/**
 * Classe che gestisce gli elementi generali di una richiesta
 */
@Entity
public class Richiesta {

    @Id
    private String idRichiesta;
    private String nomeMittente;
    @Transient
    private List<Utente> destinatari;
    private StatoRichiesta stato;
    @Column(insertable=false, updatable=false)
    private TipoRichiesta tipo;
    @Nullable
    private Periodo periodo;
    @Embedded
    private String payload;

    public Richiesta(){}

    /**
     * Creazione di una nuova richiesta
     * @param nomeMittente il mittente della richiesta
     */
    public Richiesta(String nomeMittente, String payload, TipoRichiesta tipo, List<Utente> destinatari, @org.jspecify.annotations.Nullable Periodo periodo) {
        this.nomeMittente = nomeMittente;
        this.tipo = tipo;
        this.destinatari = destinatari;
        this.stato = StatoRichiesta.INVIATO; //all'inizio quando ancora la richiesta non è stata valutata lo stato è sempre inviato
        this.payload = payload;
        this.periodo = periodo;
    }

    //PrePersist serve per fare operazioni prima di salvare l'entità nel database, in questo caso per assegnare un id
    // univoco alla richiesta se non è già stato assegnato, viene automaticamente chiamato da JPA/Hibernate quando si
    // salva l'entità per la prima volta.
    @PrePersist
    private void assegnaId() {
        if (this.idRichiesta == null) {
            this.idRichiesta = "R-" + UUID.randomUUID();
        }
    }

    // METODI GETTER


    public String getIdRichiesta() { return idRichiesta; }

    public StatoRichiesta getStato() { return stato; }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }
}
