package unicam.cs.hackhub.backend.repository;

import unicam.cs.hackhub.backend.domain.implementazione.Sottomissione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorySottomissioni extends JpaRepository<Sottomissione, String> {

}
