import { BrowserRouter, Routes, Route } from "react-router-dom";
import React from "react";
import Login from "./pages/Login";       
import Dashboard from "./pages/Dashboard";
import Teams from "./pages/Teams";
import Hackathon from "./pages/Hackathon";
import "./style/global.css";
import "./style/login.css";
import "./style/dashboard.css";
import "./style/hackathon.css";
import "./style/team.css";
import "./style/responsive.css";
import { Navigate } from "react-router-dom";

function ProtectedRoute({children}) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/" replace/>;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/teams" element={<ProtectedRoute><Teams /></ProtectedRoute>} />
        <Route path="/hackathon" element={<ProtectedRoute><Hackathon /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  );
}