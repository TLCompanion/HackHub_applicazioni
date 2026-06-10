import React from "react";
import { useNavigate } from "react-router-dom";

export default function Login(){

  const navigate = useNavigate();

  {/* funzione che torna alla dashboard quando clicco esci*/} 
  const handleLogin = () => {
    navigate("/dashboard");
  };

  return (
    <div className='login-page'>
      <form>
        <input className='input-type' type='text' placeholder='username'/>
        <input className='input-type' type='text' placeholder='password'/>
        <button onClick={handleLogin} className='login-button'>Login</button>
      </form>
    </div>
  )
}
