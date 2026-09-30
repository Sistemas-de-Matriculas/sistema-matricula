import { useEffect, useState } from "react";
import { api, logout } from "../api.js";

const STATUS_LABELS = {
  OFFERED: "Ofertada",
  CONFIRMED: "Confirmada",
  CANCELLED: "Cancelada"
};

function PanelCard({ title, children }) {
  return (
    <div className="rounded-xl border border-slate-800 bg-slate-900 mb-6">
      <div className="px-5 py-4 border-b border-slate-800 flex items-center justify-between">
        <div className="font-medium">{title}</div>
      </div>
      <div className="p-5">{children}</div>
    </div>
  );
}

export default function Professor({ user, onLoggedOut }) {
  const [error, setError] = useState("");
  const [offerings, setOfferings] = useState([]);
  const [selected, setSelected] = useState(null);
  const [students, setStudents] = useState([]);
  const [loadingStudents, setLoadingStudents] = useState(false);

  useEffect(() => {
    api("/api/professor/offerings")
      .then((data) => setOfferings(data))
      .catch((e) => setError(e.message || "Erro inesperado."));
  }, []);

  async function handleLogout() {
    await logout();
    onLoggedOut();
  }

  async function handleSelect(offering) {
    setSelected(offering);
    setStudents([]);
    setError("");
    setLoadingStudents(true);
    try {
      const data = await api(`/api/professor/offerings/${offering.offeringId}/students`);
      setStudents(data);
    } catch (err) {
      setError(err.message || "Erro inesperado.");
    } finally {
      setLoadingStudents(false);
    }
  }

  return (
    <div className="min-h-screen">
      <header className="border-b border-slate-800 bg-slate-950/70 backdrop-blur">
        <div className="max-w-5xl mx-auto px-6 py-4 flex items-center justify-between">
          <div>
            <div className="text-lg font-semibold">Sistema de Matrículas</div>
            <div className="text-sm text-slate-400">
              {user.username} ({user.role})
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="text-sm px-3 py-2 rounded-lg bg-slate-900 border border-slate-800 hover:border-slate-700"
          >
            Sair
          </button>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-6 py-6">
        {error ? (
          <div className="mb-4 text-sm text-red-300 bg-red-950/30 border border-red-900 rounded-lg p-3">
            {error}
          </div>
        ) : null}

        <PanelCard title="Minhas disciplinas">
          {offerings.length === 0 ? (
            <div className="text-sm text-slate-400">Nenhuma disciplina ofertada sob sua responsabilidade.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="text-left text-slate-400 border-b border-slate-800">
                    <th className="py-2 pr-3">Semestre</th>
                    <th className="py-2 pr-3">Disciplina</th>
                    <th className="py-2 pr-3">Curso</th>
                    <th className="py-2 pr-3">Tipo</th>
                    <th className="py-2 pr-3">Matriculados</th>
                    <th className="py-2 pr-3">Status</th>
                    <th className="py-2 pr-3">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {offerings.map((o) => (
                    <tr
                      key={o.offeringId}
                      className={`border-b border-slate-800/60 ${
                        selected?.offeringId === o.offeringId ? "bg-slate-800/40" : ""
                      }`}
                    >
                      <td className="py-2 pr-3">{o.semesterCode}</td>
                      <td className="py-2 pr-3">{o.disciplineName}</td>
                      <td className="py-2 pr-3">{o.courseName}</td>
                      <td className="py-2 pr-3">
                        {o.category === "MANDATORY" ? "Obrigatória" : "Optativa"}
                      </td>
                      <td className="py-2 pr-3">
                        {o.enrolledCount} / {o.capacity}
                      </td>
                      <td className="py-2 pr-3">{STATUS_LABELS[o.status] || o.status}</td>
                      <td className="py-2 pr-3">
                        <button
                          onClick={() => handleSelect(o)}
                          className="text-xs px-2 py-1 rounded bg-sky-900/40 hover:bg-sky-900/60 border border-sky-900"
                        >
                          Ver alunos
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </PanelCard>

        {selected ? (
          <PanelCard title={`Alunos matriculados — ${selected.disciplineName} (${selected.semesterCode})`}>
            {loadingStudents ? (
              <div className="text-sm text-slate-400">Carregando...</div>
            ) : students.length === 0 ? (
              <div className="text-sm text-slate-400">Nenhum aluno matriculado nesta disciplina.</div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="text-left text-slate-400 border-b border-slate-800">
                      <th className="py-2 pr-3">Nome</th>
                      <th className="py-2 pr-3">Login</th>
                      <th className="py-2 pr-3">Matriculado em</th>
                    </tr>
                  </thead>
                  <tbody>
                    {students.map((s) => (
                      <tr key={s.enrollmentId} className="border-b border-slate-800/60">
                        <td className="py-2 pr-3">{s.studentName}</td>
                        <td className="py-2 pr-3">{s.username}</td>
                        <td className="py-2 pr-3">
                          {s.enrolledAt ? new Date(s.enrolledAt).toLocaleString("pt-BR") : "—"}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </PanelCard>
        ) : null}
      </main>
    </div>
  );
}
