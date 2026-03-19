package com.example.hackhub.boundary.dto;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Sottomissione;
import com.example.hackhub.domain.implementazione.Team;
import jakarta.validation.constraints.NotBlank;

public record IscrizioneTeamDTO(
        @NotBlank String nomeHackathon,
        @NotBlank String nomeTeam,
        String linkSottomissione
        ) {
}
