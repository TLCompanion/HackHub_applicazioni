import { BrowserRouter, Routes, Route } from "react-router-dom";
import React from "react";
import Login from "./pages/Login";       
import Dashboard from "./pages/Dashboard"; 
import "./styles/global.css";
import "./styles/login.css";
import "./styles/dashboard.css";
import "./styles/hackathon.css";
import "./styles/team.css";
import "./styles/responsive.css";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}