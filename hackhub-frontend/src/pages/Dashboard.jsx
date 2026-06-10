import React from "react";
import { useNavigate } from "react-router-dom";

export default function Dashboard(){

    const navigate = useNavigate();

    /*Funzione che mi ritorna alla pagina del login quando clicco esci*/ 
    const handleLogout = () => {
        navigate("/"); 
    };

    return ( 
    <div className='dashboard'>
        {/* Colonna a sinistra che contiene il menu */} 
        <aside className='sidebar'>
        <div className='logo'>HACKHUB</div>

        <nav className='navigation'>
            <p className='group-title'>NAVIGAZIONE</p>
            <ul>
            <button className='side-menu'>Dashboard</button>
            <button className='side-menu'>Hackathon</button>
            <button className='side-menu'>I miei team</button>
            <button className='side-menu'>Sottomissioni</button>
            </ul>
        </nav>

        <nav className='navigation'>
            <p className='group-title'>ACCOUNT</p>
            <ul>
            <button className='side-menu'>Profilo</button>
            <button className='side-menu'>Impostazioni</button>
            <button onClick={handleLogout} className='side-menu'>Esci</button>
            </ul>
        </nav>
        </aside>

        {/* parte centrale con tutto il contenuto */} 
        <div className='content'>

        {/* parte alta con la barra di ricerca */} 
        <header className='header'>
            <input type='text' placeholder='Cerca Hackathon' className='search-bar'/>
        </header>

        {/* Box centrali più grandi */} 
        <main className='center'>
            <section className='center-column'>

            <div className='welcome'>
                <h2 className='welcome-speech'>Benvenuto su Hackhub</h2>
                <p className='welcome-speech'>Questa piattaforma ti permette di creare team e iscriverti con i tuoi amici a vari hackathon presenti in tutta italia e di gestirli nel modo più veloce e semplice possibile.</p>
            </div>

            <div className='center-box'>
                <h3 className='row-name'>Hackathon</h3>
                <div className='hackathon-list'>
                <div className='hackathon-card'>
                    <div className='card-header'></div>
                    <div className='card-body'>
                    <h4 className='nome-hackathon'>nome</h4>
                    <p className='data'> data </p>
                    <p className='luogo'>luogo</p>
                    </div>
                </div>
                </div>
            </div>

            <div className='bottom-box'>
            </div>

            </section>

            {/* box a destra più piccoli */} 
            <aside className="widgets-column">
                <div className="widget-box"></div>
                <div className="widget-box"></div>
                <div className="widget-box calendar-placeholder">
                <p>Calendario</p>
                </div>
            </aside>
        </main>
        </div>
    </div>
    );
    }