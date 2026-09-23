import { useState } from "react";
import { login } from "../api.js";

export default function Login({ onLoggedIn }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const user = await login(username, password);
      onLoggedIn(user);
    } catch (err) {
      setError(err.message || "Falha no login.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md rounded-xl bg-slate-900 border border-slate-800 p-6">
        <h1 className="text-xl font-semibold">Sistema de Matrículas</h1>
        <p className="text-slate-400 mt-1">Entre com seu login e senha</p>

        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-sm text-slate-300">Login</label>
            <input
              className="mt-1 w-full rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
            />
          </div>

          <div>
            <label className="block text-sm text-slate-300">Senha</label>
            <input
              type="password"
              className="mt-1 w-full rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
            />
          </div>

          {error ? (
            <div className="text-sm text-red-300 bg-red-950/30 border border-red-900 rounded-lg p-3">
              {error}
            </div>
          ) : null}

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>
    </div>
  );
}
