package com.example.hackhub.servizi.esterni;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SistemaDiPagamentoMock {

    public void pagaPremio(String recapitoOrg, String recapitoBancario, BigDecimal premio){}
}
