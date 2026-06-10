import React, {useState} from "react";
import { useNavigate } from "react-router-dom";

export default function Login(){

  const navigate = useNavigate();

  const[username, setUsername] = useState("");
  const[password, setPassword] = useState("");
  const[errore, setErrore] = useState("");
  
  {/* funzione che torna alla dashboard quando clicco esci*/} 
  const handleLogin = async (e) => {
    e.preventDefault();
    setErrore("");
    
    try {
      const response = await fetch("http://localhost:8081/api/autenticazione/accesso", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nomeUtente: username,
          password: password,
        }),
      });

      if(!response.ok){
        throw new Error("Credenziali non valide");
      }

      const data = await response.json();

      const token = data.token || data.jwt;

      if(token){
        localStorage.setItem("token", token);

        navigate("/dashboard");
      } else {
        setErrore("Token non ricevuto dal server");
      }
    } catch (err) {
      setErrore(err.message || "Impossibile connettersi al server");
    }
  };

  return (
    <div className='login-page'>
      <form onSubmit={handleLogin}>
        <input 
        className='input-type' 
        type='text' 
        placeholder='username'
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        />
        <input 
        className='input-type' 
        type='text' 
        placeholder='password'
        value={password}
        onChange={(e) => setPassword(e.target.value)}/>
        <button type="submit" className='login-button'>Login</button>
      </form>
    </div>
  )
}
