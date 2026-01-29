package com.example.hackhub.domain.team;

/**
 * Un Team registrato nella piattaforma, di cui fanno parte un gruppo di Utenti, di cui uno è il Leader,
 * ovvero l'Utente che ha creato il Team.
 */
public class Team {
    private String nome; // nome del team, unico nella piattaforma
    private String idTeam; // identificativo univoco del team
    private String idLeader; // id dell'Utente Leader del Team
    private String[] idMembri; // id degli Utenti Membri del Team
    private int numMembri; // numero di Membri del Team

    /**
     * Metodo che crea un nuovo Team.
     *
     * @param nome il nome del Team
     * @param id l'identificativo del team
     * @param leader l'id dell'Utente che crea il Team
     * @param membri gli id degli altri Utenti Membri del Team
     */
    public Team(String nome, String id, String leader, String[] membri) {
        this.nome = nome;
        this.idTeam = id;
        this.idLeader = leader;
        this.idMembri = membri;
        this.numMembri = idMembri.length;
    }

    // DI SEGUITO SONO RIPORTATI TUTTI I METODI GETTER

    public String getNome() { return this.nome; }

    public String getIdTeam() { return idTeam; }

    public String getIdLeader() { return idLeader; }

    public String[] getIdMembri() { return idMembri; }

    public int getNumMembri() { return numMembri; }
}
