import { useEffect, useState } from "react";
import { api, logout } from "../api.js";

function PanelCard({ title, children }) {
  return (
    <div className="rounded-xl border border-slate-800 bg-slate-900">
      <div className="px-5 py-4 border-b border-slate-800 flex items-center justify-between">
        <div className="font-medium">{title}</div>
      </div>
      <div className="p-5">{children}</div>
    </div>
  );
}

export default function Student({ user, onLoggedOut }) {
  const [error, setError] = useState("");
  const [semesters, setSemesters] = useState([]);
  const [semesterId, setSemesterId] = useState("");
  const [offerings, setOfferings] = useState([]);
  const [enrollments, setEnrollments] = useState([]);
  const [selectedOfferings, setSelectedOfferings] = useState([]);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    api("/api/student/semesters")
      .then((data) => {
        setSemesters(data);
        if (data.length > 0) {
          setSemesterId(String(data[0].id));
        }
      })
      .catch((e) => setError(e.message || "Erro inesperado."));
  }, []);

  useEffect(() => {
    if (!semesterId) {
      setOfferings([]);
      setEnrollments([]);
      setSelectedOfferings([]);
      return;
    }
    api(`/api/student/semesters/${semesterId}/offerings`)
      .then((data) => setOfferings(data))
      .catch((e) => setError(e.message || "Erro inesperado."));
    api(`/api/student/semesters/${semesterId}/enrollments`)
      .then((data) => setEnrollments(data))
      .catch((e) => setError(e.message || "Erro inesperado."));
  }, [semesterId]);

  async function handleLogout() {
    await logout();
    onLoggedOut();
  }

  function toggleSelect(offeringId) {
    setSelectedOfferings((prev) =>
      prev.includes(offeringId) ? prev.filter((id) => id !== offeringId) : [...prev, offeringId]
    );
  }

  async function handleEnroll(e) {
    e.preventDefault();
    if (!semesterId || selectedOfferings.length === 0) {
      setError("Selecione ao menos uma disciplina para matrícula.");
      return;
    }
    setSubmitting(true);
    setError("");
    try {
      await api(`/api/student/semesters/${semesterId}/enrollments`, {
        method: "POST",
        body: JSON.stringify({ offeringIds: selectedOfferings })
      });
      setSelectedOfferings([]);
      const refreshed = await api(`/api/student/semesters/${semesterId}/enrollments`);
      setEnrollments(refreshed);
      const refreshedOfferings = await api(`/api/student/semesters/${semesterId}/offerings`);
      setOfferings(refreshedOfferings);
    } catch (err) {
      setError(err.message || "Erro inesperado.");
    } finally {
      setSubmitting(false);
    }
  }

  async function handleCancel(enrollmentId) {
    setError("");
    try {
      await api(`/api/student/enrollments/${enrollmentId}`, { method: "DELETE" });
      const refreshed = await api(`/api/student/semesters/${semesterId}/enrollments`);
      setEnrollments(refreshed);
      const refreshedOfferings = await api(`/api/student/semesters/${semesterId}/offerings`);
      setOfferings(refreshedOfferings);
    } catch (err) {
      setError(err.message || "Erro inesperado.");
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

        <PanelCard title="Disciplinas ofertadas">
          <div className="flex items-center gap-3">
            <label className="text-sm text-slate-300">Semestre</label>
            <select
              className="bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-sm"
              value={semesterId}
              onChange={(e) => setSemesterId(e.target.value)}
            >
              <option value="">Selecione...</option>
              {semesters.map((s) => (
                <option key={s.id} value={String(s.id)}>
                  {s.code}
                </option>
              ))}
            </select>
          </div>

          {semesterId && offerings.length === 0 ? (
            <div className="mt-4 text-sm text-slate-400">Nenhuma disciplina ofertada para este semestre.</div>
          ) : null}

          {offerings.length > 0 ? (
            <form onSubmit={handleEnroll} className="mt-4">
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="text-left text-slate-400 border-b border-slate-800">
                      <th className="py-2 pr-3">
                        <input
                          type="checkbox"
                          checked={false}
                          onChange={() => {}}
                          className="opacity-0 pointer-events-none"
                          aria-hidden="true"
                          tabIndex={-1}
                        />
                      </th>
                      <th className="py-2 pr-3">Disciplina</th>
                      <th className="py-2 pr-3">Tipo</th>
                      <th className="py-2 pr-3">Professor</th>
                      <th className="py-2 pr-3">Vagas</th>
                    </tr>
                  </thead>
                  <tbody>
                    {offerings.map((o) => {
                      const disabled = o.availableSeats <= 0;
                      const checked = selectedOfferings.includes(o.offeringId);
                      return (
                        <tr key={o.offeringId} className="border-b border-slate-800/60">
                          <td className="py-2 pr-3">
                            <input
                              type="checkbox"
                              disabled={disabled}
                              checked={checked}
                              onChange={() => toggleSelect(o.offeringId)}
                            />
                          </td>
                          <td className="py-2 pr-3">{o.disciplineName}</td>
                          <td className="py-2 pr-3">
                            {o.category === "MANDATORY" ? "Obrigatória" : "Optativa"}
                          </td>
                          <td className="py-2 pr-3">{o.professorName}</td>
                          <td className="py-2 pr-3">
                            {o.availableSeats} / {o.capacity}
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
              <div className="mt-3 flex justify-end">
                <button
                  type="submit"
                  disabled={submitting || selectedOfferings.length === 0}
                  className="text-sm px-4 py-2 rounded-lg bg-sky-600 hover:bg-sky-500 disabled:bg-slate-700 disabled:text-slate-400"
                >
                  {submitting ? "Matriculando..." : "Matricular-se"}
                </button>
              </div>
            </form>
          ) : null}
        </PanelCard>

        {semesterId ? (
          <PanelCard title="Minhas matrículas">
            {enrollments.length === 0 ? (
              <div className="text-sm text-slate-400">Nenhuma matrícula registrada neste semestre.</div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="text-left text-slate-400 border-b border-slate-800">
                      <th className="py-2 pr-3">Disciplina</th>
                      <th className="py-2 pr-3">Tipo</th>
                      <th className="py-2 pr-3">Status</th>
                      <th className="py-2 pr-3">Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    {enrollments.map((en) => (
                      <tr key={en.id} className="border-b border-slate-800/60">
                        <td className="py-2 pr-3">{en.disciplineName}</td>
                        <td className="py-2 pr-3">
                          {en.disciplineCategory === "MANDATORY" ? "Obrigatória" : "Optativa"}
                        </td>
                        <td className="py-2 pr-3">{en.status}</td>
                        <td className="py-2 pr-3">
                          {en.status === "ENROLLED" ? (
                            <button
                              onClick={() => handleCancel(en.id)}
                              className="text-xs px-2 py-1 rounded bg-red-900/40 hover:bg-red-900/60 border border-red-900"
                            >
                              Cancelar matrícula
                            </button>
                          ) : (
                            <span className="text-xs text-slate-500">—</span>
                          )}
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
