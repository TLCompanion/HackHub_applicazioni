package com.example.hackhub.servizi;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.FactoryPattern.Payload;
import com.example.hackhub.domain.implementazione.FactoryPattern.PayloadInvitoStaff;
import com.example.hackhub.domain.implementazione.FactoryPattern.PayloadNotificaGenerica;
import com.example.hackhub.domain.implementazione.FactoryPattern.PayloadPropostaCall;
import com.example.hackhub.domain.implementazione.Periodo;
import org.springframework.stereotype.Service;

@Service
public class FactoryPayload {

    public Payload creaPayloadPropostaCall(String nomeHackathon, Periodo periodo) {
        return new PayloadPropostaCall(nomeHackathon, periodo);
    }

    public Payload creaPayloadInvitoStaff(String nomeHackathon, RuoloStaff ruolo) {
        return new PayloadInvitoStaff(nomeHackathon, ruolo);
    }

    public Payload creaPayloadNotificaGenerica(String messaggio){
        //TODO per ora ho messo un messaggio molto genericoo poi cambiamo in base a quello che vogliamo mettere
        return new PayloadNotificaGenerica("Notifica generica");
    }
}
