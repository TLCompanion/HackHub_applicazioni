package com.example.hackhub.boundary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO per la richiesta di proposta di call per un hackathon.
 * @param idHackathon l'id dell'hackathon per cui si vuole proporre la call, che deve essere una stringa non vuota
 * @param idTeam l'id del team che propone la call, che deve essere una stringa non vuota
 * @param data la data in cui si vuole tenere la call, che deve essere una data valida e non nulla
 * @param ora  l'ora in cui si vuole tenere la call, che deve essere un'ora valida e non nulla
 */
public record PropostaCallRequest(
        @NotBlank String idHackathon,
        @NotBlank String idTeam,
        @NotNull LocalDate data,
        @NotNull LocalTime ora
) { }
