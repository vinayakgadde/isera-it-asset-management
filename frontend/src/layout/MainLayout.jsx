import { Outlet, NavLink } from "react-router-dom";
import {
    FaTachometerAlt,
    FaLaptop,
    FaUsers,
    FaClipboardList,
    FaTools,
    FaHistory,
    FaUserCog,
    FaSignOutAlt,
} from "react-icons/fa";
import { useAuth } from "../context/AuthContext";
import "./MainLayout.css";

const MainLayout = () => {
    const { user, logout } = useAuth();

    const handleLogout = async () => {
        await logout();
    };

    return (
        <div className="app-layout">

            {/* Sidebar */}
            <aside className="sidebar">

                <div className="sidebar-brand">
                    <div className="brand-title">
                        ISera
                    </div>

                    <div className="brand-subtitle">
                        IT Asset Management
                    </div>
                </div>

                <nav className="sidebar-menu">

                    <NavLink
                        to="/dashboard"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaTachometerAlt />
                        <span>Dashboard</span>
                    </NavLink>

                    <NavLink
                        to="/assets"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaLaptop />
                        <span>Assets</span>
                    </NavLink>

                    <NavLink
                        to="/employees"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaUsers />
                        <span>Employees</span>
                    </NavLink>

                    <NavLink
                        to="/assignments"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaClipboardList />
                        <span>Assignments</span>
                    </NavLink>

                    <NavLink
                        to="/maintenance"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaTools />
                        <span>Maintenance</span>
                    </NavLink>

                    <NavLink
                        to="/audit-logs"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaHistory />
                        <span>Audit Logs</span>
                    </NavLink>

                    <NavLink
                        to="/users"
                        className={({ isActive }) =>
                            isActive
                                ? "sidebar-link active"
                                : "sidebar-link"
                        }
                    >
                        <FaUserCog />
                        <span>Users</span>
                    </NavLink>

                </nav>

                <div className="sidebar-footer">

                    <button
                        className="logout-button"
                        onClick={handleLogout}
                    >
                        <FaSignOutAlt />
                        <span>Logout</span>
                    </button>

                </div>

            </aside>

            {/* Main Area */}
            <div className="main-area">

                {/* Topbar */}
                <header className="topbar">

                    <div>
                        <h5 className="topbar-title">
                            IT Asset Management
                        </h5>
                    </div>

                    <div className="user-info">

                        <div className="user-avatar">
                            {user?.username?.charAt(0)?.toUpperCase() || "U"}
                        </div>

                        <div className="user-details">
                            <div className="username">
                                {user?.username || "User"}
                            </div>

                            <div className="user-role">
                                {user?.role || "User"}
                            </div>
                        </div>

                    </div>

                </header>

                {/* Page Content */}
                <main className="page-content">
                    <Outlet />
                </main>

            </div>

        </div>
    );
};

export default MainLayout;