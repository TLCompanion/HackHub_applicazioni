package com.example.hackhub.domain.implementazione;

import com.example.hackhub.domain.RuoloTeam;

/**
 * Un utente registrato alla piattaforma che diventa parte di un team
 */
public class MembroTeam {

    private String idUtente; //identificativo unico del membro del team
    private String idTeam; //identificativo unico che associa il membro del team al suo team
    private RuoloTeam ruolo; //ruolo che il membro ricopre all'interno del Team

    /**
     * Creazione di un membro del team
     * @param idUtente l'identificativo dell'utente
     * @param idTeam l'identificativo del team associato al membro
     * @param ruolo il ruolo del membro nel team
     */
    public MembroTeam(String idUtente, String idTeam, RuoloTeam ruolo){
        this.idUtente = idUtente;
        this.idTeam = idTeam;
        this.ruolo = ruolo;
    }
}
