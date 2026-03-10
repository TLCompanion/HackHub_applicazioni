package com.example.hackhub.servizi;

import com.example.hackhub.domain.implementazione.Utente;
import com.example.hackhub.repository.RepositoryUtenti;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    private final ServizioJwt servizioJwt;
    private final RepositoryUtenti repositoryUtenti;

    /**
     * Costruttore che inizializza JwtFiltro
     * @param servizioJwt
     * @param repositoryUtenti
     */
    public JwtFiltro(ServizioJwt servizioJwt, RepositoryUtenti repositoryUtenti) {
        this.servizioJwt = servizioJwt;
        this.repositoryUtenti = repositoryUtenti;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String nomeUtente = null;

        // Verifica che ci sia header e che inizi con Bearer
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Rimuove Bearer
            try {
                nomeUtente = servizioJwt.estraiNomeUtente(token);
            }
            catch (Exception ignored) {}
        }

        // ricavo l'utente se non c'è già autenticazione, e valido il token
        if (nomeUtente != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Utente utente = repositoryUtenti.findByNomeUtente(nomeUtente).orElse(null);

            if (utente != null) {
                try { // Valido il token e creo un oggetto Authentication
                    servizioJwt.validaToken(token, utente);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    utente,
                                    null,
                                    Collections.emptyList());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                catch (Exception ignored) {}
            }
        }
        chain.doFilter(request, response); // Continua la catena
    }
}
