package unicam.cs.hackhub.backend.repository;

import unicam.cs.hackhub.backend.domain.implementazione.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RepositoryTeam extends JpaRepository<Team, String> {

    /**
     * Cotrolla se esiste un team con il nome specificato
     *
     * @param nomeTeam il nome del team
     * @return vero se il team esiste, falso altrimenti
     */
    boolean existsByNome(String nomeTeam);

    /**
     * Ritorna il team con il nome specificato
     *
     * @param nomeTeam il nome del team
     * @return il team se esiste, altrimenti un Optional vuoto
     */
    Optional<Team> findByNome(String nomeTeam);
}
