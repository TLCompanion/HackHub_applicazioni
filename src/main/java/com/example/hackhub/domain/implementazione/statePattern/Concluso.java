package com.example.hackhub.domain.implementazione.statePattern;

public class Concluso implements StatoHackathon {
    public static final Concluso INSTANCE = new Concluso();

    private Concluso() {}
}
