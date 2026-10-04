import { useEffect, useState } from "react";
import {
    FaLaptop,
    FaCheckCircle,
    FaUserCheck,
    FaTools,
    FaArrowUp,
    FaArrowDown,
} from "react-icons/fa";
import api from "../services/api";
import "./Dashboard.css";

const Dashboard = () => {

    const [dashboardData, setDashboardData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadDashboard = async () => {
            try {
                const response = await api.get("/dashboard/summary");

                setDashboardData(response.data);

            } catch (error) {
                console.error(
                    "Failed to load dashboard:",
                    error
                );

                setError(
                    "Unable to load dashboard data."
                );

            } finally {
                setLoading(false);
            }
        };

        loadDashboard();

    }, []);


    if (loading) {
        return (
            <div className="dashboard-loading">
                Loading dashboard...
            </div>
        );
    }


    if (error) {
        return (
            <div className="dashboard-error">
                {error}
            </div>
        );
    }


    const assetsByStatus =
        dashboardData?.assetsByStatus || {};


    const getStatusCount = (status) => {

        const key = Object.keys(assetsByStatus)
            .find(
                currentStatus =>
                    currentStatus.toUpperCase() ===
                    status.toUpperCase()
            );

        return key
            ? assetsByStatus[key]
            : 0;
    };


    const totalAssets =
        dashboardData?.totalAssets || 0;

    const availableAssets =
        getStatusCount("AVAILABLE");

    const assignedAssets =
        getStatusCount("ASSIGNED");

    const assetsUnderMaintenance =
        dashboardData?.assetsUnderMaintenance || 0;


    return (
        <div className="dashboard-page">

            {/* Page Header */}

            <div className="dashboard-header">

                <div>
                    <h2>Dashboard</h2>

                    <p>
                        Overview of your IT assets and asset
                        management activities.
                    </p>
                </div>

                <div className="dashboard-date">
                    IT Asset Management
                </div>

            </div>


            {/* Statistics Cards */}

            <div className="dashboard-stats">

                {/* Total Assets */}

                <div className="stat-card">

                    <div className="stat-icon">
                        <FaLaptop />
                    </div>

                    <div className="stat-content">

                        <span className="stat-label">
                            Total Assets
                        </span>

                        <h3>
                            {totalAssets}
                        </h3>

                    </div>

                </div>


                {/* Available Assets */}

                <div className="stat-card">

                    <div className="stat-icon available">
                        <FaCheckCircle />
                    </div>

                    <div className="stat-content">

                        <span className="stat-label">
                            Available Assets
                        </span>

                        <h3>
                            {availableAssets}
                        </h3>

                    </div>

                </div>


                {/* Assigned Assets */}

                <div className="stat-card">

                    <div className="stat-icon assigned">
                        <FaUserCheck />
                    </div>

                    <div className="stat-content">

                        <span className="stat-label">
                            Assigned Assets
                        </span>

                        <h3>
                            {assignedAssets}
                        </h3>

                    </div>

                </div>


                {/* Maintenance */}

                <div className="stat-card">

                    <div className="stat-icon maintenance">
                        <FaTools />
                    </div>

                    <div className="stat-content">

                        <span className="stat-label">
                            Under Maintenance
                        </span>

                        <h3>
                            {assetsUnderMaintenance}
                        </h3>

                    </div>

                </div>

            </div>


            {/* Asset Status */}

            <div className="dashboard-grid">

                <div className="dashboard-card">

                    <div className="card-header">

                        <div>

                            <h5>
                                Asset Status
                            </h5>

                            <p>
                                Current asset distribution
                            </p>

                        </div>

                    </div>


                    <div className="asset-status">

                        <div className="status-row">

                            <div className="status-info">

                                <span className="status-dot available-dot"></span>

                                <span>
                                    Available
                                </span>

                            </div>

                            <strong>
                                {availableAssets}
                            </strong>

                        </div>


                        <div className="status-bar">

                            <div
                                className="status-progress available-progress"
                                style={{
                                    width:
                                        totalAssets > 0
                                            ? `${(availableAssets / totalAssets) * 100}%`
                                            : "0%"
                                }}
                            ></div>

                        </div>


                        <div className="status-row">

                            <div className="status-info">

                                <span className="status-dot assigned-dot"></span>

                                <span>
                                    Assigned
                                </span>

                            </div>

                            <strong>
                                {assignedAssets}
                            </strong>

                        </div>


                        <div className="status-bar">

                            <div
                                className="status-progress assigned-progress"
                                style={{
                                    width:
                                        totalAssets > 0
                                            ? `${(assignedAssets / totalAssets) * 100}%`
                                            : "0%"
                                }}
                            ></div>

                        </div>


                        <div className="status-row">

                            <div className="status-info">

                                <span className="status-dot maintenance-dot"></span>

                                <span>
                                    Maintenance
                                </span>

                            </div>

                            <strong>
                                {assetsUnderMaintenance}
                            </strong>

                        </div>


                        <div className="status-bar">

                            <div
                                className="status-progress maintenance-progress"
                                style={{
                                    width:
                                        totalAssets > 0
                                            ? `${(assetsUnderMaintenance / totalAssets) * 100}%`
                                            : "0%"
                                }}
                            ></div>

                        </div>

                    </div>

                </div>


                {/* Warranty */}

                <div className="dashboard-card">

                    <div className="card-header">

                        <div>

                            <h5>
                                Warranty Overview
                            </h5>

                            <p>
                                Assets with warranty expiring
                                within 30 days
                            </p>

                        </div>

                    </div>


                    <div className="warranty-summary">

                        <div className="warranty-number">
                            {dashboardData?.warrantyExpiringAssets || 0}
                        </div>

                        <div className="warranty-label">
                            Assets require attention
                        </div>

                    </div>

                </div>

            </div>


            {/* Recently Assigned */}

            <div className="dashboard-card recent-assets-card">

                <div className="card-header">

                    <div>

                        <h5>
                            Recently Assigned Assets
                        </h5>

                        <p>
                            Latest asset assignments
                        </p>

                    </div>

                </div>


                <div className="table-responsive">

                    <table className="assets-table">

                        <thead>

                        <tr>

                            <th>
                                Asset Tag
                            </th>

                            <th>
                                Employee
                            </th>

                            <th>
                                Assigned Date
                            </th>

                            <th>
                                Status
                            </th>

                        </tr>

                        </thead>


                        <tbody>

                        {dashboardData?.recentlyAssignedAssets?.length > 0 ? (

                            dashboardData.recentlyAssignedAssets.map(
                                (activity) => (

                                    <tr
                                        key={
                                            activity.assignmentId
                                        }
                                    >

                                        <td>
                                            {activity.assetTag}
                                        </td>

                                        <td>
                                            {activity.employeeName}
                                        </td>

                                        <td>
                                            {activity.assignedDate}
                                        </td>

                                        <td>

                                                <span className="table-status assigned-status">
                                                    {activity.status}
                                                </span>

                                        </td>

                                    </tr>

                                )
                            )

                        ) : (

                            <tr>

                                <td
                                    colSpan="4"
                                    className="empty-table"
                                >
                                    No recent assignments
                                </td>

                            </tr>

                        )}

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
    );
};

export default Dashboard;