package com.example.hackhub.boundary.dto;

import com.example.hackhub.domain.implementazione.MembroTeam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamDTO(
        @NotNull String nome,
        @NotNull List<MembroTeam> membri
) {
}
