import { useState } from "react";
import { api, setMe, setToken } from "../api";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("admin123");
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      const data = await api("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ username, password }),
      });
      setToken(data.token);
      setMe({
        username: data.username,
        role: data.role,
        familiaId: data.familiaId,
        familiaNombre: data.familiaNombre,
      });
      nav("/dashboard");
    } catch (err) {
      alert("Login falló: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 360, margin: "60px auto", fontFamily: "Arial" }}>
      <h2>Iniciar sesión</h2>
      <form onSubmit={submit} style={{ display: "grid", gap: 10 }}>
        <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Usuario" />
        <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Contraseña" type="password" />
        <button disabled={loading} type="submit">{loading ? "Entrando..." : "Entrar"}</button>
      </form>

      <p style={{ marginTop: 12, color: "#555" }}>
        Admin por defecto: <b>admin / admin123</b>
      </p>
    </div>
  );
}