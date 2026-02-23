import { API_BASE } from "../config/apiBase";

export async function apiGet(path, token) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });

  if (!res.ok) {
    const msg = await res.text().catch(() => "");
    throw new Error(msg || `Error ${res.status}`);
  }
  return res.json();
}

export async function apiPost(path, body, token) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const msg = await res.text().catch(() => "");
    throw new Error(msg || `Error ${res.status}`);
  }
  return res.json();
}

// Ejemplos:
export const login = (cred) => apiPost("/api/auth/login", cred);
export const listarAlumnos = (token) => apiGet("/api/alumnos", token);