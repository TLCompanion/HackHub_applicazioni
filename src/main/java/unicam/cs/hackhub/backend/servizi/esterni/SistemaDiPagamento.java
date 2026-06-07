package unicam.cs.hackhub.backend.servizi.esterni;

import java.math.BigDecimal;

public interface SistemaDiPagamento {

    void pagaPremio(String recapitoOrg, String recapitoBancario, BigDecimal premio);
}
