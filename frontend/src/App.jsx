import { Navigate, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Assets from "./pages/Assets";
import AssetForm from "./pages/AssetForm";
import AssetDetails from "./pages/AssetDetails";
import ProtectedRoute from "./routes/ProtectedRoute";
import MainLayout from "./layout/MainLayout";

function App() {
    return (
        <Routes>

            <Route
                path="/login"
                element={<Login />}
            />

            <Route element={<ProtectedRoute />}>

                <Route element={<MainLayout />}>

                    <Route
                        path="/dashboard"
                        element={<Dashboard />}
                    />

                    <Route
                        path="/assets"
                        element={<Assets />}
                    />

                    <Route
                        path="/assets/new"
                        element={<AssetForm />}
                    />

                    <Route
                        path="/assets/:id"
                        element={<AssetDetails />}
                    />

                    <Route
                        path="/assets/:id/edit"
                        element={<AssetForm />}
                    />

                </Route>

            </Route>

            <Route
                path="*"
                element={
                    <Navigate
                        to="/dashboard"
                        replace
                    />
                }
            />

        </Routes>
    );
}

export default App;