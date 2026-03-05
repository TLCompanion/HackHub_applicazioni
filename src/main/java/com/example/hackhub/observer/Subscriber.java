package com.example.hackhub.observer;

import com.example.hackhub.domain.TipoNotifica;

public interface Subscriber {

    public void update(Evento evento);
}
