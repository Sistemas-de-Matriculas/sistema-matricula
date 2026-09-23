## Épico E0 — Bootstrap do Projeto (estrutura + Docker-first)
### INFRA-01 — Criar estrutura do repositório
- Objetivo: organizar o repo para suportar app Spring Boot + Docker + migrações.
- Estrutura-alvo (mínima):
  - `docs/` (já existe)
  - `app/` (código Spring Boot)
  - `docker/` (artefatos auxiliares: init-db, billing-mock futuro)
  - `docker-compose.yml`
  - `Dockerfile` (na raiz ou em`app/` , decidir e padronizar)
  - `.gitignore` ,`.dockerignore`
- Critérios de aceite:
  - Estrutura criada e consistente (sem arquivos soltos/confusos).
  - Projeto preparado para rodar via`docker compose up --build` .
### INFRA-02 — Inicializar aplicação Spring Boot (console) com Maven
- Objetivo: ter um Spring Boot “CLI” inicial que sobe e executa um loop simples (placeholder da UI).
- Escopo:
  - `app/pom.xml` com Spring Boot (Maven).
  - Classe`Main` com`CommandLineRunner` (ou`ApplicationRunner` ) para iniciar a aplicação console.
  - Pacote base definido (ex.:`br.edu.pucminas.matricula` ).
- Critérios de aceite:
  - Build do JAR funciona dentro do Docker.
  - Ao subir, app imprime algo como “Sistema de Matrículas iniciado” e aguarda input (mesmo que ainda rudimentar).
### INFRA-03 — Dockerfile multi-stage (build dentro do container)
- Objetivo: garantir “zero dependências locais”: compila e roda via Docker.
- Escopo:
  - Stage 1: imagem Maven + Temurin para`mvn package`
  - Stage 2: imagem JRE (Temurin JRE) rodando o`.jar`
  - Expor apenas o necessário (mesmo sendo console, o processo precisa rodar em foreground).
- Critérios de aceite:
  - `docker compose up --build` compila sem precisar de Java/Maven local.
  - Container da app inicia sem erro e permanece ativo.
### INFRA-04 — docker-compose com Postgres + App
- Objetivo: subir stack mínima:`db` +`app` .
- Escopo:
  - Serviço`db` (postgres) com volume nomeado.
  - Serviço`app` buildando via Dockerfile.
  - Variáveis de ambiente padronizadas (DB name/user/pass).
  - Healthcheck no Postgres + dependência da app (subir após db “healthy”).
- Critérios de aceite:
  - `db` fica healthy.
  - `app` conecta no Postgres e inicia corretamente.
### INFRA-05 — Configuração Spring por profile “docker”
- Objetivo: separar config local/geral de config docker.
- Escopo:
  - `application.yml` (defaults)
  - `application-docker.yml` (datasource apontando para`db:5432` )
  - `SPRING_PROFILES_ACTIVE=docker` no compose.
- Critérios de aceite:
  - App sobe em Docker usando o profile correto e conecta no Postgres sem hardcode de localhost.
### INFRA-06 — Migrações com Flyway (baseline)
- Objetivo: garantir banco versionado desde o primeiro commit.
- Escopo:
  - Dependência Flyway no`pom.xml` .
  - Pasta`db/migration` com migração inicial (pode começar com tabelas mínimas de autenticação/usuário, para validar pipeline).
- Critérios de aceite:
  - Ao subir`docker compose` , Flyway executa e o schema aparece no Postgres.
  - Re-subir não quebra (migrações idempotentes por versão).
### INFRA-07 — Padronizar logs e encerramento do app (console)
- Objetivo: evitar container “morrer” e permitir interação via`docker attach` /stdin quando necessário.
- Escopo:
  - Configurar leitura de stdin (loop) no runner.
  - Encerramento gracioso ao escolher “sair”.
- Critérios de aceite:
  - App mantém sessão de console ativa e responde a inputs simples dentro do container.
### INFRA-08 — CI local via Docker (comandos padrão)
- Objetivo: definir comandos únicos e repetíveis (sem scripts extras obrigatórios).
- Comandos (Windows / PowerShell):
  - Subir:`docker compose up --build`
  - Derrubar:`docker compose down`
  - Derrubar + limpar volume:`docker compose down -v`
  - Ver logs app:`docker compose logs -f app`
- Critérios de aceite:
  - Qualquer integrante consegue rodar do zero apenas com Docker instalado.
## Épico E1 — Modelo e Persistência (alinhado ao Diagrama de Classes) Só começar após E0 completo (porque tudo depende do banco e do build).
### DOMAIN-01 — Congelar mapeamento do Diagrama de Classes → Entidades
- Objetivo: listar classes/atributos/relacionamentos exatamente como no diagrama e definir IDs e cardinalidades.
- Critérios de aceite:
  - Tabela de rastreio “Classe UML → Entidade Java → Tabela SQL”.
### DB-01 — Schema completo (migrações) conforme diagrama
- Objetivo: criar tabelas e constraints para cursos/disciplinas/professores/alunos/semestres/ofertas/matrículas.
- Critérios de aceite:
  - Migrações criam schema completo.
  - Constraints para impedir duplicidades óbvias (ex.: matrícula duplicada na mesma oferta).
### REPO-01 — Repositórios (interfaces) e implementação JPA
- Objetivo: CRUD básico para suportar UC02–UC06 primeiro.
- Critérios de aceite:
  - Teste de integração simples (subindo contexto Spring) validando persistência.
## Épico E2 — Use Cases (implementação 1:1 com UC01–UC12)
### UC01-01 — Login + sessão
- Critérios de aceite: valida credenciais (RN07), cria sessão, redireciona menu por perfil.
### UC02–UC05 — Cadastros (Secretaria)
- Critérios de aceite: somente secretaria executa (RN08), CRUD completo, validações.
### UC06 — Gerar currículo/abrir período
- Critérios de aceite: cria ofertas do semestre e marca período aberto.
(UC07–UC12 entram depois de UI base e modelo estabilizados.)

## Épico E3 — Console UI navegável
### UI-01 — Motor de navegação (menus + voltar/home)
### UI-02 — Telas por perfil (Aluno/Professor/Secretaria)
- Critérios de aceite: fluxo navegável e consistente, com validação de input.
## Épico E4 — Regras críticas (concorrência, vagas, segurança, cobrança)
- CONC-01: RNF04/RN05 (garantir nunca passar de 60)
- SEC-01: RNF05 (hash de senha)
- BILL-01: UC11 (mock do sistema de cobranças em container + estratégia de entrega)