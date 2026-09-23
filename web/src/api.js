export async function api(path, options = {}) {
  const res = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {})
    },
    credentials: "include",
    ...options
  });

  if (res.status === 204) {
    return null;
  }

  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    const message = data?.message || "Erro inesperado.";
    throw new Error(message);
  }

  return data;
}

export async function me() {
  return api("/api/auth/me");
}

export async function login(username, password) {
  return api("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({ username, password })
  });
}

export async function logout() {
  return api("/api/auth/logout", { method: "POST" });
}
