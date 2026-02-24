package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Hackathon;
import com.example.hackhub.domain.implementazione.Sottomissione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorySottomissioni extends JpaRepository<Sottomissione, String> {

    List<Sottomissione> findByHackathon_IdHackathon(String idHackathon);

    List<Sottomissione> findByHackathon(Hackathon hackathon);
}
