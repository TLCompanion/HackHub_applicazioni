package com.example.hackhub.observer;

import com.example.hackhub.domain.TipoNotifica;

import java.util.Map;

public class Evento {
    public final TipoNotifica tipo;
    public final Map<String, Object> data; // Dati aggiuntivi per l'evento

    public Evento(TipoNotifica tipo, Map<String, Object> data) {
        this.tipo = tipo;
        this.data = data;
    }

    public TipoNotifica getTipo() {
        return tipo;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
