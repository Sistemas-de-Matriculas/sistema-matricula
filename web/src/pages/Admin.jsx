import { useEffect, useMemo, useState } from "react";
import { api, logout } from "../api.js";

function TabButton({ active, onClick, children }) {
  return (
    <button
      onClick={onClick}
      className={[
        "px-3 py-2 rounded-lg text-sm border",
        active
          ? "bg-slate-800 border-slate-700 text-slate-100"
          : "bg-slate-950 border-slate-800 text-slate-300 hover:border-slate-700"
      ].join(" ")}
    >
      {children}
    </button>
  );
}

export default function Admin({ user, onLoggedOut }) {
  const [tab, setTab] = useState("users");
  const [error, setError] = useState("");

  const tabs = useMemo(() => {
    return [
      { key: "users", label: "Usuários" },
      { key: "students", label: "Alunos" },
      { key: "professors", label: "Professores" },
      { key: "courses", label: "Cursos" },
      { key: "disciplines", label: "Disciplinas" },
      { key: "semesters", label: "Currículo" }
    ];
  }, []);

  async function handleLogout() {
    await logout();
    onLoggedOut();
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
        {user.role !== "SECRETARIA" ? (
          <div className="rounded-xl border border-slate-800 bg-slate-900 p-5">
            <div className="font-medium">Acesso restrito</div>
            <div className="text-slate-400 text-sm mt-1">
              Esta versão inicial possui telas administrativas apenas para a Secretaria.
            </div>
          </div>
        ) : (
          <>
            <div className="flex gap-2">
              {tabs.map((t) => (
                <TabButton key={t.key} active={tab === t.key} onClick={() => setTab(t.key)}>
                  {t.label}
                </TabButton>
              ))}
            </div>

            {error ? (
              <div className="mt-4 text-sm text-red-300 bg-red-950/30 border border-red-900 rounded-lg p-3">
                {error}
              </div>
            ) : null}

            <div className="mt-6">
              {tab === "users" ? <UsersPanel onError={setError} /> : null}
              {tab === "students" ? <StudentsPanel onError={setError} /> : null}
              {tab === "professors" ? <ProfessorsPanel onError={setError} /> : null}
              {tab === "courses" ? <CoursesPanel onError={setError} /> : null}
              {tab === "disciplines" ? <DisciplinesPanel onError={setError} /> : null}
              {tab === "semesters" ? <SemestersPanel onError={setError} /> : null}
            </div>
          </>
        )}
      </main>
    </div>
  );
}

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

function Table({ columns, rows, actions }) {
  return (
    <div className="overflow-x-auto border border-slate-800 rounded-lg">
      <table className="w-full text-sm">
        <thead className="bg-slate-950 text-slate-300">
          <tr>
            {columns.map((c) => (
              <th key={c.key} className="text-left px-3 py-2 font-medium">
                {c.label}
              </th>
            ))}
            {actions ? <th className="px-3 py-2"></th> : null}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={row.__key} className="border-t border-slate-800">
              {columns.map((c) => (
                <td key={c.key} className="px-3 py-2 text-slate-200">
                  {row[c.key]}
                </td>
              ))}
              {actions ? (
                <td className="px-3 py-2 text-right">{actions(row)}</td>
              ) : null}
            </tr>
          ))}
          {rows.length === 0 ? (
            <tr>
              <td
                colSpan={columns.length + (actions ? 1 : 0)}
                className="px-3 py-6 text-center text-slate-500"
              >
                Nenhum registro.
              </td>
            </tr>
          ) : null}
        </tbody>
      </table>
    </div>
  );
}

function UsersPanel({ onError }) {
  const [rows, setRows] = useState([]);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("ALUNO");
  const [loading, setLoading] = useState(false);

  async function refresh() {
    const data = await api("/api/admin/users");
    setRows(data.map((u) => ({ ...u, __key: String(u.id) })));
  }

  useEffect(() => {
    refresh().catch((e) => onError(e.message));
  }, []);

  async function create(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      await api("/api/admin/users", {
        method: "POST",
        body: JSON.stringify({ username, password, role })
      });
      setUsername("");
      setPassword("");
      setRole("ALUNO");
      await refresh();
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function remove(id) {
    onError("");
    try {
      await api(`/api/admin/users/${id}`, { method: "DELETE" });
      await refresh();
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Cadastrar usuário">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={create}>
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="login"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <select
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <option value="ALUNO">ALUNO</option>
            <option value="PROFESSOR">PROFESSOR</option>
            <option value="SECRETARIA">SECRETARIA</option>
          </select>
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Salvando..." : "Cadastrar"}
          </button>
        </form>
      </PanelCard>

      <PanelCard title="Usuários cadastrados">
        <Table
          columns={[
            { key: "id", label: "ID" },
            { key: "username", label: "Login" },
            { key: "role", label: "Perfil" }
          ]}
          rows={rows}
          actions={(row) => (
            <button
              onClick={() => remove(row.id)}
              className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
            >
              Remover
            </button>
          )}
        />
      </PanelCard>
    </div>
  );
}

function StudentsPanel({ onError }) {
  const [rows, setRows] = useState([]);
  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  async function refresh() {
    const data = await api("/api/admin/students");
    setRows(data.map((s) => ({ ...s, __key: String(s.id) })));
  }

  useEffect(() => {
    refresh().catch((e) => onError(e.message));
  }, []);

  async function create(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      await api("/api/admin/students", {
        method: "POST",
        body: JSON.stringify({ username, password, name })
      });
      setName("");
      setUsername("");
      setPassword("");
      await refresh();
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function remove(id) {
    onError("");
    try {
      await api(`/api/admin/students/${id}`, { method: "DELETE" });
      await refresh();
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Cadastrar aluno">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={create}>
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="nome"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="login"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Salvando..." : "Cadastrar"}
          </button>
        </form>
      </PanelCard>

      <PanelCard title="Alunos cadastrados">
        <Table
          columns={[
            { key: "id", label: "ID" },
            { key: "userId", label: "User" },
            { key: "username", label: "Login" },
            { key: "name", label: "Nome" }
          ]}
          rows={rows}
          actions={(row) => (
            <button
              onClick={() => remove(row.id)}
              className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
            >
              Remover
            </button>
          )}
        />
      </PanelCard>
    </div>
  );
}

function ProfessorsPanel({ onError }) {
  const [rows, setRows] = useState([]);
  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  async function refresh() {
    const data = await api("/api/admin/professors");
    setRows(data.map((p) => ({ ...p, __key: String(p.id) })));
  }

  useEffect(() => {
    refresh().catch((e) => onError(e.message));
  }, []);

  async function create(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      await api("/api/admin/professors", {
        method: "POST",
        body: JSON.stringify({ username, password, name })
      });
      setName("");
      setUsername("");
      setPassword("");
      await refresh();
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function remove(id) {
    onError("");
    try {
      await api(`/api/admin/professors/${id}`, { method: "DELETE" });
      await refresh();
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Cadastrar professor">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={create}>
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="nome"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="login"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Salvando..." : "Cadastrar"}
          </button>
        </form>
      </PanelCard>

      <PanelCard title="Professores cadastrados">
        <Table
          columns={[
            { key: "id", label: "ID" },
            { key: "userId", label: "User" },
            { key: "username", label: "Login" },
            { key: "name", label: "Nome" }
          ]}
          rows={rows}
          actions={(row) => (
            <button
              onClick={() => remove(row.id)}
              className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
            >
              Remover
            </button>
          )}
        />
      </PanelCard>
    </div>
  );
}

function CoursesPanel({ onError }) {
  const [rows, setRows] = useState([]);
  const [name, setName] = useState("");
  const [credits, setCredits] = useState("");
  const [loading, setLoading] = useState(false);

  async function refresh() {
    const data = await api("/api/admin/courses");
    setRows(data.map((c) => ({ ...c, __key: String(c.id) })));
  }

  useEffect(() => {
    refresh().catch((e) => onError(e.message));
  }, []);

  async function create(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      await api("/api/admin/courses", {
        method: "POST",
        body: JSON.stringify({ name, credits: Number(credits) })
      });
      setName("");
      setCredits("");
      await refresh();
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function remove(id) {
    onError("");
    try {
      await api(`/api/admin/courses/${id}`, { method: "DELETE" });
      await refresh();
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Cadastrar curso">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={create}>
          <input
            className="md:col-span-2 rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="nome do curso"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="créditos"
            value={credits}
            onChange={(e) => setCredits(e.target.value)}
          />
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Salvando..." : "Cadastrar"}
          </button>
        </form>
      </PanelCard>

      <PanelCard title="Cursos cadastrados">
        <Table
          columns={[
            { key: "id", label: "ID" },
            { key: "name", label: "Nome" },
            { key: "credits", label: "Créditos" }
          ]}
          rows={rows}
          actions={(row) => (
            <button
              onClick={() => remove(row.id)}
              className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
            >
              Remover
            </button>
          )}
        />
      </PanelCard>
    </div>
  );
}

function DisciplinesPanel({ onError }) {
  const [rows, setRows] = useState([]);
  const [name, setName] = useState("");
  const [courseId, setCourseId] = useState("");
  const [professorId, setProfessorId] = useState("");
  const [courses, setCourses] = useState([]);
  const [professors, setProfessors] = useState([]);
  const [loading, setLoading] = useState(false);

  async function refresh() {
    const data = await api("/api/admin/disciplines");
    setRows(data.map((d) => ({ ...d, __key: String(d.id) })));
  }

  async function loadRefs() {
    const [coursesData, professorsData] = await Promise.all([
      api("/api/admin/courses"),
      api("/api/admin/professors")
    ]);
    setCourses(coursesData);
    setProfessors(professorsData);
    if (!courseId && coursesData.length > 0) {
      setCourseId(String(coursesData[0].id));
    }
    if (!professorId && professorsData.length > 0) {
      setProfessorId(String(professorsData[0].id));
    }
  }

  useEffect(() => {
    Promise.all([refresh(), loadRefs()]).catch((e) => onError(e.message));
  }, []);

  async function create(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      await api("/api/admin/disciplines", {
        method: "POST",
        body: JSON.stringify({
          name,
          courseId: courseId ? Number(courseId) : null,
          professorId: professorId ? Number(professorId) : null
        })
      });
      setName("");
      await refresh();
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function remove(id) {
    onError("");
    try {
      await api(`/api/admin/disciplines/${id}`, { method: "DELETE" });
      await refresh();
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Cadastrar disciplina">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={create}>
          <input
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="nome da disciplina"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <select
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            value={courseId}
            onChange={(e) => setCourseId(e.target.value)}
          >
            {courses.map((c) => (
              <option key={c.id} value={String(c.id)}>
                {c.name}
              </option>
            ))}
          </select>
          <select
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            value={professorId}
            onChange={(e) => setProfessorId(e.target.value)}
          >
            {professors.map((p) => (
              <option key={p.id} value={String(p.id)}>
                {p.name}
              </option>
            ))}
          </select>
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            {loading ? "Salvando..." : "Cadastrar"}
          </button>
        </form>
        {(courses.length === 0 || professors.length === 0) ? (
          <div className="text-sm text-slate-400 mt-3">
            Para cadastrar disciplinas, cadastre pelo menos 1 curso e 1 professor.
          </div>
        ) : null}
      </PanelCard>

      <PanelCard title="Disciplinas cadastradas">
        <Table
          columns={[
            { key: "id", label: "ID" },
            { key: "name", label: "Disciplina" },
            { key: "courseName", label: "Curso" },
            { key: "professorName", label: "Professor" }
          ]}
          rows={rows}
          actions={(row) => (
            <button
              onClick={() => remove(row.id)}
              className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
            >
              Remover
            </button>
          )}
        />
      </PanelCard>
    </div>
  );
}

function SemestersPanel({ onError }) {
  const [semesters, setSemesters] = useState([]);
  const [semesterId, setSemesterId] = useState("");
  const [semesterCode, setSemesterCode] = useState("");
  const [disciplines, setDisciplines] = useState([]);
  const [disciplineId, setDisciplineId] = useState("");
  const [offerings, setOfferings] = useState([]);
  const [loading, setLoading] = useState(false);

  async function refreshSemesters() {
    const data = await api("/api/admin/semesters");
    setSemesters(data);
    if (!semesterId && data.length > 0) {
      setSemesterId(String(data[0].id));
    }
  }

  async function refreshDisciplines() {
    const data = await api("/api/admin/disciplines");
    setDisciplines(data);
    if (!disciplineId && data.length > 0) {
      setDisciplineId(String(data[0].id));
    }
  }

  async function refreshOfferings(targetSemesterId) {
    if (!targetSemesterId) {
      setOfferings([]);
      return;
    }
    const data = await api(`/api/admin/semesters/${targetSemesterId}/offerings`);
    setOfferings(data.map((o) => ({ ...o, __key: String(o.id) })));
  }

  useEffect(() => {
    Promise.all([refreshSemesters(), refreshDisciplines()]).catch((e) => onError(e.message));
  }, []);

  useEffect(() => {
    refreshOfferings(semesterId).catch((e) => onError(e.message));
  }, [semesterId]);

  async function createSemester(e) {
    e.preventDefault();
    onError("");
    setLoading(true);
    try {
      const created = await api("/api/admin/semesters", {
        method: "POST",
        body: JSON.stringify({ code: semesterCode })
      });
      setSemesterCode("");
      await refreshSemesters();
      setSemesterId(String(created.id));
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function addOffering(e) {
    e.preventDefault();
    if (!semesterId) {
      onError("Selecione um semestre.");
      return;
    }
    onError("");
    setLoading(true);
    try {
      await api(`/api/admin/semesters/${semesterId}/offerings`, {
        method: "POST",
        body: JSON.stringify({ disciplineId: disciplineId ? Number(disciplineId) : null })
      });
      await refreshOfferings(semesterId);
    } catch (err) {
      onError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function removeOffering(id) {
    onError("");
    try {
      await api(`/api/admin/offerings/${id}`, { method: "DELETE" });
      await refreshOfferings(semesterId);
    } catch (err) {
      onError(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <PanelCard title="Criar semestre e abrir período de matrículas (UC06)">
        <form className="grid grid-cols-1 md:grid-cols-4 gap-3" onSubmit={createSemester}>
          <input
            className="md:col-span-3 rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            placeholder="código do semestre (ex: 2026-2)"
            value={semesterCode}
            onChange={(e) => setSemesterCode(e.target.value)}
          />
          <button
            disabled={loading}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            Criar
          </button>
        </form>
      </PanelCard>

      <PanelCard title="Currículo do semestre (disciplinas ofertadas)">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
          <select
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            value={semesterId}
            onChange={(e) => setSemesterId(e.target.value)}
          >
            {semesters.map((s) => (
              <option key={s.id} value={String(s.id)}>
                {s.code} {s.enrollmentOpen ? "(matrículas abertas)" : ""}
              </option>
            ))}
          </select>

          <select
            className="rounded-lg bg-slate-950 border border-slate-800 px-3 py-2 outline-none focus:border-slate-600"
            value={disciplineId}
            onChange={(e) => setDisciplineId(e.target.value)}
          >
            {disciplines.map((d) => (
              <option key={d.id} value={String(d.id)}>
                {d.name} • {d.courseName} • {d.professorName}
              </option>
            ))}
          </select>

          <button
            disabled={loading}
            onClick={addOffering}
            className="rounded-lg bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 px-3 py-2 font-medium"
          >
            Adicionar
          </button>
        </div>

        {semesters.length === 0 ? (
          <div className="text-sm text-slate-400 mt-3">Crie um semestre para começar o currículo.</div>
        ) : null}
        {disciplines.length === 0 ? (
          <div className="text-sm text-slate-400 mt-3">
            Cadastre disciplinas antes de gerar o currículo do semestre.
          </div>
        ) : null}

        <div className="mt-5">
          <Table
            columns={[
              { key: "id", label: "ID" },
              { key: "disciplineName", label: "Disciplina" },
              { key: "courseName", label: "Curso" },
              { key: "professorName", label: "Professor" },
              { key: "capacity", label: "Vagas" },
              { key: "status", label: "Status" }
            ]}
            rows={offerings}
            actions={(row) => (
              <button
                onClick={() => removeOffering(row.id)}
                className="text-sm px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700"
              >
                Remover
              </button>
            )}
          />
        </div>
      </PanelCard>
    </div>
  );
}
