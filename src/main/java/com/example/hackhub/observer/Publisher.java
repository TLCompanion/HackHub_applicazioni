package com.example.hackhub.observer;

import com.example.hackhub.domain.TipoNotifica;

public interface Publisher {

    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notify(Evento evento);
}
