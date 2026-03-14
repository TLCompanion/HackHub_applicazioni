package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.statePattern.StatoHackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
            SELECT h 
            FROM Hackathon h
            WHERE h.stato = :stato
            AND (h.periodo.dataInizio < :today
            OR h.periodo.dataInizio = :today
               AND h.oraInizio <= :nowTime)
             ORDER BY h.periodo.dataInizio ASC, h.oraInizio ASC
                         """)
    List<Hackathon> findHackathonDaAvviare(
            @Param("stato") StatoHackathon stato,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime);

    //TODO aggiungere altre query per i passaggi temporali degli hackathon
}
