package com.example.hackhub.handler;

import com.example.hackhub.boundary.dto.AuthResponse;
import com.example.hackhub.boundary.dto.LoginRequest;
import com.example.hackhub.boundary.dto.RegisterRequest;
import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.eccezioni.BadRequestException;
import com.example.hackhub.eccezioni.NotFoundException;
import com.example.hackhub.repository.RepositoryUtenti;
import com.example.hackhub.servizi.ServizioJwt;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EffettuaAutenticazioneHandler {

    private final RepositoryUtenti repositoryUtenti;
    private final PasswordEncoder passwordEncoder;
    private final ServizioJwt servizioJwt;

    /**
     * Costruttore che inizializza questo handler
     * @param repositoryUtenti la repository degli utento
     * @param passwordEncoder l'encoder della password
     * @param servizioJwt il servizio jwt
     */
    public EffettuaAutenticazioneHandler(RepositoryUtenti repositoryUtenti, PasswordEncoder passwordEncoder, ServizioJwt servizioJwt) {
        this.repositoryUtenti = repositoryUtenti;
        this.passwordEncoder = passwordEncoder;
        this.servizioJwt = servizioJwt;
    }

    /**
     * Metodo che attiva la procedura di registrazione di un Visitatore alla piattaforma, per poter accedere
     * e partecipare agli hackathon
     * @param request il dto di richiesta di registrazione
     */
    @Transactional
    public void attivaRegistrazione(RegisterRequest request) {
        // Codifica della password in hash
        // Creo l'Utente nuovo e lo salvo nel database
        Utente utente = new Utente(
                request.nomeUtente(),
                request.email(),
                passwordEncoder.encode(request.password()));
        repositoryUtenti.save(utente);
    }

    /**
     * Metodo che attiva l'autenticazione di un utente tramite una richiesta di login
     * @param request la richiesta
     * @return una nuova authResponse
     */
    @Transactional
    public AuthResponse attivaAutenticazione(LoginRequest request) {
        // Prelevo l'utente dal db, se non esiste esco
        Utente utente = repositoryUtenti.findByNomeUtente(request.nomeUtente())
                .orElseThrow(() -> new BadRequestException("Nome utente errato"));

        // Altrimenti verifico se la password inserita è corretta, e in caso positivo genero il token
        if (!passwordEncoder.matches(request.password(), utente.getPasswordHash()))
            throw new BadRequestException("Password errata");
        String token = servizioJwt.generaToken(utente);
        return new AuthResponse(token, "Bearer");
    }
}
