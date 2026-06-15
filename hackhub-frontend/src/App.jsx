import { BrowserRouter, Routes, Route } from "react-router-dom";
import React from "react";
import Login from "./pages/Login";       
import Dashboard from "./pages/Dashboard"; 
import "./style/global.css";
import "./style/login.css";
import "./style/dashboard.css";
import "./style/hackathon.css";
import "./style/team.css";
import "./style/responsive.css";

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