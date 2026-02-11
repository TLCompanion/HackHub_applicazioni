package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryHackathon extends JpaRepository<Hackathon, String> {

    boolean existsByNome(String nome);
/*
    void salva(Hackathon hackathon);

    List<Hackathon> getHackathonAperti();

    //String idHackathon
    Hackathon getHackathon(Hackathon hackathon);*/
}
