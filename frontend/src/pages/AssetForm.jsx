import { useEffect, useState } from "react";
import { FaArrowLeft, FaSave } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import "./AssetForm.css";

const initialForm = {
    assetTag: "",
    serialNumber: "",
    categoryId: "",
    vendorId: "",
    brand: "",
    model: "",
    purchaseDate: "",
    purchaseCost: "",
    warrantyExpiryDate: "",
    locationId: "",
    status: "IN_STOCK",
    condition: "GOOD",
    description: "",
};

const AssetForm = () => {
    const navigate = useNavigate();
    const { id } = useParams();

    const isEditMode = Boolean(id);

    const [form, setForm] = useState(initialForm);

    const [categories, setCategories] = useState([]);
    const [vendors, setVendors] = useState([]);
    const [locations, setLocations] = useState([]);

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadFormData = async () => {
            try {
                setLoading(true);
                setError("");

                const [
                    categoryResponse,
                    vendorResponse,
                    locationResponse,
                ] = await Promise.all([
                    api.get("/asset-categories"),
                    api.get("/vendors"),
                    api.get("/locations"),
                ]);

                setCategories(
                    (categoryResponse.data || []).filter(
                        (item) => item.active !== false
                    )
                );

                setVendors(
                    (vendorResponse.data || []).filter(
                        (item) => item.active !== false
                    )
                );

                setLocations(
                    (locationResponse.data || []).filter(
                        (item) => item.active !== false
                    )
                );

                if (isEditMode) {
                    const assetResponse =
                        await api.get(`/assets/${id}`);

                    const asset = assetResponse.data;

                    setForm({
                        assetTag: asset.assetTag || "",
                        serialNumber: asset.serialNumber || "",
                        categoryId:
                            asset.categoryId?.toString() || "",
                        vendorId:
                            asset.vendorId?.toString() || "",
                        brand: asset.brand || "",
                        model: asset.model || "",
                        purchaseDate:
                            asset.purchaseDate || "",
                        purchaseCost:
                            asset.purchaseCost?.toString() || "",
                        warrantyExpiryDate:
                            asset.warrantyExpiryDate || "",
                        locationId:
                            asset.locationId?.toString() || "",
                        status: asset.status || "IN_STOCK",
                        condition: asset.condition || "GOOD",
                        description:
                            asset.description || "",
                    });
                }
            } catch (error) {
                console.error(
                    "Failed to load asset form:",
                    error
                );

                setError(
                    error.response?.data?.message ||
                    "Unable to load asset information."
                );
            } finally {
                setLoading(false);
            }
        };

        loadFormData();
    }, [id, isEditMode]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setForm((currentForm) => ({
            ...currentForm,
            [name]: value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            setSaving(true);
            setError("");

            const payload = {
                assetTag: form.assetTag.trim(),

                serialNumber:
                    form.serialNumber.trim() || null,

                categoryId:
                    Number(form.categoryId),

                vendorId:
                    form.vendorId === ""
                        ? null
                        : Number(form.vendorId),

                brand:
                    form.brand.trim(),

                model:
                    form.model.trim() || null,

                purchaseDate:
                    form.purchaseDate || null,

                purchaseCost:
                    form.purchaseCost === ""
                        ? null
                        : Number(form.purchaseCost),

                warrantyExpiryDate:
                    form.warrantyExpiryDate || null,

                locationId:
                    Number(form.locationId),

                status:
                    form.status.trim(),

                condition:
                    form.condition.trim(),

                description:
                    form.description.trim() || null,
            };

            if (isEditMode) {
                await api.put(
                    `/assets/${id}`,
                    payload
                );
            } else {
                await api.post(
                    "/assets",
                    payload
                );
            }

            navigate("/assets");
        } catch (error) {
            console.error(
                "Failed to save asset:",
                error
            );

            const validationErrors =
                error.response?.data?.errors;

            if (validationErrors) {
                setError(
                    Object.values(validationErrors).join(" ")
                );
            } else {
                setError(
                    error.response?.data?.message ||
                    "Unable to save asset."
                );
            }
        } finally {
            setSaving(false);
        }
    };

    if (loading) {
        return (
            <div className="asset-form-loading">
                Loading asset form...
            </div>
        );
    }

    return (
        <div className="asset-form-page">

            <div className="asset-form-header">

                <div>
                    <h2>
                        {isEditMode
                            ? "Edit Asset"
                            : "Add Asset"}
                    </h2>

                    <p>
                        {isEditMode
                            ? "Update the asset information."
                            : "Add a new IT asset to the system."}
                    </p>
                </div>

                <button
                    type="button"
                    className="asset-back-button"
                    onClick={() =>
                        navigate("/assets")
                    }
                >
                    <FaArrowLeft />
                    Back to Assets
                </button>

            </div>

            {error && (
                <div className="asset-form-error">
                    {error}
                </div>
            )}

            <form
                className="asset-form-card"
                onSubmit={handleSubmit}
            >

                {/* Basic Information */}

                <div className="asset-form-section">

                    <div className="asset-form-section-title">
                        <h4>Basic Information</h4>

                        <p>
                            Enter the primary details of the asset.
                        </p>
                    </div>

                    <div className="asset-form-grid">

                        <div className="form-field">
                            <label htmlFor="assetTag">
                                Asset Tag *
                            </label>

                            <input
                                id="assetTag"
                                name="assetTag"
                                type="text"
                                value={form.assetTag}
                                onChange={handleChange}
                                maxLength={50}
                                required
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="serialNumber">
                                Serial Number
                            </label>

                            <input
                                id="serialNumber"
                                name="serialNumber"
                                type="text"
                                value={form.serialNumber}
                                onChange={handleChange}
                                maxLength={100}
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="categoryId">
                                Category *
                            </label>

                            <select
                                id="categoryId"
                                name="categoryId"
                                value={form.categoryId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">
                                    Select category
                                </option>

                                {categories.map(
                                    (category) => (
                                        <option
                                            key={category.id}
                                            value={category.id}
                                        >
                                            {category.name}
                                        </option>
                                    )
                                )}
                            </select>
                        </div>

                        <div className="form-field">
                            <label htmlFor="vendorId">
                                Vendor
                            </label>

                            <select
                                id="vendorId"
                                name="vendorId"
                                value={form.vendorId}
                                onChange={handleChange}
                            >
                                <option value="">
                                    Select vendor
                                </option>

                                {vendors.map(
                                    (vendor) => (
                                        <option
                                            key={vendor.id}
                                            value={vendor.id}
                                        >
                                            {vendor.name}
                                        </option>
                                    )
                                )}
                            </select>
                        </div>

                        <div className="form-field">
                            <label htmlFor="brand">
                                Brand *
                            </label>

                            <input
                                id="brand"
                                name="brand"
                                type="text"
                                value={form.brand}
                                onChange={handleChange}
                                maxLength={100}
                                required
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="model">
                                Model
                            </label>

                            <input
                                id="model"
                                name="model"
                                type="text"
                                value={form.model}
                                onChange={handleChange}
                                maxLength={150}
                            />
                        </div>

                    </div>

                </div>


                {/* Purchase Information */}

                <div className="asset-form-section">

                    <div className="asset-form-section-title">
                        <h4>Purchase Information</h4>

                        <p>
                            Enter purchase and warranty details.
                        </p>
                    </div>

                    <div className="asset-form-grid">

                        <div className="form-field">
                            <label htmlFor="purchaseDate">
                                Purchase Date
                            </label>

                            <input
                                id="purchaseDate"
                                name="purchaseDate"
                                type="date"
                                value={form.purchaseDate}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="purchaseCost">
                                Purchase Cost
                            </label>

                            <input
                                id="purchaseCost"
                                name="purchaseCost"
                                type="number"
                                min="0"
                                step="0.01"
                                value={form.purchaseCost}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="warrantyExpiryDate">
                                Warranty Expiry
                            </label>

                            <input
                                id="warrantyExpiryDate"
                                name="warrantyExpiryDate"
                                type="date"
                                value={form.warrantyExpiryDate}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="locationId">
                                Location *
                            </label>

                            <select
                                id="locationId"
                                name="locationId"
                                value={form.locationId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">
                                    Select location
                                </option>

                                {locations.map(
                                    (location) => (
                                        <option
                                            key={location.id}
                                            value={location.id}
                                        >
                                            {location.name}
                                            {location.city
                                                ? ` - ${location.city}`
                                                : ""}
                                        </option>
                                    )
                                )}
                            </select>
                        </div>

                    </div>

                </div>


                {/* Status */}

                <div className="asset-form-section">

                    <div className="asset-form-section-title">
                        <h4>Asset Status</h4>

                        <p>
                            Set the current status and condition.
                        </p>
                    </div>

                    <div className="asset-form-grid">

                        <div className="form-field">
                            <label htmlFor="status">
                                Status *
                            </label>

                            <input
                                id="status"
                                name="status"
                                type="text"
                                value={form.status}
                                onChange={handleChange}
                                maxLength={30}
                                required
                            />
                        </div>

                        <div className="form-field">
                            <label htmlFor="condition">
                                Condition *
                            </label>

                            <input
                                id="condition"
                                name="condition"
                                type="text"
                                value={form.condition}
                                onChange={handleChange}
                                maxLength={30}
                                required
                            />
                        </div>

                    </div>

                </div>


                {/* Description */}

                <div className="asset-form-section">

                    <div className="asset-form-section-title">
                        <h4>Description</h4>

                        <p>
                            Add additional information about the asset.
                        </p>
                    </div>

                    <div className="form-field">

                        <label htmlFor="description">
                            Description
                        </label>

                        <textarea
                            id="description"
                            name="description"
                            value={form.description}
                            onChange={handleChange}
                            maxLength={500}
                            rows={5}
                        />

                        <div className="character-count">
                            {form.description.length}/500
                        </div>

                    </div>

                </div>


                {/* Actions */}

                <div className="asset-form-actions">

                    <button
                        type="button"
                        className="asset-cancel-button"
                        onClick={() =>
                            navigate("/assets")
                        }
                        disabled={saving}
                    >
                        Cancel
                    </button>

                    <button
                        type="submit"
                        className="asset-save-button"
                        disabled={saving}
                    >
                        <FaSave />

                        {saving
                            ? "Saving..."
                            : isEditMode
                                ? "Update Asset"
                                : "Save Asset"}
                    </button>

                </div>

            </form>

        </div>
    );
};

export default AssetForm;