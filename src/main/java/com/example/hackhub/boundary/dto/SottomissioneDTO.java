package com.example.hackhub.boundary.dto;

import com.example.hackhub.domain.implementazione.Valutazione;
import jakarta.validation.constraints.NotBlank;

public record SottomissioneDTO(
        @NotBlank String link,
        String giudizio,
        int punteggio
) {
}
