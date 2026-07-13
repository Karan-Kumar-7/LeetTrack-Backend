import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";
import Dashboard from "../pages/dashboard/Dashboard";
import Problems from "../pages/problems/Problems.jsx";
import AddProblem from "../pages/problems/AddProblems";

function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>

                <Route path="/" element={<Navigate to="/login" />} />

                <Route path="/login" element={<Login />} />

                <Route path="/register" element={<Register />} />

                <Route path="/dashboard" element={<Dashboard />} />

                <Route path="/problems" element={<Problems />} />

                <Route path="/problems/add" element={<AddProblem />} />

            </Routes>
        </BrowserRouter>
    );
}


export default AppRoutes;