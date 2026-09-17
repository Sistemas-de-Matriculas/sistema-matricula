# Sistema de Matrículas — Especificação Funcional

> **Curso:** Engenharia de Software — PUC Minas
> **Disciplina:** Projeto de Software
> **Professora:** Milena Menezes Adão
> **Sprint:** Lab01S01 — Laboratório 1 (2º Semestre / 2026)
> **Integrantes:** *(preencher com os nomes do grupo)*

## Sumário

1. [Introdução](#1-introdução)
2. [Visão Geral do Sistema](#2-visão-geral-do-sistema)
3. [Atores do Sistema](#3-atores-do-sistema)
4. [Glossário](#4-glossário)
5. [Regras de Negócio](#5-regras-de-negócio)
6. [Requisitos Funcionais](#6-requisitos-funcionais)
7. [Requisitos Não Funcionais](#7-requisitos-não-funcionais)
8. [Diagrama de Casos de Uso](#8-diagrama-de-casos-de-uso)
9. [Diagrama de Classe](#9-diagrama-de-classe)
10. [Especificação dos Casos de Uso](#10-especificação-dos-casos-de-uso)
11. [Histórias de Usuário](#11-histórias-de-usuário)
12. [Matriz de Rastreabilidade](#12-matriz-de-rastreabilidade)
13. [Roadmap das Sprints](#13-roadmap-das-sprints)

---

## 1. Introdução

### 1.1 Objetivo

Este documento apresenta a especificação funcional do **Sistema de Matrículas**, elaborada a partir da descrição fornecida pelo Product Owner. Ele reúne o modelo de análise do sistema — atores, regras de negócio, requisitos, casos de uso e histórias de usuário — servindo como base para as etapas seguintes de modelagem estrutural (diagrama de classes) e implementação.

### 1.2 Escopo

O sistema tem como escopo automatizar o processo de matrícula de uma universidade, permitindo que a secretaria mantenha o cadastro acadêmico (cursos, disciplinas, professores e alunos) e gere o currículo de cada semestre; que alunos se matriculem e cancelem matrículas em disciplinas dentro do período vigente; e que professores consultem os alunos matriculados em suas disciplinas. O sistema também deve notificar um sistema externo de cobranças sempre que uma matrícula for efetivada.

### 1.3 Documento de Referência

Especificação original fornecida pelo Product Owner: *Laboratório 1 — Segundo Semestre/2026, Disciplina de Projeto de Software.*

---

## 2. Visão Geral do Sistema

A secretaria da universidade gera o currículo de cada semestre e mantém as informações sobre disciplinas, professores e alunos. Cada curso possui nome, número de créditos e é composto por diversas disciplinas.

Durante os períodos de matrícula, alunos podem se matricular em até 4 disciplinas obrigatórias (1ª opção) e até 2 disciplinas optativas (alternativas), bem como cancelar matrículas feitas anteriormente. Uma disciplina só é confirmada para o semestre seguinte se atingir, ao final do período de matrículas, pelo menos 3 alunos inscritos; caso contrário, é cancelada. O limite máximo de vagas por disciplina é de 60 alunos, encerrando-se as inscrições ao atingir esse número.

Após a matrícula, o sistema de cobranças é notificado para que o aluno seja cobrado pelas disciplinas do semestre. Professores podem consultar os alunos matriculados em suas disciplinas. Todos os usuários autenticam-se por login e senha.

---

## 3. Atores do Sistema

| Ator | Descrição |
|---|---|
| **Aluno** | Usuário que se matricula e cancela matrículas em disciplinas dentro dos períodos de matrícula. |
| **Professor** | Usuário que consulta os alunos matriculados nas disciplinas que leciona. |
| **Secretaria** | Usuário responsável por manter os cadastros de cursos, disciplinas, professores e alunos, e por gerar o currículo do semestre. |
| **Sistema de Cobranças** | Sistema externo, notificado pelo Sistema de Matrículas para cobrar o aluno pelas disciplinas do semestre. |

---

## 4. Glossário

| Termo | Definição |
|---|---|
| **Curso** | Agrupamento de disciplinas com nome e número de créditos definidos. |
| **Disciplina** | Unidade curricular ofertada em um semestre, vinculada a um curso e a um professor. |
| **Currículo** | Conjunto de disciplinas ofertadas pela secretaria em um determinado semestre. |
| **Matrícula** | Vínculo formal de um aluno a uma disciplina em um dado semestre. |
| **Período de Matrículas** | Intervalo de tempo em que alunos podem se matricular e/ou cancelar matrículas. |
| **Disciplina obrigatória (1ª opção)** | Uma das até 4 disciplinas exigidas na matrícula do aluno. |
| **Disciplina optativa (alternativa)** | Uma das até 2 disciplinas eletivas que o aluno pode escolher. |

---

## 5. Regras de Negócio

| ID | Regra |
|---|---|
| RN01 | Cada curso possui nome, número de créditos e é constituído por diversas disciplinas. |
| RN02 | Um aluno pode se matricular em, no máximo, 4 disciplinas obrigatórias (1ª opção) e 2 disciplinas optativas (alternativas) por semestre. |
| RN03 | Matrículas e cancelamentos só podem ser realizados durante o período de matrículas vigente. |
| RN04 | Uma disciplina só fica ativa para o semestre seguinte se tiver, ao final do período de matrículas, no mínimo 3 alunos matriculados; caso contrário, é cancelada. |
| RN05 | O número máximo de alunos matriculados em uma disciplina é 60; ao atingir esse número, as inscrições na disciplina são automaticamente encerradas. |
| RN06 | Após a matrícula de um aluno, o Sistema de Cobranças deve ser notificado para gerar a cobrança das disciplinas do semestre. |
| RN07 | Todos os usuários do sistema (alunos, professores e secretaria) possuem senha para validação de login. |
| RN08 | Somente a secretaria pode gerar o currículo do semestre e cadastrar cursos, disciplinas, professores e alunos. |
| RN09 | Um professor só pode consultar os alunos matriculados nas disciplinas que ele próprio leciona. |

---

## 6. Requisitos Funcionais

| ID | Descrição | Prioridade |
|---|---|---|
| RF01 | O sistema deve permitir que qualquer usuário efetue login mediante usuário e senha. | Alta |
| RF02 | O sistema deve permitir que a secretaria cadastre cursos (nome, número de créditos). | Alta |
| RF03 | O sistema deve permitir que a secretaria cadastre disciplinas, vinculando-as a um curso e a um professor. | Alta |
| RF04 | O sistema deve permitir que a secretaria cadastre professores. | Média |
| RF05 | O sistema deve permitir que a secretaria cadastre alunos. | Média |
| RF06 | O sistema deve permitir que a secretaria gere o currículo (conjunto de disciplinas ofertadas) de cada semestre. | Alta |
| RF07 | O sistema deve permitir que o aluno consulte as disciplinas ofertadas no semestre, com o número de vagas disponíveis. | Alta |
| RF08 | O sistema deve permitir que o aluno se matricule em disciplinas obrigatórias e optativas, respeitando o limite de 4 + 2 (RN02). | Alta |
| RF09 | O sistema deve permitir que o aluno cancele matrículas realizadas anteriormente, dentro do período de matrículas. | Alta |
| RF10 | O sistema deve impedir novas matrículas em uma disciplina que já atingiu 60 alunos matriculados (RN05). | Alta |
| RF11 | O sistema deve, ao final do período de matrículas, cancelar automaticamente as disciplinas com menos de 3 alunos matriculados (RN04). | Alta |
| RF12 | O sistema deve notificar o Sistema de Cobranças sempre que um aluno se matricular em disciplinas (RN06). | Alta |
| RF13 | O sistema deve permitir que o professor consulte a lista de alunos matriculados em cada disciplina que leciona. | Média |

---

## 7. Requisitos Não Funcionais

| ID | Descrição |
|---|---|
| RNF01 | O sistema final deve ser desenvolvido na linguagem Java. |
| RNF02 | Na versão de protótipo, a interface pode ser em linha de comando (CLI). |
| RNF03 | Na versão de protótipo, a persistência de dados pode ser feita em arquivos. |
| RNF04 | O sistema deve tratar corretamente o acesso concorrente de múltiplos alunos tentando se matricular na mesma disciplina, garantindo que o limite de 60 vagas nunca seja ultrapassado. |
| RNF05 | As senhas dos usuários não devem ser armazenadas em texto puro. |
| RNF06 | O código-fonte e os modelos UML devem ser mantidos versionados e atualizados em um repositório GitHub. |

---

## 8. Diagrama de Casos de Uso

<img width="2720" height="2960" alt="diagrama_caso_uso_matricula_universidade" src="https://github.com/user-attachments/assets/3d0eff33-3d46-483f-b283-db61500b5112" />

---
## 9. Diagrama de Classe
<img width="878" height="941" alt="WhatsApp Image 2026-09-16 at 2 10 23 PM" src="https://github.com/user-attachments/assets/eebf86e3-02a3-494b-b24c-fdf014b476fa" />
---

## 10. Especificação dos Casos de Uso

### UC01 — Efetuar Login

- **Atores:** Aluno, Professor, Secretaria
- **Pré-condições:** O usuário deve possuir cadastro prévio no sistema.
- **Pós-condições:** O usuário é autenticado e direcionado às funcionalidades do seu perfil.
- **Fluxo principal:**
  1. O usuário informa login e senha.
  2. O sistema valida as credenciais (RN07).
  3. O sistema apresenta o menu de funcionalidades correspondente ao perfil do usuário.
- **Fluxo de exceção:** Credenciais inválidas → o sistema exibe mensagem de erro e permite nova tentativa.

### UC02 a UC05 — Manutenção de Cadastros (Curso, Disciplina, Professor, Aluno)

- **Ator:** Secretaria
- **Pré-condições:** Login efetuado (UC01) com perfil de secretaria.
- **Pós-condições:** O registro (curso, disciplina, professor ou aluno) é incluído, alterado ou removido da base de dados.
- **Fluxo principal:**
  1. A secretaria seleciona a opção de cadastro desejada.
  2. A secretaria informa os dados do registro (ex.: nome e número de créditos, no caso de curso; disciplina vinculada a curso e professor, no caso de disciplina).
  3. O sistema valida e persiste os dados (RN01).
- **Observação:** cada cadastro é tratado individualmente na modelagem estrutural (diagrama de classes), mas segue o mesmo padrão de fluxo (incluir/alterar/consultar/remover).

### UC06 — Gerar Currículo do Semestre

- **Ator:** Secretaria
- **Pré-condições:** Cursos, disciplinas e professores previamente cadastrados.
- **Pós-condições:** O currículo do semestre — conjunto de disciplinas ofertadas — fica disponível para consulta e matrícula dos alunos.
- **Fluxo principal:**
  1. A secretaria seleciona as disciplinas a ofertar no semestre.
  2. O sistema abre o período de matrículas para o currículo gerado.

### UC07 — Consultar Disciplinas Ofertadas

- **Ator:** Aluno
- **Pré-condições:** Currículo do semestre gerado (UC06); período de matrículas aberto.
- **Pós-condições:** O aluno visualiza as disciplinas ofertadas e o número de vagas restantes em cada uma.

### UC08 — Matricular-se em Disciplina

- **Ator:** Aluno
- **Inclui:** UC01 (Efetuar Login), UC11 (Notificar Sistema de Cobranças)
- **Pré-condições:** Login efetuado; período de matrículas aberto.
- **Pós-condições:** O aluno é vinculado à(s) disciplina(s) escolhida(s); o Sistema de Cobranças é notificado.
- **Fluxo principal:**
  1. O aluno seleciona até 4 disciplinas obrigatórias e até 2 optativas (RN02).
  2. O sistema verifica, para cada disciplina, se o limite de 60 vagas já foi atingido (RN05).
  3. O sistema confirma a matrícula e a persiste.
  4. O sistema aciona UC11 para notificar o Sistema de Cobranças (RN06).
- **Fluxos de exceção:**
  - Disciplina com vagas esgotadas → o sistema informa o aluno e impede a matrícula nessa disciplina (RF10).
  - Limite de disciplinas do aluno excedido (mais de 4 obrigatórias ou 2 optativas) → o sistema rejeita a solicitação.
  - Período de matrículas encerrado → o sistema impede a operação.

### UC09 — Cancelar Matrícula

- **Ator:** Aluno
- **Inclui:** UC01 (Efetuar Login)
- **Pré-condições:** Aluno matriculado em ao menos uma disciplina no semestre; período de matrículas aberto.
- **Pós-condições:** O vínculo do aluno com a disciplina é removido.
- **Fluxo de exceção:** Período de matrículas encerrado → o sistema impede o cancelamento.

### UC10 — Consultar Alunos Matriculados

- **Ator:** Professor
- **Inclui:** UC01 (Efetuar Login)
- **Pré-condições:** Professor autenticado e responsável pela disciplina consultada (RN09).
- **Pós-condições:** O professor visualiza a lista de alunos matriculados na disciplina.

### UC11 — Notificar Sistema de Cobranças

- **Ator:** Sistema de Cobranças (sistema externo)
- **Disparado por:** UC08 (Matricular-se em Disciplina)
- **Pós-condições:** O Sistema de Cobranças recebe os dados necessários para cobrar o aluno pelas disciplinas do semestre (RN06).

### UC12 — Processar Encerramento do Período de Matrículas

- **Ator:** Secretaria / processamento automático do sistema
- **Pré-condições:** Período de matrículas chegou ao seu término.
- **Pós-condições:** Disciplinas com pelo menos 3 alunos matriculados são confirmadas para o semestre seguinte; disciplinas com menos de 3 alunos são canceladas (RN04).
- **Fluxo principal:**
  1. O sistema verifica o número de alunos matriculados em cada disciplina do currículo.
  2. Disciplinas com 3 ou mais alunos são marcadas como ativas.
  3. Disciplinas com menos de 3 alunos são canceladas e os alunos matriculados nelas são notificados.

---

## 11. Histórias de Usuário

### Aluno

- **US01** — Como aluno, quero efetuar login no sistema, para acessar minhas funcionalidades de matrícula.
- **US02** — Como aluno, quero me matricular em disciplinas obrigatórias e optativas, para cursar as disciplinas do semestre.
  - *Critérios de aceite:* respeita o limite de 4 obrigatórias + 2 optativas; impede matrícula em disciplina com 60 vagas ocupadas; dispara a notificação de cobrança.
- **US03** — Como aluno, quero cancelar uma matrícula feita anteriormente, para ajustar minha grade durante o período de matrículas.
  - *Critérios de aceite:* só é permitido dentro do período de matrículas vigente.
- **US04** — Como aluno, quero visualizar as disciplinas ofertadas e as vagas restantes, para decidir em quais me matricular.
- **US05** — Como aluno, quero que a cobrança das minhas disciplinas seja gerada automaticamente após a matrícula, para saber os valores a pagar no semestre.

### Professor

- **US06** — Como professor, quero efetuar login no sistema, para acessar minhas funcionalidades.
- **US07** — Como professor, quero consultar os alunos matriculados nas disciplinas que leciono, para me preparar para o semestre.

### Secretaria

- **US08** — Como funcionário da secretaria, quero cadastrar cursos, disciplinas, professores e alunos, para manter as informações acadêmicas atualizadas.
- **US09** — Como funcionário da secretaria, quero gerar o currículo de cada semestre, para disponibilizar as disciplinas ofertadas aos alunos.
- **US10** — Como funcionário da secretaria, quero que o sistema encerre automaticamente as matrículas de uma disciplina ao atingir 60 alunos, para respeitar a capacidade máxima da turma.
- **US11** — Como funcionário da secretaria, quero que o sistema cancele automaticamente disciplinas com menos de 3 alunos inscritos ao final do período de matrículas, para evitar a abertura de turmas inviáveis.

### Sistema de Cobranças

- **US12** — Como sistema de cobranças, quero ser notificado automaticamente quando um aluno se matricula em disciplinas, para gerar a cobrança correspondente.

---

## 12. Matriz de Rastreabilidade

| Requisito Funcional | Caso de Uso | História de Usuário | Regra de Negócio |
|---|---|---|---|
| RF01 | UC01 | US01, US06 | RN07 |
| RF02 | UC02 | US08 | RN01 |
| RF03 | UC03 | US08 | RN01 |
| RF04 | UC04 | US08 | — |
| RF05 | UC05 | US08 | — |
| RF06 | UC06 | US09 | RN08 |
| RF07 | UC07 | US04 | — |
| RF08 | UC08 | US02 | RN02, RN03 |
| RF09 | UC09 | US03 | RN03 |
| RF10 | UC08 | US02 | RN05 |
| RF11 | UC12 | US11 | RN04 |
| RF12 | UC11 | US05, US12 | RN06 |
| RF13 | UC10 | US07 | RN09 |

---

## 13. Roadmap das Sprints

| Sprint | Entrega | Pontos |
|---|---|---|
| Lab01S01 | Diagrama de Caso de Uso + Histórias de Usuário (este documento) | 4 |
| Lab01S02 | Correção dos diagramas + Diagrama de Classes + criação do projeto Java (classes, atributos e stubs dos métodos) | 4 |
| Lab01S03 | Correção dos diagramas + protótipo funcional (interface em CLI e persistência em arquivos) | 7 |
| Apresentação Final | Comparação entre o modelo inicial e o protótipo final desenvolvido em Java | 5 |
