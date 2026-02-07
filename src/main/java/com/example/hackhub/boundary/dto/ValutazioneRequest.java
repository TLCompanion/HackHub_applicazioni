package com.example.hackhub.boundary.dto;

/*
Questo rappresenta il body JSON tipo:
{ "giudizio": "…", "punteggio": 8 }
 */
public record ValutazioneRequest(String giudizio, int punteggio) {

}
