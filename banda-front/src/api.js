import { API_BASE } from "./config/apiBase.js";

export async function api(path, options = {}) {
  const token = localStorage.getItem("token");

  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  });

  if (!res.ok) {
    const msg = await res.text().catch(() => "");
    throw new Error(msg || `Error ${res.status}`);
  }

  const text = await res.text().catch(() => "");
  return text ? JSON.parse(text) : {};
}

export function setToken(token) {
  if (token) localStorage.setItem("token", token);
  else localStorage.removeItem("token");
}

export function getToken() {
  return localStorage.getItem("token");
}

export function setMe(me) {
  if (me) localStorage.setItem("me", JSON.stringify(me));
  else localStorage.removeItem("me");
}

export function getMe() {
  const raw = localStorage.getItem("me");
  return raw ? JSON.parse(raw) : null;
}

// Lo que te faltaba para App.jsx
export function clearToken() {
  setToken(null);
  setMe(null);
}