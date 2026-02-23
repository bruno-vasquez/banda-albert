import { Routes, Route, Navigate, Link, useNavigate } from "react-router-dom";
import { clearToken, getMe } from "./api";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AdminAlumnos from "./pages/AdminAlumnos";
import AdminInstrumentos from "./pages/AdminInstrumentos";
import Asistencia from "./pages/Asistencia";

function Layout({ children }) {
  const me = getMe();
  const nav = useNavigate();

  const logout = () => {
    clearToken();
    nav("/login");
  };

  return (
    <div style={{ fontFamily: "Arial", padding: 16 }}>
      <div style={{ display: "flex", gap: 12, alignItems: "center", marginBottom: 16 }}>
        <h2 style={{ margin: 0 }}>Banda System</h2>
        <Link to="/dashboard">Dashboard</Link>
        {me?.role === "ROLE_ADMIN" && (
          <>
            <Link to="/admin/alumnos">Alumnos</Link>
            <Link to="/admin/instrumentos">Instrumentos</Link>
          </>
        )}
        <Link to="/asistencia">Asistencia</Link>
        <div style={{ marginLeft: "auto" }}>
          {me ? (
            <>
              <span style={{ marginRight: 10 }}>
                {me.username} ({me.role}{me.familiaNombre ? ` - ${me.familiaNombre}` : ""})
              </span>
              <button onClick={logout}>Salir</button>
            </>
          ) : null}
        </div>
      </div>

      {children}
    </div>
  );
}

function PrivateRoute({ children }) {
  const me = getMe();
  return me ? children : <Navigate to="/login" replace />;
}

function AdminRoute({ children }) {
  const me = getMe();
  if (!me) return <Navigate to="/login" replace />;
  if (me.role !== "ROLE_ADMIN") return <Navigate to="/dashboard" replace />;
  return children;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <Layout>
              <Dashboard />
            </Layout>
          </PrivateRoute>
        }
      />

      <Route
        path="/admin/alumnos"
        element={
          <AdminRoute>
            <Layout>
              <AdminAlumnos />
            </Layout>
          </AdminRoute>
        }
      />

      <Route
        path="/admin/instrumentos"
        element={
          <AdminRoute>
            <Layout>
              <AdminInstrumentos />
            </Layout>
          </AdminRoute>
        }
      />

      <Route
        path="/asistencia"
        element={
          <PrivateRoute>
            <Layout>
              <Asistencia />
            </Layout>
          </PrivateRoute>
        }
      />

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}