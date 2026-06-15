import React, {useState} from "react";
import { API_URL } from "../config/api";
import { useNavigate } from "react-router-dom";

export default function Login(){

  const navigate = useNavigate();

  const[username, setUsername] = useState("");
  const[password, setPassword] = useState("");
  const[email, setEmail] = useState("");
  const[errore, setErrore] = useState("");
  const[isLogin, setIsLogin] = useState(false);
  const[submitted, setSubmitted] = useState(false);
  
  {/* funzione che gestisce la registrazione e il login. Se l'utente è in fase di registrazione
    il campo per l'email è attivo. Una volta completata la registrazione si passa in allo stato login
    con username e password*/} 
  const handleSubmit = async (e) => {

    if (submitted) return;
    setSubmitted(true);

    if(password.length < 6){
      alert("La password deve essere composta da almeno 6 caratteri");
      return;
    }

    /*Per evitare che il browser ogni volta mi resetti il form*/
    e.preventDefault();
    setErrore("");
    
    const endpoint = isLogin ? `${API_URL}/autenticazione/accesso` 
    : `${API_URL}/autenticazione/registrazione`;

    console.log("Endpoint:", endpoint);
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
        alert(isLogin? "Credenziali non valide" : "Registrazione non riuscita");
        return;
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
    } finally {
      setSubmitted(false);
    }
  };

  return (
    <div className='login-page'>
      <form onSubmit={handleSubmit}>
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
        <button disabled={submitted} type="submit" className='main-button'>{isLogin? "Login" : "Registrati"}</button>

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
