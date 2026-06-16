import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../config/api";
import Sidebar from "../components/Sidebar";
import useHandleBackButton from "../hooks/useHandleBackButton";

export default function Hackathon() {

const navigate = useNavigate();

const [hackathons, setHackathons] = useState([]);
const [selectedHackathon, setSelectedHackathon] = useState(null);
const [loading, setLoading] = useState(true);

useHandleBackButton(navigate);

useEffect(() => {
    caricaHackathon();
}, []);

const caricaHackathon = () => {

    fetch(`${API_URL}/hackathon`, {
        method: "GET"
    })
        .then(res => res.json())
        .then(data => {
            console.log("Hackathon data:", data);
            setHackathons(data);

            if (data.length > 0) {
                console.log("First hackathon:", data[0]);
                setSelectedHackathon(data[0]);
            }
        })
        .catch(err => console.error(err))
        .finally(() => setLoading(false));
};

return (
    <div className="dashboard">

        <Sidebar />

        <div className="content-hackathon">

            <main className="center-hackathon">

                <section className="center-column">

                    <div className="center-box">

                        <h3 className="row-name">
                            Dettagli Hackathon
                        </h3>

                        {loading ? (

                            <p>Caricamento hackathon...</p>

                        ) : hackathons.length === 0 ? (

                            <p>Nessun hackathon disponibile</p>

                        ) : (

                            <>
                                <select
                                    className="hackathon-selector"
                                    value={selectedHackathon?.nome || ""}
                                    onChange={(e) => {

                                        const hackathon =
                                            hackathons.find(
                                                h => h.nome === e.target.value
                                            );

                                        setSelectedHackathon(hackathon);
                                    }}
                                >

                                    {hackathons.map((hackathon, index) => (
                                        <option
                                            key={index}
                                            value={hackathon.nome}
                                        >
                                            {hackathon.nome}
                                        </option>
                                    ))}

                                </select>

                                {selectedHackathon && (

                                    <div className="hackathon-detail-card">

                                        <div className="card-header"></div>

                                        <div className="card-body">

                                            <h4 className="nome-hackathon">
                                                {selectedHackathon.nome}
                                            </h4>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Data inizio: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.dataInizio}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Data fine: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.dataFine}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Luogo: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.luogo}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Premio: 
                                                </span>
                                                <span className="detail-value">
                                                    € {selectedHackathon.premio}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Team min: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.teamMin}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Team max: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.teamMax}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Max iscrizioni: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.maxIscrizioni}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Scadenza iscrizioni: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.scadenzaIscrizioni}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="detail-label">
                                                    Regolamento: 
                                                </span>
                                                <span className="detail-value">
                                                    {selectedHackathon.regolamento}
                                                </span>
                                            </div>

                                        </div>

                                    </div>

                                )}

                            </>
                        )}

                    </div>

                </section>

            </main>

        </div>

    </div>
);

}
