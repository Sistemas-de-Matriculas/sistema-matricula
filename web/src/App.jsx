import { useEffect, useState } from "react";
import { me } from "./api.js";
import Login from "./pages/Login.jsx";
import Admin from "./pages/Admin.jsx";
import Student from "./pages/Student.jsx";

export default function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    me()
      .then((u) => setUser(u))
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-slate-400">
        Carregando...
      </div>
    );
  }

  if (!user) {
    return <Login onLoggedIn={(u) => setUser(u)} />;
  }

  if (user.role === "SECRETARIA") {
    return <Admin user={user} onLoggedOut={() => setUser(null)} />;
  }

  if (user.role === "ALUNO") {
    return <Student user={user} onLoggedOut={() => setUser(null)} />;
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-slate-300">
      <div className="text-lg font-medium">Acesso restrito</div>
      <div className="text-sm text-slate-400 mt-1">Ainda não há telas para este perfil.</div>
    </div>
  );
}
