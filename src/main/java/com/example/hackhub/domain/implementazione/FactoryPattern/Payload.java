package com.example.hackhub.domain.implementazione.FactoryPattern;

import com.example.hackhub.domain.TipoNotifica;

public abstract class Payload {

    private TipoNotifica tipo;

    protected Payload(TipoNotifica tipo){
        this.tipo = tipo;
    }

    public TipoNotifica getTipo() {
        return tipo;
    }
}
