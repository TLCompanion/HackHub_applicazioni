package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Sottomissione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorySottomissioni extends JpaRepository<Sottomissione, String> {
/*
    //String idSottomissione
    Sottomissione getSottomissione(Sottomissione sottomissione);

    void salva(Sottomissione sottomissione); */

    List<Sottomissione> findByHackathon(Hackathon hackathon);
}
