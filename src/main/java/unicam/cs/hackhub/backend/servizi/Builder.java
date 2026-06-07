package unicam.cs.hackhub.backend.servizi;

import unicam.cs.hackhub.backend.domain.implementazione.Hackathon;
import unicam.cs.hackhub.backend.domain.implementazione.Periodo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Builder {

    void impostaNome(String nome);

    void impostaPeriodo(Periodo periodo);

    void impostaPremio(BigDecimal premio);

    void impostaLuogo(String luogo);

    void impostaTeamMax(int teamMax);

    void impostaTeamMin(int teamMin);

    void impostaRegolamento(String regolamento);

    void impostaScadenzaIscrizioni(LocalDateTime scadenza);

    void impostaMaxIscrizioni(int maxIscrizioni);

    Hackathon getRisultato();

    void reset();
}
