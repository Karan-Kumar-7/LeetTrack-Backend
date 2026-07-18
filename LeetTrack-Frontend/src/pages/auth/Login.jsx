import { FaEnvelope, FaLock } from "react-icons/fa";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

import { login as loginUser } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";

function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm();

    const onSubmit = async (data) => {
        try {
            const response = await loginUser(data);

            login(response.data.token);

            toast.success("Login successful!");

            navigate("/dashboard");
        } catch (error) {
            toast.error(
                error.response?.data?.message || "Invalid email or password"
            );
        }
    };

    return (
        <div className="min-h-screen bg-slate-900 flex items-center justify-center px-4">
            <div className="w-full max-w-md bg-slate-800 rounded-2xl shadow-2xl p-8">

                <h1 className="text-3xl font-bold text-white text-center">
                    Welcome Back
                </h1>

                <p className="text-slate-400 text-center mt-2">
                    Login to continue using LeetTrack
                </p>

                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="mt-8 space-y-5"
                >

                    {/* Email */}

                    <div>
                        <label className="text-slate-300 block mb-2">
                            Email
                        </label>

                        <div className="flex items-center bg-slate-700 rounded-lg px-3">

                            <FaEnvelope className="text-slate-400" />

                            <input
                                type="email"
                                placeholder="Enter your email"
                                className="w-full bg-transparent outline-none px-3 py-3 text-white"
                                {...register("email", {
                                    required: "Email is required",
                                })}
                            />
                        </div>

                        {errors.email && (
                            <p className="text-red-400 text-sm mt-1">
                                {errors.email.message}
                            </p>
                        )}
                    </div>

                    {/* Password */}

                    <div>
                        <label className="text-slate-300 block mb-2">
                            Password
                        </label>

                        <div className="flex items-center bg-slate-700 rounded-lg px-3">

                            <FaLock className="text-slate-400" />

                            <input
                                type="password"
                                placeholder="Enter your password"
                                className="w-full bg-transparent outline-none px-3 py-3 text-white"
                                {...register("password", {
                                    required: "Password is required",
                                })}
                            />
                        </div>

                        {errors.password && (
                            <p className="text-red-400 text-sm mt-1">
                                {errors.password.message}
                            </p>
                        )}
                    </div>

                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full bg-cyan-500 hover:bg-cyan-600 transition rounded-lg py-3 text-white font-semibold disabled:opacity-50"
                    >
                        {isSubmitting ? "Logging in..." : "Login"}
                    </button>

                </form>

            </div>
        </div>
    );
}

export default Login;