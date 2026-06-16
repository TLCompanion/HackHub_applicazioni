import React from "react";
import { useNavigate } from "react-router-dom";

export default function Sidebar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/", { replace: true });
    };

    const handleNavigate = (page) => {
        if (page === "dashboard") {
            navigate("/dashboard");
        } else if (page === "teams") {
            navigate("/teams");
        } else if (page === "hackathon") {
            navigate("/hackathon");
        }
    };

    return (
        <aside className='sidebar'>
            <div className='logo'>HACKHUB</div>

            <nav className='navigation'>
                <p className='group-title'>NAVIGAZIONE</p>
                <ul>
                    <button 
                        className='side-menu'
                        onClick={() => handleNavigate("dashboard")}
                    >
                        Dashboard
                    </button>
                    <button 
                        className='side-menu'
                        onClick={() => handleNavigate("hackathon")}
                    >
                        Hackathon
                    </button>
                    <button 
                        className='side-menu'
                        onClick={() => handleNavigate("teams")}
                    >
                        visualizza team
                    </button>
                    <button className="side-menu">
                        Sottomissioni
                    </button>
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
    );
}
