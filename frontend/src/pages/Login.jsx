import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaLock, FaUser, FaServer } from "react-icons/fa";
import { useAuth } from "../context/AuthContext";
import "./Login.css";

const Login = () => {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError("");

        if (!formData.username.trim() || !formData.password.trim()) {
            setError("Username and password are required.");
            return;
        }

        try {
            setLoading(true);

            await login(formData.username, formData.password);

            navigate("/dashboard");
        } catch (error) {
            console.error("Login failed:", error);

            const message =
                error.response?.data?.message ||
                "Invalid username or password.";

            setError(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <div className="login-card">

                <div className="login-brand">
                    <div className="brand-icon">
                        <FaServer />
                    </div>

                    <h1>ISera Biological</h1>
                    <p>IT Asset Management System</p>
                </div>

                <div className="login-content">

                    <h2>Welcome back</h2>
                    <p className="login-subtitle">
                        Sign in to access the asset management portal.
                    </p>

                    {error && (
                        <div className="login-error">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>

                        <div className="form-group">
                            <label htmlFor="username">
                                Username
                            </label>

                            <div className="input-wrapper">
                                <FaUser className="input-icon" />

                                <input
                                    type="text"
                                    id="username"
                                    name="username"
                                    value={formData.username}
                                    onChange={handleChange}
                                    placeholder="Enter your username"
                                    autoComplete="username"
                                    disabled={loading}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">
                                Password
                            </label>

                            <div className="input-wrapper">
                                <FaLock className="input-icon" />

                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    placeholder="Enter your password"
                                    autoComplete="current-password"
                                    disabled={loading}
                                />
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="login-button"
                            disabled={loading}
                        >
                            {loading ? "Signing in..." : "Sign In"}
                        </button>

                    </form>

                    <div className="login-footer">
                        <span>Secure access</span>
                        <span>•</span>
                        <span>JWT Authentication</span>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default Login;