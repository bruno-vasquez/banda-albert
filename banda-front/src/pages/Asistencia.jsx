import { useEffect, useMemo, useState } from "react";
import { api, getMe } from "../api";

function todayISO() {
  const d = new Date();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${d.getFullYear()}-${m}-${day}`;
}

export default function Asistencia() {
  const me = getMe();
  const [fecha, setFecha] = useState(todayISO());
  const [alumnos, setAlumnos] = useState([]);
  const [mark, setMark] = useState({}); // { alumnoId: true/false }

  const cargar = async () => {
    const a = await api("/api/alumnos"); // backend ya filtra por familia si es encargado
    setAlumnos(a);
    const init = {};
    a.forEach((x) => (init[x.id] = false));
    setMark(init);
  };

  useEffect(() => {
    cargar().catch((e) => alert(e.message));
  }, []);

  const familiaActual = useMemo(() => {
    if (me.role === "ROLE_ADMIN") return "ADMIN (todas)";
    return me.familiaNombre || "Sin familia asignada";
  }, [me]);

  const toggle = (id) => setMark((p) => ({ ...p, [id]: !p[id] }));

  const guardar = async () => {
    const items = alumnos.map((a) => ({ alumnoId: a.id, asistio: !!mark[a.id] }));
    await api("/api/asistencias/bulk", {
      method: "POST",
      body: JSON.stringify({ fecha, items }),
    });
    alert("Asistencia guardada ✅ (duplicados en la misma fecha se ignoran)");
  };

  return (
    <div>
      <h3>Asistencia — {familiaActual}</h3>

      <div style={{ display: "flex", gap: 10, alignItems: "center" }}>
        <label>Fecha:</label>
        <input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} />
        <button onClick={guardar}>Guardar asistencia</button>
      </div>

      <table border="1" style={{ width: "100%", marginTop: 14, borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>Asistió</th>
            <th>Alumno</th>
            <th>Familia</th>
            <th>Instrumento</th>
            <th>Banda</th>
            <th>Curso</th>
          </tr>
        </thead>
        <tbody>
          {alumnos.map((a) => (
            <tr key={a.id}>
              <td style={{ textAlign: "center" }}>
                <input type="checkbox" checked={!!mark[a.id]} onChange={() => toggle(a.id)} />
              </td>
              <td>{a.apellido} {a.nombre}</td>
              <td>{a.familiaNombre || "-"}</td>
              <td>{a.instrumento?.codigo || "-"}</td>
              <td>{a.bandaTexto}</td>
              <td>{a.cursoTexto || "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}