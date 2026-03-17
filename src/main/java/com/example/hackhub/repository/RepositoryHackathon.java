package com.example.hackhub.repository;

import com.example.hackhub.domain.StatoEnum;
import com.example.hackhub.domain.implementazione.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RepositoryHackathon extends JpaRepository<Hackathon, String> {

    boolean existsByNome(String nome);

    Optional<Hackathon> findByNome(String nomeHackathon);

    Optional<Hackathon> findByIdHackathon(String idHackathon);

    // Query per trovare tutti gli hackathon che hanno le iscrizioni chiuse e la data di inizio passata, quindi pronti
    // per essere avviati (si mette quella passata perchè equals non è abbastanza preciso e potrebbe non funzionare)
    @Query("""
            SELECT h\s
            FROM Hackathon h
            WHERE h.statoEnum = :stato
            AND (h.periodo.dataInizio <= :today
            AND h.periodo.oraInizio <= :nowTime)
            ORDER BY h.periodo.dataInizio ASC, h.periodo.oraInizio ASC
                       \s""")
    List<Hackathon> findHackathonDaAvviare(
            @Param("stato") StatoEnum stato,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime);

    /**
     * Query che trova tutti gli hackathon di cui si devono chiudere le iscrizioni
     * @param statoEnum lo stato dell'hackathon
     * @param scadenza la data di scadenza
     * @return la lista degli hackathon di cui si devono chiudere le iscrizioni
     */
    @Query("""
        SELECT h
        FROM Hackathon h
        WHERE h.statoEnum = :stato
        AND h.scadenzaIscrizioni <= :scadenza
        ORDER BY h.periodo.dataInizio ASC, h.periodo.oraInizio ASC
                       \s""")
    List<Hackathon> findHackathonDaChiudere(
            @Param("stato") StatoEnum statoEnum,
            @Param("today") LocalDateTime scadenza
    );

    /**
     * Query che trova tutti gli hackathon da valutare
     * @param statoEnum lo stato dell'hackathon
     * @param scadenza la scadenza della consegna delle sottomissioni
     * @return la lista degli hackathon da valutare
     */
    @Query("""
        SELECT h
        FROM Hackathon h
        WHERE h.statoEnum = :stato
        AND h.periodo.dataFine <= :scadenza
        ORDER BY h.periodo.dataInizio ASC, h.periodo.oraInizio ASC
                       \s""")
    List<Hackathon> findHackathonDaValutare(
            @Param("stato") StatoEnum statoEnum,
            @Param("today") LocalDateTime scadenza
    );
}
