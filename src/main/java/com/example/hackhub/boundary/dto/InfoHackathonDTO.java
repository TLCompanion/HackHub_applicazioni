package com.example.hackhub.boundary.dto;

import com.example.hackhub.domain.implementazione.statePattern.StatoHackathon;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InfoHackathonDTO(
        @NotBlank String nome,
        @NotNull LocalDate dataInizio,
        @NotNull LocalDate dataFine,
        @NotBlank String luogo,
        @NotNull @Positive BigDecimal premio,
        @Min(3) @Max(6) int teamMin,
        @Max(6) @Min(3) int teamMax,
        @NotBlank String regolamento,
        @NotNull LocalDateTime scadenzaIscrizioni,
        @NotNull StatoHackathon stato,
        //todo scegliere un minimo e un massimo per queste
        @Min(5) int numeroTeamIscritti,
        @Max(40) int maxIscrizioni,
        int postiRimanenti,
        @NotBlank String regolamentoDisponibile
) {
}
