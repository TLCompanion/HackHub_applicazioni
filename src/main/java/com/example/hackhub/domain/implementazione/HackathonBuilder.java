package com.example.hackhub.domain.implementazione;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HackathonBuilder {

    String nome;
    Periodo periodo;
    BigDecimal premio;
    String luogo;
    int teamMax;
    int teamMin;
    String regolamento;
    LocalDateTime scadenzaIscrizioni;
    int maxIscrizioni;

    public void impostaNome(String nome) {
        this.nome = nome;
    }

    public void impostaPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public void impostaPremio(BigDecimal premio) {
        this.premio = premio;
    }

    public void impostaLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void impostaTeamMax(int teamMax) {
        this.teamMax = teamMax;
    }

    public void impostaTeamMin(int teamMin) {
        this.teamMin = teamMin;
    }

    public void impostaRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public void impostaScadenzaIscrizioni(LocalDateTime scadenza) {
        this.scadenzaIscrizioni = scadenza;
    }

    public void impostaMaxIscrizioni(int maxIscrizioni) {
        this.maxIscrizioni = maxIscrizioni;
    }

    public Hackathon getRisultato() {
        return new Hackathon(this.nome, this.periodo, this.premio, this.luogo, this.teamMax, this.teamMin,
                this.scadenzaIscrizioni, this.regolamento, this.maxIscrizioni);
    }
}
