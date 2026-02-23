import { useEffect, useState } from "react";
import { api } from "../api";

const initial = {
  id: null,
  nombre: "",
  apellido: "",
  celularAlumno: "",
  celularApoderado: "",
  curso: "",
  paralelo: "",
  grado: "",
  instrumentoId: "",
  antiguedad: false,
  bandaMusica: false,
  bandaGuerra: false,
  extras: "",
};

export default function AdminAlumnos() {
  const [alumnos, setAlumnos] = useState([]);
  const [instrumentos, setInstrumentos] = useState([]);
  const [form, setForm] = useState(initial);

  const cargar = async () => {
    setAlumnos(await api("/api/alumnos"));
    setInstrumentos(await api("/api/instrumentos"));
  };

  useEffect(() => {
    cargar().catch((e) => alert(e.message));
  }, []);

  const onChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((p) => ({ ...p, [name]: type === "checkbox" ? checked : value }));
  };

  const editar = (a) => {
    setForm({
      id: a.id,
      nombre: a.nombre || "",
      apellido: a.apellido || "",
      celularAlumno: a.celularAlumno || "",
      celularApoderado: a.celularApoderado || "",
      curso: a.curso ?? "",
      paralelo: a.paralelo || "",
      grado: a.grado || "",
      instrumentoId: a.instrumento?.id ?? "",
      antiguedad: !!a.antiguedad,
      bandaMusica: !!a.bandaMusica,
      bandaGuerra: !!a.bandaGuerra,
      extras: a.extras || "",
    });
  };

  const limpiar = () => setForm(initial);

  const guardar = async (e) => {
    e.preventDefault();

    const payload = {
      nombre: form.nombre,
      apellido: form.apellido,
      celularAlumno: form.celularAlumno,
      celularApoderado: form.celularApoderado,
      curso: form.curso === "" ? null : Number(form.curso),
      paralelo: form.paralelo,
      grado: form.grado,
      instrumento: form.instrumentoId ? { id: Number(form.instrumentoId) } : null,
      antiguedad: form.antiguedad,
      bandaMusica: form.bandaMusica,
      bandaGuerra: form.bandaGuerra,
      extras: form.extras,
    };

    if (form.id) {
      await api(`/api/alumnos/${form.id}`, { method: "PUT", body: JSON.stringify(payload) });
    } else {
      await api(`/api/alumnos`, { method: "POST", body: JSON.stringify(payload) });
    }

    limpiar();
    await cargar();
  };

  const retirar = async (id) => {
    if (!confirm("¿Retirar alumno (no se borra historial)?")) return;
    await api(`/api/alumnos/${id}/retirar`, { method: "PUT" });
    await cargar();
  };

  return (
    <div style={{ display: "flex", gap: 16 }}>
      <div style={{ width: 360, border: "1px solid #ddd", borderRadius: 10, padding: 12 }}>
        <h3>{form.id ? "Editar" : "Crear"} alumno</h3>
        <form onSubmit={guardar} style={{ display: "grid", gap: 10 }}>
          <input name="nombre" value={form.nombre} onChange={onChange} placeholder="Nombre" required />
          <input name="apellido" value={form.apellido} onChange={onChange} placeholder="Apellido" required />
          <input name="celularAlumno" value={form.celularAlumno} onChange={onChange} placeholder="Celular alumno" required />
          <input name="celularApoderado" value={form.celularApoderado} onChange={onChange} placeholder="Celular tutor" />

          <input name="curso" value={form.curso} onChange={onChange} placeholder="Curso (número)" />
          <input name="paralelo" value={form.paralelo} onChange={onChange} placeholder="Paralelo" />
          <input name="grado" value={form.grado} onChange={onChange} placeholder="Grado" />

          <select name="instrumentoId" value={form.instrumentoId} onChange={onChange}>
            <option value="">-- Instrumento --</option>
            {instrumentos.map((i) => (
              <option key={i.id} value={i.id}>
                {i.codigo} — {i.familia?.nombre}
              </option>
            ))}
          </select>

          <input name="extras" value={form.extras} onChange={onChange} placeholder="Detalles / extras" />

          <label><input type="checkbox" name="antiguedad" checked={form.antiguedad} onChange={onChange} /> Antigüedad</label>
          <label><input type="checkbox" name="bandaMusica" checked={form.bandaMusica} onChange={onChange} /> Banda Música</label>
          <label><input type="checkbox" name="bandaGuerra" checked={form.bandaGuerra} onChange={onChange} /> Banda Guerra</label>

          <button>{form.id ? "Actualizar" : "Guardar"}</button>
          {form.id && <button type="button" onClick={limpiar}>Cancelar</button>}
        </form>
      </div>

      <div style={{ flex: 1 }}>
        <h3>Alumnos activos (Admin)</h3>
        <table border="1" style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Instrumento</th>
              <th>Familia</th>
              <th>Banda</th>
              <th>Curso</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {alumnos.map((a) => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{a.apellido} {a.nombre}</td>
                <td>{a.instrumento?.codigo || "-"}</td>
                <td>{a.familiaNombre || "-"}</td>
                <td>{a.bandaTexto}</td>
                <td>{a.cursoTexto || "-"}</td>
                <td>
                  <button onClick={() => editar(a)}>Editar</button>{" "}
                  <button onClick={() => retirar(a.id)}>Retirar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}