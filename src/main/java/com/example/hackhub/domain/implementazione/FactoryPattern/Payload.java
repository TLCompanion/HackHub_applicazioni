package com.example.hackhub.domain.implementazione.FactoryPattern;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.TipoRichiesta;

public abstract class Payload {

    private TipoRichiesta tipo;

    protected Payload(TipoRichiesta tipo){
        this.tipo = tipo;
    }

    public TipoRichiesta getTipo() {
        return tipo;
    }
}
