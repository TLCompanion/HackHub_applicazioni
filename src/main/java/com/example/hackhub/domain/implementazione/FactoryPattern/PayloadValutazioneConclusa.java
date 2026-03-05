package com.example.hackhub.domain.implementazione.FactoryPattern;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.TipoRichiesta;

public class PayloadValutazioneConclusa extends Payload {


    protected PayloadValutazioneConclusa(TipoRichiesta tipo) {
        super(tipo);
    }
}
