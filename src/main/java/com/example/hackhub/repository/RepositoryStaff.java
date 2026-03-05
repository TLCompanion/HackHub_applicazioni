package com.example.hackhub.repository;

import com.example.hackhub.domain.RuoloStaff;
import com.example.hackhub.domain.implementazione.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryStaff extends JpaRepository<Staff, String> {


}
