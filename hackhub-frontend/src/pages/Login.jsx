import React, {useState} from "react";
import { useNavigate } from "react-router-dom";

export default function Login(){

  const navigate = useNavigate();

  const[username, setUsername] = useState("");
  const[password, setPassword] = useState("");
  const[email, setEmail] = useState("");
  const[errore, setErrore] = useState("");
  const[isLogin, setIsLogin] = useState(false);
  
  {/* funzione che torna alla dashboard quando clicco esci*/} 
  const handleLogin = async (e) => {
    e.preventDefault();
    setErrore("");
    
    const endpoint = isLogin ? "http://localhost:8081/api/autenticazione/accesso" 
    : "http://localhost:8081/api/autenticazione/registrazione";

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nomeUtente: username,
          email: email,
          password: password,
        }),
      });

      if(!response.ok){
        throw new Error(isLogin? "Credenziali non valide" : "Registrazione non riuscita");
      }

      if (!isLogin) {
      alert("Registrazione completata!");

      setUsername("");
      setPassword("");
      setEmail("");

      setIsLogin(true);

      return;
      }
      const data = await response.json();

      const token = data.token || data.jwt;

      if (token) {
      localStorage.setItem("token", token);
      navigate("/dashboard");
      } else {
      setErrore("Token non ricevuto dal server");
      }

    } catch (err) {
      setErrore(err.message);
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
        {!isLogin && (
        <input
          className="input-type"
          type="email"
          placeholder="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        )}
        <input 
        className='input-type' 
        type='text' 
        placeholder='password'
        value={password}
        onChange={(e) => setPassword(e.target.value)}/>
        <button type="submit" className='main-button'>{isLogin? "Login" : "Registrati"}</button>

        <div className="container-switch">
        <p className="switch-mode">
        {isLogin ? "Non hai un account? " : "Hai già un account? "}

        <button
          className="other-button"
          type="button"
          onClick={() => setIsLogin(!isLogin)}>
          {isLogin ? "Registrati" : "Accedi"}
        </button>
        </p>
        </div>
      </form>
    </div>
  )
}
