package com.example.hackhub.domain.implementazione.FactoryPattern;

import com.example.hackhub.domain.TipoNotifica;
import com.example.hackhub.domain.implementazione.Periodo;

public class PayloadNotificaGenerica extends Payload {

    private final String messaggio;

    /**
     * Crea una proposta di chiamata da parte di un mentore per un team
     */
    public PayloadNotificaGenerica(String messaggio) {
        super(TipoNotifica.NOTIFICA_GENERICA);
        this.messaggio = messaggio;
    }

    @Override
    public TipoNotifica getTipo() {
        return TipoNotifica.NOTIFICA_GENERICA;
    }
}
