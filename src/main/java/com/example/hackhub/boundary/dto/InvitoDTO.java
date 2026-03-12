package com.example.hackhub.boundary.dto;

import com.example.hackhub.domain.RuoloStaff;
import jakarta.validation.constraints.*;

/**
 * DTO che rappresenta un invito ad entrare a far parte dello Staff di un hackathon o di un Team
 */
public record InvitoDTO(
    @NotBlank String destinatario,
    @NotBlank String tipo,
    String nomeTeam,
    String nomeHackathon,
    RuoloStaff ruolo
) {
}
