import { useEffect, useState } from "react";
import { api } from "../api";

export default function AdminInstrumentos() {
  const [familias, setFamilias] = useState([]);
  const [instrumentos, setInstrumentos] = useState([]);

  const [codigo, setCodigo] = useState("");
  const [familiaId, setFamiliaId] = useState("");
  const [propio, setPropio] = useState(false);

  const cargar = async () => {
    setFamilias(await api("/api/familias"));
    setInstrumentos(await api("/api/instrumentos"));
  };

  useEffect(() => {
    cargar().catch((e) => alert(e.message));
  }, []);

  const crear = async (e) => {
    e.preventDefault();
    await api(`/api/instrumentos?codigo=${encodeURIComponent(codigo)}&familiaId=${familiaId}&propio=${propio}`, {
      method: "POST",
    });
    setCodigo("");
    setFamiliaId("");
    setPropio(false);
    await cargar();
  };

  return (
    <div>
      <h3>Instrumentos (Admin)</h3>

      <form onSubmit={crear} style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
        <input value={codigo} onChange={(e) => setCodigo(e.target.value)} placeholder="Código (TP01)" required />
        <select value={familiaId} onChange={(e) => setFamiliaId(e.target.value)} required>
          <option value="">-- Familia --</option>
          {familias.map((f) => (
            <option key={f.id} value={f.id}>{f.nombre}</option>
          ))}
        </select>
        <label>
          <input type="checkbox" checked={propio} onChange={(e) => setPropio(e.target.checked)} /> Propio
        </label>
        <button>Crear</button>
      </form>

      <table border="1" style={{ width: "100%", marginTop: 14, borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Código</th>
            <th>Familia</th>
            <th>Propio</th>
          </tr>
        </thead>
        <tbody>
          {instrumentos.map((i) => (
            <tr key={i.id}>
              <td>{i.id}</td>
              <td>{i.codigo}</td>
              <td>{i.familia?.nombre}</td>
              <td>{i.propio ? "Sí" : "No"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}