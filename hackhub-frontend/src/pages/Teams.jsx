import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL } from "../config/api";
import Sidebar from "../components/Sidebar";
import useHandleBackButton from "../hooks/useHandleBackButton";

export default function Teams() {
    const navigate = useNavigate();
    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(true);

    useHandleBackButton(navigate);

    useEffect(() => {
        caricaTeam();
    }, []);

    const caricaTeam = () => {
        const token = localStorage.getItem("token");

        fetch(`${API_URL}/team`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(res => {
                if (res.status === 401) {
                    localStorage.removeItem("token");
                    navigate("/");
                    return;
                }
                return res.json();
            })
            .then(data => {
                if (data) {
                    setTeams(data);
                }
            })
            .catch(err => console.error(err))
            .finally(() => setLoading(false));
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/", { replace: true });
    };

    return (
        <div className='dashboard'>
            <Sidebar />

            <div className='content'>
                <header className='header'>
                    <input 
                        type='text' 
                        placeholder='Cerca Team' 
                        className='search-bar'
                    />
                </header>

                <main className='center'>
                    <section className='center-column'>
                        <div className='center-box'>
                            <h3 className='row-name'>Team presenti</h3>

                            {loading ? (
                                <p>Caricamento team in corso...</p>
                            ) : teams.length === 0 ? (
                                <p>Non ci sono team disponibili</p>
                            ) : (
                                <div className="team-list">
                                    {teams.map((team, index) => (
                                        <div className="team-card" key={index}>
                                            <h3 className="team-name">
                                                {team.nomeTeam}
                                            </h3>
                                            <div className="team-members">
                                                {team.membri && team.membri.map((membro, i) => (
                                                    <span className="member" key={i}>
                                                        {membro}
                                                    </span>
                                                ))}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </section>
                </main>
            </div>
        </div>
    );
}
