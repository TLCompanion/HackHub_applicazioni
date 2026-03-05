package com.example.hackhub.observer;

import java.util.ArrayList;
import java.util.List;

public class GeneralPublisher implements Publisher {
    private final List<Subscriber> subscribers = new ArrayList<>();

    public GeneralPublisher(List<Subscriber> subscribers) {
        if (subscribers != null) {
            this.subscribers.addAll(subscribers);
        }
    }


    @Override
    public void subscribe(Subscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }

    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notify(Evento evento) {
        for(Subscriber subscriber : subscribers){
            subscriber.update(evento);
        }
    }
}
