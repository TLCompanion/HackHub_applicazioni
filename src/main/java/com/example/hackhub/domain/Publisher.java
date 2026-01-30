package com.example.hackhub.domain;

public interface Publisher {

    void attach(Subscriber subscriber);

    void detach(Subscriber subscriber);

    void notify(TipoNotifica evento);
}
