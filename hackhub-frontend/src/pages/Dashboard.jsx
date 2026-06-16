import React, { useEffect, useState } from "react";
import { data, useNavigate } from "react-router-dom";
import { API_URL } from "../config/api";
import Sidebar from "../components/Sidebar";
import useHandleBackButton from "../hooks/useHandleBackButton";

export default function Dashboard(){

    const navigate = useNavigate();
    
    useHandleBackButton(navigate);

    const[hackathon, setHackathon] = useState([]);
    const[nome, setNome] = useState("");
    const[dataInizio, setDataInizio] = useState("");
    const[dataFine, setDataFine] = useState("");
    const[luogo, setLuogo] = useState("");
    const[premio, setPremio] = useState("");
    const[teamMin, setTeamMin] = useState("");
    const[teamMax, setTeamMax] = useState("");
    const[maxIscrizioni, setMaxIscrizioni] = useState("");
    const[regolamento, setRegolamento] = useState("");
    const[scadenzaIscrizioni, setScadenzaIscrizioni] = useState("");
    const[nomeGiudice, setNomeGiudice] = useState("");
    const[nomeMentori, setNomeMentori] = useState("");
    const[nomeTeam, setNomeTeam] = useState("");
    const [teams, setTeams] = useState([]);
    const [submitted, setSubmitted] = useState(false);

    const createHackathon = async(e) => {

        if (submitted) return;

        if(nome == "" || luogo ==  "" || premio ==  "" || dataInizio == null ||
        dataFine == null || !teamMin || !teamMax || !maxIscrizioni||regolamento ==  ""
        || scadenzaIscrizioni == null || nomeGiudice ==  "" || nomeMentori ==  ""){
            alert("Tutti i campi devono essere compilati per poter creare un'hackathon");
            setSubmitted(true);
            return;
        }

        if(teamMax < teamMin){
            alert("Il numero massimo di membri non può essere minore del numero minimo");
            return;
        }

        if(new Date(dataFine) <= new Date(dataInizio)){
            alert("La data di fine non può essere precedente o uguale alla data di inizio");
            return;
        }

        if(new Date(scadenzaIscrizioni) >= new Date(dataInizio)){
            alert("La data di scadenza delle iscrizioni deve essere precedente o uguale alla data di inizio");
            return;
        }

        setSubmitted(true);

        try {

        const token = localStorage.getItem("token");

        const response = await fetch(
            `${API_URL}/hackathon/crea`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body:JSON.stringify({
                    nome,
                    dataInizio,
                    dataFine,
                    luogo,
                    premio,
                    teamMin,
                    teamMax,
                    maxIscrizioni,
                    regolamento,
                    scadenzaIscrizioni,
                    nomeGiudice,
                    nomeMentori : [nomeMentori]
                })
            }
        );
        
        if(response.status === 401){
            localStorage.removeItem("token");
            navigate("/");
            return;
        }

        if (!response.ok) {
            const errorText = await response.text();
            alert("errore nella creazione");
            return;
        }

        alert("Hackathon creato!");
        caricaHackathon()

        } catch (err) {
            console.error(err);
            console.log("Errore nella creazione");
        } finally {
            setSubmitted(false);
        }
    };
    
    const caricaHackathon = () => {
    fetch(`${API_URL}/hackathon`, {
        method: "GET"
    })
    .then(res => res.json())
    .then(data => setHackathon(data))
    .catch(err => console.error(err));
    };

    useEffect(() => {
        caricaHackathon()
    }, []);

    useEffect(() => {
    const token = localStorage.getItem("token");

    fetch(`${API_URL}/team`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .catch(err => console.error(err));
    }, []);

    /*Funzione che mi ritorna alla pagina del login quando clicco esci*/ 
    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/", { replace: true });
    };

    return ( 
    <div className='dashboard'>
        {/* Colonna a sinistra che contiene il menu */} 
        <Sidebar />

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
                <h2 className='welcome-speech'>Benvenuto su Hackhub!</h2>
                <p className='welcome-speech'>Questa piattaforma ti permette di creare team e iscriverti con i tuoi amici a vari hackathon presenti in tutto il mondo e di gestirli nel modo più veloce e semplice possibile.</p>
            </div>

            <div className='center-box'>
                <h3 className='row-name'>Visualizza Hackathon</h3>
                <div className='hackathon-list'>
                {hackathon.map((h, index) => (
                    <div className='hackathon-card' key={index}>
                    <div className='card-header'></div>
                    <div className='card-body'>
                    <h4 className='nome-hackathon'>{h.nome}</h4>
                    <p className='data'> {h.dataInizio} - {h.dataFine} </p>
                    <p className='luogo'>{h.luogo}</p>
                            </div>
                        </div>
                ))}
                </div>
            </div>  
            </section>

            {/* box a destra */} 
            <aside className="widgets-column">
                <div className="widget-box">
                    <h2 className="row-name">Crea Hackathon</h2>
                    <div className="row">
                    <label>Nome:</label>
                    <input type="text" className="input-creation" placeholder="Nome Hackathon"
                    value={nome} onChange={(e) => setNome(e.target.value)}/></div>
                    <div className="row"><label>Data inizio: </label>
                    <input type="date" className="input-creation" placeholder="Data inizio"
                    value={dataInizio} onChange={(e) => setDataInizio(e.target.value)}/></div>
                    <div className="row"><label>Data fine: </label>
                    <input type="date" className="input-creation" placeholder="Data fine"
                    value={dataFine} onChange={(e) => setDataFine(e.target.value)}/></div>
                    <div className="row"><label>Luogo: </label>
                    <input type="text" className="input-creation" placeholder="luogo"
                    value={luogo} onChange={(e) => setLuogo(e.target.value)}/></div>
                    <div className="row"><label>Premio:</label>
                    <input type="number" className="input-creation"  placeholder="(in euro)"
                    value={premio} onChange={(e) => setPremio(e.target.value)}/></div>
                    <div className="row"><label>Numero minimo di partecipanti per team:</label>
                    <input type="number" className="input-creation" placeholder="da 3 a 6"
                    value={teamMin} onChange={(e) => setTeamMin(e.target.value)}/></div>
                    <div className="row"><label>Numero massimo di partecipanti per team:</label>
                    <input type="number" className="input-creation" placeholder="da 3 a 6"
                    value={teamMax} onChange={(e) => setTeamMax(e.target.value)}/></div>
                    <div className="row"><label>Numero massimo di iscrizioni:</label>
                    <input type="number" className="input-creation" placeholder="max 25"
                    value={maxIscrizioni} onChange={(e) => setMaxIscrizioni(e.target.value)}/></div>
                    <div className="row"><label>Regolamento:</label>
                    <input type="text" className="input-creation" placeholder="regole"
                    value={regolamento} onChange={(e) => setRegolamento(e.target.value)}/></div>
                    <div className="row"><label>Data di fine iscrizioni (precedente all'inizio): </label>
                    <input type="datetime-local" className="input-creation" placeholder="scadenzaIscrizioni"
                    value={scadenzaIscrizioni} onChange={(e) => setScadenzaIscrizioni(e.target.value)}/></div>
                    <div className="row"><label>Nome del giudice:</label>
                    <input type="text" className="input-creation" placeholder="solo un giudice"
                    value={nomeGiudice} onChange={(e) => setNomeGiudice(e.target.value)}/></div>
                    <div className="row"><label>Nome dei mentori:</label>
                    <input type="text" className="input-creation" placeholder="almeno un mentore"
                    value={nomeMentori} onChange={(e) => setNomeMentori(e.target.value)}/></div>
                    <button disabled = {submitted} className="other-button" onClick={createHackathon}>Crea Hackathon</button>
                </div>
            </aside>
        </main>
        </div>
    </div>
    );
}