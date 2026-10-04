import {
    createContext,
    useContext,
    useEffect,
    useRef,
    useState,
} from "react";

import api, {
    setAccessToken,
    clearAccessToken,
} from "../services/api";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const initialized = useRef(false);

    const login = async (username, password) => {
        const response = await api.post("/auth/login", {
            username,
            password,
        });

        const { accessToken } = response.data;

        setAccessToken(accessToken);

        await loadCurrentUser();

        return response.data;
    };

    const loadCurrentUser = async () => {
        try {
            const response = await api.get("/me");
            setUser(response.data);
        } catch (error) {
            console.error("Failed to load current user:", error);
            clearAccessToken();
            setUser(null);
        }
    };

    const logout = async () => {
        try {
            await api.post("/auth/logout");
        } catch (error) {
            console.error("Logout request failed:", error);
        } finally {
            clearAccessToken();
            setUser(null);
        }
    };

    useEffect(() => {
        if (initialized.current) {
            return;
        }

        initialized.current = true;

        const initializeAuth = async () => {
            try {
                const response = await api.post("/auth/refresh");

                setAccessToken(response.data.accessToken);

                await loadCurrentUser();
            } catch (error) {
                clearAccessToken();
                setUser(null);
            } finally {
                setLoading(false);
            }
        };

        initializeAuth();
    }, []);

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                isAuthenticated: !!user,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth must be used inside AuthProvider"
        );
    }

    return context;
};