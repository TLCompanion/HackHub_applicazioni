package com.example.hackhub.domain.implementazione;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe che gestisce le tempistiche di inizio e fine di un'hackathon
 */
@Embeddable
public class Periodo {

    //NotNull è utilizzato per assicurarsi che i campi data e ora non siano nulli, garantendo così che un periodo sia
    // sempre definito correttamente.
    @NotNull
    private LocalDate dataInizio;
    @NotNull
    private LocalTime oraInizio;
    @NotNull
    private LocalDate dataFine;
    @NotNull
    private LocalTime oraFine;

    public Periodo() {
    }

    /**
     * Creazione di un periodo di tempo con validazione per assicurarsi che la data e ora di inizio siano precedenti a
     * quelle di fine
     *
     * @param dataInizio la data d'inizio dell'intervallo
     * @param oraInizio  l'ora d'inizio dell'intervallo dell'intervallo
     * @param dataFine   la data di fine dell'intervallo
     * @param oraFine    l'ora in cui finisce l'intervallo
     */
    public Periodo(LocalDate dataInizio, LocalTime oraInizio, LocalDate dataFine, LocalTime oraFine) {
        validazione(dataInizio, dataFine, oraInizio, oraFine);
        this.dataInizio = dataInizio;
        this.oraInizio = oraInizio;
        this.dataFine = dataFine;
        this.oraFine = oraFine;
    }

    /**
     * Creazione di un periodo di tempo con orari predefiniti (00:00 - 23:59) con validazione per assicurarsi che la
     * data di inizio sia precedente a quella di fine
     *
     * @param dataInizio la data d'inizio dell'intervallo
     * @param dataFine   la data di fine dell'intervallo
     */
    public Periodo(LocalDate dataInizio, LocalDate dataFine) {
        validazione(dataInizio, dataFine, LocalTime.of(0, 0), LocalTime.of(23, 59));
        this.dataInizio = dataInizio;
        this.oraInizio = LocalTime.of(0, 0);
        this.oraFine = LocalTime.of(23, 59);
        this.dataFine = dataFine;

    }

    //Aggiunta di un metodo di validazione per assicurarsi che la data e ora di inizio siano precedenti a quelle di fine
    @AssertTrue(message = "La data e ora di inizio devono essere precedenti alla data e ora di fine")
    private boolean validazione(LocalDate dataInizio, LocalDate dataFine, LocalTime oraInizio, LocalTime oraFine) {
        return dataInizio.isBefore(dataFine) || (dataInizio.isEqual(dataFine) && oraInizio.isBefore(oraFine));
    }


    // METODI GETTER

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }
}
