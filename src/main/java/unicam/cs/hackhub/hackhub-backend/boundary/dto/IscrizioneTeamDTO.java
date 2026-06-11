package unicam.cs.hackhub.backend.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record IscrizioneTeamDTO(
        @NotBlank String nomeHackathon,
        @NotBlank String nomeTeam,
        String linkSottomissione
        ) {
}
