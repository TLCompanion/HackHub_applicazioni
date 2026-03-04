package com.example.hackhub.repository;

import com.example.hackhub.domain.implementazione.Notifica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryNotifica extends JpaRepository<Notifica, String> {
}
