package unicam.cs.hackhub.repository;

import unicam.cs.hackhub.domain.RuoloTeam;
import unicam.cs.hackhub.domain.implementazione.MembroTeam;
import unicam.cs.hackhub.domain.implementazione.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryMembriTeam extends JpaRepository<MembroTeam, String> {

    /**
     * Controlla se esiste un membro del team con l'utente specificato
     *
     * @param utente l'utente
     * @return vero se esiste, false altrimenti
     */
    boolean existsByUtente(Utente utente);

    /**
     * Trova un membro del team dal suo ruolo
     *
     * @param ruolo il ruolo
     * @return il membro del team, un optional vuoto se non esiste
     */
    Optional<MembroTeam> findMembroTeamByRuolo(RuoloTeam ruolo);

    /**
     * Trova un membro del team dal suo nome utente
     *
     * @param nomeUtente il nome dell'utente
     * @return il membro del team, un optional vuoto se non esiste
     */
    Optional<MembroTeam> findByUtente_NomeUtente(String nomeUtente);

    /**
     * Trova un membro del team dal suo id
     *
     * @param utenteIdUtente id dell'utente
     * @return il membro del team, un optional vuoto se non esiste
     */
    Optional<MembroTeam> findByUtente_idUtente(String utenteIdUtente);

    Optional<MembroTeam> findByTeam_IdTeamAndRuolo(String idTeam, RuoloTeam ruolo);
}
