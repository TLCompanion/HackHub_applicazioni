package com.example.hackhub.boundary.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//TODO ricordarsi di usare swagger o cose simili per documentare le API, in modo da rendere chiaro quali sono i campi
// richiesti e il formato atteso per ogni campo. Inoltre, è importante gestire correttamente le validazioni dei dati in
// ingresso, ad esempio assicurandosi che le date siano valide e che i numeri siano positivi quando necessario.
public record HackathonRequest(
        @NotBlank String nome,
        @NotNull LocalDate dataInizio,
        @NotNull LocalDate dataFine,
        @NotBlank String luogo,
        @NotNull @Positive BigDecimal premio,
        @Min(3) int teamMin,
        @Max(6) int teamMax,
        @Min(1) int maxIscrizioni,
        @NotBlank String regolamento,
        @NotBlank String nomeGiudice,
        @NotBlank @NotNull List<String> nomeMentore
) {
}
