package com.example.hackhub.domain.implementazione;

import com.example.hackhub.repository.RepositoryHackathon;
import com.example.hackhub.repository.RepositoryTeam;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class Validatore {

    private Validatore instance;

    private final RepositoryHackathon repositoryHackathon;

    private final RepositoryTeam repositoryTeam;
    /**
     * Costruzione di un'entità di validatore
     */
    @Autowired
    public Validatore(RepositoryHackathon rh, RepositoryTeam rt) {
        this.repositoryHackathon = rh;
        this.repositoryTeam = rt;
    }

    /**
     * Se l'istanza è nulla ne creo una nuuova
     * @return l'istanza creata se è null o quella precedentemente esistente se non è nulla
     */
    /*
    public Validatore getInstance() {
        if (instance == null) {
            instance = new Validatore();
        }
        return instance;
    }*/

    /**
     * Verifica se il nome dell'hackathon è già stato usato nel database
     * @param hackathon l'hackathon da verificare
     * @return true se è presente, false altrimenti
     */
    public boolean verificaNomeHackathon(Hackathon hackathon){
        return repositoryHackathon.cercaNome(hackathon.getNome());
    }

    /**
     * Verifica se il premio inserito è valido
     *
     * @param premio il premio da verificare
     * @return true se il premio è BigDecimal, false altrimenti
     */
    public boolean verificaPremio(BigDecimal premio){
        return premio != null;
    }

    /**
     * Verifica che il numero minimo di membri per team sia rispettato
     * @param teamMin il numero da verificare
     * @return true se ci sono almeno 3 membri, false altrimenti
     */
    public boolean verificaTeamMin(int teamMin){
        return teamMin >= 3;
    }

    /**
     * Verifica che il numero massimo di membri sia valido
     * @param teamMax il numero massimo di membri
     * @param teamMin il numero minimo di membri per team
     * @return true se teamMax >= teamMin, false altrimenti
     */
    public boolean verificaTeamMax(int teamMax, int teamMin){
        return teamMax >= teamMin;
    }

    /**
     * Verifica che il nome del team non sia già stato usato nel db
     * @param team il team da verificare
     * @return true se è già presente, false altrimenti
     */
    public boolean verificaNomeTeam(Team team){
        return repositoryTeam.esisteNomeTeam(team.getNome());
    }
}
