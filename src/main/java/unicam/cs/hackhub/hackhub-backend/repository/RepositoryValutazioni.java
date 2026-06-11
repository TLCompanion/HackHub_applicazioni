package unicam.cs.hackhub.backend.repository;

import unicam.cs.hackhub.backend.domain.implementazione.Valutazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryValutazioni extends JpaRepository<Valutazione, String> {
}
