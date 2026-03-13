package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryHackathon extends JpaRepository<Hackathon, String> {

    boolean existsByNome(String nome);

    Optional<Hackathon> findByNome(String nomeHackathon);

    Optional<Hackathon> findByIdHackathon(String idHackathon);
}
