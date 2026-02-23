import { useEffect, useState } from "react";
import { api, getMe } from "../api";

export default function Dashboard() {
  const [alumnos, setAlumnos] = useState([]);
  const [instrumentos, setInstrumentos] = useState([]);
  const [familias, setFamilias] = useState([]);
  const me = getMe();

  useEffect(() => {
    (async () => {
      const a = await api("/api/alumnos");
      setAlumnos(a);
      const f = await api("/api/familias");
      setFamilias(f);
      if (me?.role === "ROLE_ADMIN") {
        const i = await api("/api/instrumentos");
        setInstrumentos(i);
      }
    })().catch((e) => alert(e.message));
  }, []);

  const porFamilia = alumnos.reduce((acc, x) => {
    const fam = x.familiaNombre || "SIN_FAMILIA";
    acc[fam] = (acc[fam] || 0) + 1;
    return acc;
  }, {});

  return (
    <div>
      <h3>Dashboard</h3>
      <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
        <Card title="Total alumnos activos" value={alumnos.length} />
        <Card title="Total familias" value={familias.length} />
        {me?.role === "ROLE_ADMIN" && <Card title="Total instrumentos" value={instrumentos.length} />}
      </div>

      <h4 style={{ marginTop: 18 }}>Miembros por familia (activos)</h4>
      <ul>
        {Object.entries(porFamilia).map(([k, v]) => (
          <li key={k}>{k}: {v}</li>
        ))}
      </ul>
    </div>
  );
}

function Card({ title, value }) {
  return (
    <div style={{ border: "1px solid #ddd", borderRadius: 10, padding: 12, minWidth: 200 }}>
      <div style={{ color: "#666", fontSize: 13 }}>{title}</div>
      <div style={{ fontSize: 28, fontWeight: 700 }}>{value}</div>
    </div>
  );
}