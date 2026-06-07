package unicam.cs.hackhub.backend.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificaDTO(
        @NotBlank String messaggio
) {
}
