package com.example.hackhub.domain.implementazione;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe che gestisce le tempistiche di inizio e fine di un'hackathon
 */
public class Periodo {

    private LocalDate dataInizio;
    private LocalTime oraInizio;
    private LocalDate dataFine;
    private LocalTime oraFine;

    /**
     * Creazione di un periodo di tempo
     * @param dataInizio la data d'inizio dell'intervallo
     * @param oraInizio l'ora d'inizio dell'intervallo dell'intervallo
     * @param dataFine la data di fine dell'intervallo
     * @param oraFine l'ora in cui finisce l'intervallo
     */
    public Periodo(LocalDate dataInizio, LocalTime oraInizio, LocalDate dataFine, LocalTime oraFine) {
        this.dataInizio = dataInizio;
        this.oraInizio = oraInizio;
        this.dataFine = dataFine;
        this.oraFine = oraFine;
    }



    // METODI GETTER

    public LocalDate getDataInizio() { return dataInizio; }

    public LocalTime getOraInizio() { return oraInizio; }

    public LocalDate getDataFine() { return dataFine; }

    public LocalTime getOraFine() { return oraFine; }
}
