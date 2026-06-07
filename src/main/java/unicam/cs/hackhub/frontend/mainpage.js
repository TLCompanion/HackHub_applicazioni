import React, { useState, useEffect } from 'react';
import './App.css'; // Opzionale, per i tuoi stili web

export default function App() {
    // 1. Stati per gestire i dati del backend, il caricamento e gli errori
    const [dati, setDati] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 2. useEffect si avvia non appena la pagina viene caricata nel browser
    useEffect(() => {
        // Sostituisci con l'URL reale del tuo endpoint REST (es. porta 8080 di Java)
        fetch('http://localhost:8080/api/risorsa')
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Errore nella risposta del server backend');
                }
                return response.json();
            })
            .then((data) => {
                setDati(data);       // Salva i dati nel nostro stato
                setLoading(false);   // Disattiva la schermata di caricamento
            })
            .catch((err) => {
                console.error("Dettaglio errore:", err);
                setError(err.message);
                setLoading(false);
            });
    }, []); // L'array vuoto [] assicura che la chiamata venga fatta una sola volta all'avvio

    // 3. Gestione visiva degli stati di caricamento ed errore
    if (loading) return <div className="loading">Caricamento dati dal backend...</div>;
    if (error) return <div className="error">Si è verificato un errore: {error}</div>;

    // 4. Il layout HTML (JSX) vero e proprio della tua pagina principale
    return (
        <div className="main-container">
            <header>
                <h1>Pannello di Controllo</h1>
                <p>Frontend React collegato al database MySQL via REST API</p>
            </header>

            <main>
                <h2>Dati Ricevuti</h2>

                {dati.length === 0 ? (
                    <p>Nessun dato presente nel database.</p>
                ) : (
                    <ul className="data-list">
                        {/* Ciclo sui dati ricevuti dal backend (assumendo che sia una lista di oggetti con un id e un nome) */}
                        {dati.map((item) => (
                            <li key={item.id} className="data-item">
                                <strong>ID:</strong> {item.id} - <strong>Nome:</strong> {item.nome}
                            </li>
                        ))}
                    </ul>
                )}
            </main>
        </div>
    );
}