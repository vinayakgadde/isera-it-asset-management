import { useEffect, useState } from "react";
import {
    FaArrowLeft,
    FaEdit,
    FaLaptop,
} from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import "./AssetDetails.css";

const AssetDetails = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [asset, setAsset] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadAsset = async () => {
            try {
                const response =
                    await api.get(`/assets/${id}`);

                setAsset(response.data);
            } catch (error) {
                console.error(
                    "Failed to load asset:",
                    error
                );

                setError(
                    error.response?.data?.message ||
                    "Unable to load asset."
                );
            } finally {
                setLoading(false);
            }
        };

        loadAsset();
    }, [id]);

    const formatStatus = (value) => {
        if (!value) {
            return "-";
        }

        return value
            .replaceAll("_", " ")
            .toLowerCase()
            .replace(/\b\w/g, (letter) => letter.toUpperCase());
    };

    const formatDate = (value) => {
        if (!value) {
            return "-";
        }

        return new Date(value).toLocaleDateString();
    };

    const formatDateTime = (value) => {
        if (!value) {
            return "-";
        }

        return new Date(value).toLocaleString();
    };

    if (loading) {
        return (
            <div className="asset-details-loading">
                Loading asset...
            </div>
        );
    }

    if (error) {
        return (
            <div className="asset-details-error">
                {error}
            </div>
        );
    }

    if (!asset) {
        return (
            <div className="asset-details-error">
                Asset not found.
            </div>
        );
    }

    return (
        <div className="asset-details-page">

            <div className="asset-details-header">

                <div className="asset-details-title">

                    <div className="asset-details-icon">
                        <FaLaptop />
                    </div>

                    <div>
                        <h2>{asset.assetTag}</h2>

                        <p>
                            {asset.brand}{" "}
                            {asset.model || ""}
                        </p>
                    </div>

                </div>

                <div className="asset-details-actions">

                    <button
                        type="button"
                        className="asset-detail-back"
                        onClick={() =>
                            navigate("/assets")
                        }
                    >
                        <FaArrowLeft />
                        Back
                    </button>

                    <button
                        type="button"
                        className="asset-detail-edit"
                        onClick={() =>
                            navigate(`/assets/${id}/edit`)
                        }
                    >
                        <FaEdit />
                        Edit
                    </button>

                </div>

            </div>


            <div className="asset-details-card">

                <div className="asset-details-section">

                    <h4>Asset Information</h4>

                    <div className="asset-details-grid">

                        <div>
                            <span>Asset Tag</span>
                            <strong>{asset.assetTag}</strong>
                        </div>

                        <div>
                            <span>Serial Number</span>
                            <strong>
                                {asset.serialNumber || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Category</span>
                            <strong>
                                {asset.categoryName || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Vendor</span>
                            <strong>
                                {asset.vendorName || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Brand</span>
                            <strong>
                                {asset.brand || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Model</span>
                            <strong>
                                {asset.model || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Location</span>
                            <strong>
                                {asset.locationName || "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Status</span>
                            <strong>
                                {formatStatus(asset.status)}
                            </strong>
                        </div>

                        <div>
                            <span>Condition</span>
                            <strong>
                                {formatStatus(asset.condition)}
                            </strong>
                        </div>

                    </div>

                </div>


                <div className="asset-details-section">

                    <h4>Purchase & Warranty</h4>

                    <div className="asset-details-grid">

                        <div>
                            <span>Purchase Date</span>
                            <strong>
                                {formatDate(
                                    asset.purchaseDate
                                )}
                            </strong>
                        </div>

                        <div>
                            <span>Purchase Cost</span>
                            <strong>
                                {asset.purchaseCost != null
                                    ? asset.purchaseCost
                                    : "-"}
                            </strong>
                        </div>

                        <div>
                            <span>Warranty Expiry</span>
                            <strong>
                                {formatDate(
                                    asset.warrantyExpiryDate
                                )}
                            </strong>
                        </div>

                    </div>

                </div>


                <div className="asset-details-section">

                    <h4>Description</h4>

                    <div className="asset-description">
                        {asset.description || "No description available."}
                    </div>

                </div>


                <div className="asset-details-section">

                    <h4>System Information</h4>

                    <div className="asset-details-grid">

                        <div>
                            <span>Asset ID</span>
                            <strong>
                                {asset.id}
                            </strong>
                        </div>

                        <div>
                            <span>Created At</span>
                            <strong>
                                {formatDateTime(
                                    asset.createdAt
                                )}
                            </strong>
                        </div>

                        <div>
                            <span>Updated At</span>
                            <strong>
                                {formatDateTime(
                                    asset.updatedAt
                                )}
                            </strong>
                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
};

export default AssetDetails;