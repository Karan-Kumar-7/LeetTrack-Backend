import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {

    const navigate = useNavigate();
    const { logout } = useAuth();

    const handleLogout = () => {

        logout();
        navigate("/login");

    };

    return (

        <nav className="bg-slate-800 shadow-lg">

            <div className="max-w-7xl mx-auto px-8 py-4 flex justify-between items-center">

                {/* Logo */}

                <h1 className="text-3xl font-bold text-cyan-400">
                    LeetTrack
                </h1>

                {/* Navigation */}

                <div className="flex items-center gap-8">

                    <NavLink
                        to="/dashboard"
                        className={({ isActive }) =>
                            isActive
                                ? "text-cyan-400 font-semibold"
                                : "text-white hover:text-cyan-400 transition"
                        }
                    >
                        Dashboard
                    </NavLink>

                    <NavLink
                        to="/problems"
                        className={({ isActive }) =>
                            isActive
                                ? "text-cyan-400 font-semibold"
                                : "text-white hover:text-cyan-400 transition"
                        }
                    >
                        Problems
                    </NavLink>

                    <button
                        onClick={handleLogout}
                        className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg text-white font-semibold transition"
                    >
                        Logout
                    </button>

                </div>

            </div>

        </nav>

    );
}

export default Navbar;