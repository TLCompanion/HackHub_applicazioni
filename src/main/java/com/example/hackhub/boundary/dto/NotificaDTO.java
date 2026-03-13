package com.example.hackhub.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificaDTO(
        @NotBlank String messaggio
) {
}
