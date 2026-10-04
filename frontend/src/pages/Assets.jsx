import { useEffect, useMemo, useState } from "react";
import {
    FaSearch,
    FaSyncAlt,
    FaLaptop,
    FaEye,
    FaEdit,
    FaTrash,
    FaPlus,
} from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Assets.css";

const Assets = () => {
    const navigate = useNavigate();

    const [assets, setAssets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [searchTerm, setSearchTerm] = useState("");
    const [statusFilter, setStatusFilter] = useState("ALL");

    const loadAssets = async () => {
        try {
            setLoading(true);
            setError("");

            const response = await api.get("/assets");

            setAssets(response.data || []);
        } catch (error) {
            console.error("Failed to load assets:", error);

            setError(
                error.response?.data?.message ||
                "Unable to load assets."
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadAssets();
    }, []);

    const handleDelete = async (asset) => {
        const confirmed = window.confirm(
            `Are you sure you want to delete asset "${asset.assetTag}"?`
        );

        if (!confirmed) {
            return;
        }

        try {
            setError("");

            await api.delete(
                `/assets/${asset.id}`
            );

            setAssets((currentAssets) =>
                currentAssets.filter(
                    (item) => item.id !== asset.id
                )
            );
        } catch (error) {
            console.error(
                "Failed to delete asset:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Unable to delete asset."
            );
        }
    };

    const statusOptions = useMemo(() => {
        const statuses = assets
            .map((asset) => asset.status)
            .filter(Boolean);

        return [...new Set(statuses)];
    }, [assets]);

    const filteredAssets = useMemo(() => {
        const search = searchTerm.trim().toLowerCase();

        return assets.filter((asset) => {
            const matchesSearch =
                !search ||
                asset.assetTag?.toLowerCase().includes(search) ||
                asset.serialNumber?.toLowerCase().includes(search) ||
                asset.brand?.toLowerCase().includes(search) ||
                asset.model?.toLowerCase().includes(search) ||
                asset.categoryName?.toLowerCase().includes(search) ||
                asset.vendorName?.toLowerCase().includes(search) ||
                asset.locationName?.toLowerCase().includes(search);

            const matchesStatus =
                statusFilter === "ALL" ||
                asset.status === statusFilter;

            return matchesSearch && matchesStatus;
        });
    }, [assets, searchTerm, statusFilter]);

    const formatStatus = (status) => {
        if (!status) {
            return "-";
        }

        return status
            .replaceAll("_", " ")
            .toLowerCase()
            .replace(/\b\w/g, (letter) => letter.toUpperCase());
    };

    const formatDate = (date) => {
        if (!date) {
            return "-";
        }

        return new Date(date).toLocaleDateString();
    };

    const getStatusClass = (status) => {
        if (!status) {
            return "";
        }

        return status
            .toLowerCase()
            .replaceAll("_", "-");
    };

    return (
        <div className="assets-page">

            <div className="assets-header">

                <div className="assets-title-row">

                    <div className="assets-page-icon">
                        <FaLaptop />
                    </div>

                    <div>
                        <h2>Assets</h2>

                        <p>
                            Manage and monitor all IT assets.
                        </p>
                    </div>

                </div>

                <div className="assets-header-actions">

                    <button
                        type="button"
                        className="assets-refresh-button"
                        onClick={loadAssets}
                        disabled={loading}
                    >
                        <FaSyncAlt
                            className={
                                loading ? "spin" : ""
                            }
                        />

                        Refresh
                    </button>

                    <button
                        type="button"
                        className="assets-add-button"
                        onClick={() =>
                            navigate("/assets/new")
                        }
                    >
                        <FaPlus />
                        Add Asset
                    </button>

                </div>

            </div>


            <div className="assets-filter-card">

                <div className="assets-search-box">

                    <FaSearch />

                    <input
                        type="text"
                        placeholder="Search by asset tag, serial number, brand, model..."
                        value={searchTerm}
                        onChange={(event) =>
                            setSearchTerm(
                                event.target.value
                            )
                        }
                    />

                </div>

                <div className="assets-status-filter">

                    <label htmlFor="statusFilter">
                        Status
                    </label>

                    <select
                        id="statusFilter"
                        value={statusFilter}
                        onChange={(event) =>
                            setStatusFilter(
                                event.target.value
                            )
                        }
                    >
                        <option value="ALL">
                            All Statuses
                        </option>

                        {statusOptions.map((status) => (
                            <option
                                key={status}
                                value={status}
                            >
                                {formatStatus(status)}
                            </option>
                        ))}
                    </select>

                </div>

            </div>


            <div className="assets-summary">

                <span>
                    Total assets:
                    <strong>{assets.length}</strong>
                </span>

                <span>
                    Showing:
                    <strong>
                        {filteredAssets.length}
                    </strong>
                </span>

            </div>


            {error && (
                <div className="assets-error">
                    {error}
                </div>
            )}


            {loading && (
                <div className="assets-loading">
                    Loading assets...
                </div>
            )}


            {!loading &&
                !error &&
                filteredAssets.length === 0 && (
                    <div className="assets-empty">

                        <FaLaptop />

                        <h4>
                            No assets found
                        </h4>

                        <p>
                            Try changing your search or status filter.
                        </p>

                    </div>
                )}


            {!loading &&
                filteredAssets.length > 0 && (

                    <div className="assets-table-card">

                        <div className="table-responsive">

                            <table className="assets-list-table">

                                <thead>

                                <tr>
                                    <th>Asset Tag</th>
                                    <th>Asset</th>
                                    <th>Category</th>
                                    <th>Vendor</th>
                                    <th>Location</th>
                                    <th>Status</th>
                                    <th>Condition</th>
                                    <th>Purchase Date</th>
                                    <th>Actions</th>
                                </tr>

                                </thead>

                                <tbody>

                                {filteredAssets.map(
                                    (asset) => (

                                        <tr key={asset.id}>

                                            <td>
                                                <strong>
                                                    {asset.assetTag}
                                                </strong>

                                                <small>
                                                    ID: {asset.id}
                                                </small>
                                            </td>

                                            <td>

                                                <div className="asset-name-cell">

                                                        <span className="asset-mini-icon">
                                                            <FaLaptop />
                                                        </span>

                                                    <div>

                                                        <strong>
                                                            {asset.brand || "-"}
                                                        </strong>

                                                        <span>
                                                                {asset.model || "-"}
                                                            </span>

                                                    </div>

                                                </div>

                                            </td>

                                            <td>
                                                {asset.categoryName || "-"}
                                            </td>

                                            <td>
                                                {asset.vendorName || "-"}
                                            </td>

                                            <td>
                                                {asset.locationName || "-"}
                                            </td>

                                            <td>

                                                    <span
                                                        className={`asset-status-badge ${getStatusClass(
                                                            asset.status
                                                        )}`}
                                                    >
                                                        {formatStatus(
                                                            asset.status
                                                        )}
                                                    </span>

                                            </td>

                                            <td>

                                                    <span className="asset-condition">
                                                        {formatStatus(
                                                            asset.condition
                                                        )}
                                                    </span>

                                            </td>

                                            <td>
                                                {formatDate(
                                                    asset.purchaseDate
                                                )}
                                            </td>

                                            <td>

                                                <div className="asset-actions">

                                                    <button
                                                        type="button"
                                                        className="asset-action-button view"
                                                        title="View asset"
                                                        onClick={() =>
                                                            navigate(
                                                                `/assets/${asset.id}`
                                                            )
                                                        }
                                                    >
                                                        <FaEye />
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="asset-action-button edit"
                                                        title="Edit asset"
                                                        onClick={() =>
                                                            navigate(
                                                                `/assets/${asset.id}/edit`
                                                            )
                                                        }
                                                    >
                                                        <FaEdit />
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="asset-action-button delete"
                                                        title="Delete asset"
                                                        onClick={() =>
                                                            handleDelete(
                                                                asset
                                                            )
                                                        }
                                                    >
                                                        <FaTrash />
                                                    </button>

                                                </div>

                                            </td>

                                        </tr>

                                    )
                                )}

                                </tbody>

                            </table>

                        </div>

                    </div>

                )}

        </div>
    );
};

export default Assets;