package unicam.cs.hackhub.backend.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record SottomissioneDTO(
        @NotBlank String link,
        String giudizio,
        int punteggio
) {
}
