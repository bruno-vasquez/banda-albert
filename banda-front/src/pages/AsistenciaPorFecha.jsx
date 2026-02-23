import { useEffect, useMemo, useState } from "react";
import { apiGet } from "../api";

function toLowerSafe(v) {
  return (v ?? "").toString().toLowerCase();
}

function sortRows(rows, field, dir) {
  const mult = dir === "desc" ? -1 : 1;

  return [...rows].sort((a, b) => {
    const vaRaw = a?.[field];
    const vbRaw = b?.[field];

    if (typeof vaRaw === "boolean" || typeof vbRaw === "boolean") {
      return ((vaRaw ? 1 : 0) - (vbRaw ? 1 : 0)) * mult;
    }

    const va = toLowerSafe(vaRaw);
    const vb = toLowerSafe(vbRaw);
    if (va < vb) return -1 * mult;
    if (va > vb) return 1 * mult;
    return 0;
  });
}

export default function AsistenciaPorFecha() {
  const hoy = new Date().toISOString().slice(0, 10);

  const [fecha, setFecha] = useState(hoy);
  const [q, setQ] = useState("");
  const [familiaId, setFamiliaId] = useState("");
  const [instrumentoId, setInstrumentoId] = useState("");
  const [asistio, setAsistio] = useState("");

  const [familias, setFamilias] = useState([]);
  const [instrumentos, setInstrumentos] = useState([]);

  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(50);

  const [sortField, setSortField] = useState("apellido");
  const [sortDir, setSortDir] = useState("asc");

  const [error, setError] = useState("");

  async function loadFamilias() {
    try {
      const data = await apiGet(`/api/familias`);
      setFamilias(Array.isArray(data) ? data : (data?.content || []));
    } catch (e) {
      console.error(e);
    }
  }

  async function loadInstrumentos() {
    try {
      const data = await apiGet(`/api/instrumentos`);
      setInstrumentos(Array.isArray(data) ? data : (data?.content || []));
    } catch (e) {
      console.error(e);
    }
  }

  async function loadAsistencia() {
    setLoading(true);
    setError("");
    try {
      const params = new URLSearchParams();
      params.set("fecha", fecha);
      params.set("page", String(page));
      params.set("size", String(size));

      if (q.trim()) params.set("q", q.trim());
      if (familiaId) params.set("familiaId", familiaId);
      if (instrumentoId) params.set("instrumentoId", instrumentoId);
      if (asistio !== "") params.set("asistio", asistio);

      const data = await apiGet(`/api/asistencias/por-fecha?${params.toString()}`);
      const list = Array.isArray(data) ? data : (data?.content || []);
      setRows(list);
    } catch (e) {
      console.error(e);
      setRows([]);
      setError(e.message || "Error cargando");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadFamilias();
    loadInstrumentos();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    loadAsistencia();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [fecha, page, size]);

  const rowsSorted = useMemo(() => sortRows(rows, sortField, sortDir), [rows, sortField, sortDir]);

  function toggleSort(field) {
    if (sortField === field) setSortDir((d) => (d === "asc" ? "desc" : "asc"));
    else {
      setSortField(field);
      setSortDir("asc");
    }
  }

  function aplicarFiltros() {
    setPage(0);
    loadAsistencia();
  }

  function limpiar() {
    setQ("");
    setFamiliaId("");
    setInstrumentoId("");
    setAsistio("");
    setPage(0);
    setTimeout(loadAsistencia, 0);
  }

  return (
    <div style={{ padding: 16 }}>
      <h1 style={{ marginBottom: 12 }}>Asistencia por fecha</h1>

      <div style={{ display: "grid", gridTemplateColumns: "repeat(6, 1fr)", gap: 10, marginBottom: 12 }}>
        <div>
          <label>Fecha</label>
          <input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} style={{ width: "100%" }} />
        </div>

        <div style={{ gridColumn: "span 2" }}>
          <label>Buscar</label>
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="nombre, apellido, instrumento, familia..."
            style={{ width: "100%" }}
          />
        </div>

        <div>
          <label>Asistió</label>
          <select value={asistio} onChange={(e) => setAsistio(e.target.value)} style={{ width: "100%" }}>
            <option value="">Todos</option>
            <option value="true">Sí</option>
            <option value="false">No</option>
          </select>
        </div>

        <div>
          <label>Familia</label>
          <select value={familiaId} onChange={(e) => setFamiliaId(e.target.value)} style={{ width: "100%" }}>
            <option value="">Todas</option>
            {familias.map((f) => (
              <option key={f.id} value={f.id}>
                {f.nombre}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>Instrumento</label>
          <select value={instrumentoId} onChange={(e) => setInstrumentoId(e.target.value)} style={{ width: "100%" }}>
            <option value="">Todos</option>
            {instrumentos.map((i) => (
              <option key={i.id} value={i.id}>
                {i.codigo ?? i.id}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div style={{ display: "flex", gap: 10, alignItems: "center", marginBottom: 12 }}>
        <button onClick={aplicarFiltros} disabled={loading}>
          {loading ? "Cargando..." : "Aplicar filtros"}
        </button>
        <button onClick={limpiar} disabled={loading}>Limpiar</button>

        <div style={{ marginLeft: "auto", display: "flex", gap: 10, alignItems: "center" }}>
          <label>Tamaño:</label>
          <select
            value={size}
            onChange={(e) => {
              setPage(0);
              setSize(Number(e.target.value));
            }}
          >
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>

          <button onClick={() => setPage((p) => Math.max(0, p - 1))}>◀</button>
          <span>Página {page + 1}</span>
          <button onClick={() => setPage((p) => p + 1)}>▶</button>
        </div>
      </div>

      {error && (
        <div style={{ padding: 10, border: "1px solid #f99", background: "#fff2f2", marginBottom: 12 }}>
          <b>Error:</b> {error}
        </div>
      )}

      <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden" }}>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ background: "#f5f5f5" }}>
              <th style={{ padding: 10, cursor: "pointer", textAlign: "left" }} onClick={() => toggleSort("apellido")}>
                Alumno {sortField === "apellido" ? (sortDir === "asc" ? "▲" : "▼") : ""}
              </th>
              <th style={{ padding: 10, cursor: "pointer", textAlign: "left" }} onClick={() => toggleSort("instrumentoNombre")}>
                Instrumento {sortField === "instrumentoNombre" ? (sortDir === "asc" ? "▲" : "▼") : ""}
              </th>
              <th style={{ padding: 10, cursor: "pointer", textAlign: "left" }} onClick={() => toggleSort("familiaNombre")}>
                Familia {sortField === "familiaNombre" ? (sortDir === "asc" ? "▲" : "▼") : ""}
              </th>
              <th style={{ padding: 10, cursor: "pointer", textAlign: "left" }} onClick={() => toggleSort("asistio")}>
                Asistió {sortField === "asistio" ? (sortDir === "asc" ? "▲" : "▼") : ""}
              </th>
            </tr>
          </thead>

          <tbody>
            {rowsSorted.map((r) => (
              <tr key={r.asistenciaId} style={{ borderTop: "1px solid #eee" }}>
                <td style={{ padding: 10 }}>{(r.apellido ?? "") + " " + (r.nombre ?? "")}</td>
                <td style={{ padding: 10 }}>{r.instrumentoNombre ?? "-"}</td>
                <td style={{ padding: 10 }}>{r.familiaNombre ?? "-"}</td>
                <td style={{ padding: 10 }}>{r.asistio ? "Sí" : "No"}</td>
              </tr>
            ))}

            {!rowsSorted.length && (
              <tr>
                <td colSpan={4} style={{ padding: 14, textAlign: "center" }}>
                  {loading ? "Cargando..." : "Sin resultados"}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div style={{ marginTop: 12, fontSize: 12, color: "#555" }}>
        Tip: Ordena tocando los encabezados (Alumno/Instrumento/Familia/Asistió).
      </div>
    </div>
  );
}