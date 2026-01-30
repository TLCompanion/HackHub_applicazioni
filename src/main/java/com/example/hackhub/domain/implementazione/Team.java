package com.example.hackhub.domain.implementazione;

import java.util.ArrayList;
import java.util.List;

/**
 * Un Team registrato nella piattaforma, di cui fanno parte un gruppo di Utenti, di cui uno è il Leader,
 * ovvero l'Utente che ha creato il Team.
 */
public class Team {
    private String nome; // nome del team, unico nella piattaforma
    private String idTeam; // identificativo univoco del team
    private List<MembroTeam> membri; //insieme degli utenti che fanno parte del team

    /**
     * Metodo che crea un nuovo Team.
     *
     * @param nome il nome del Team
     * @param id l'identificativo del team
     */
    public Team(String nome, String id) {
        this.nome = nome;
        this.idTeam = id;
        this.membri = new ArrayList<>();
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNome() { return this.nome; }

    public String getIdTeam() { return idTeam; }

    public List<MembroTeam> getMembri() { return membri;}
}
