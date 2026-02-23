const base = import.meta.env.VITE_API_URL;

export function getToken() {
  return localStorage.getItem("token");
}

export function setToken(t) {
  localStorage.setItem("token", t);
}

export function clearToken() {
  localStorage.removeItem("token");
  localStorage.removeItem("me");
}

export function getMe() {
  const raw = localStorage.getItem("me");
  return raw ? JSON.parse(raw) : null;
}

export function setMe(me) {
  localStorage.setItem("me", JSON.stringify(me));
}

export async function api(path, opts = {}) {
  const token = getToken();
  const headers = {
    ...(opts.headers || {}),
    "Content-Type": "application/json",
  };
  if (token) headers.Authorization = `Bearer ${token}`;

  const res = await fetch(`${base}${path}`, { ...opts, headers });
  if (!res.ok) {
    const txt = await res.text().catch(() => "");
    throw new Error(txt || `Error ${res.status}`);
  }
  const ct = res.headers.get("content-type") || "";
  return ct.includes("application/json") ? res.json() : res.text();
}