# Loan Management System — Visão Geral do Projeto

API backend para gestão e emissão de propostas de empréstimo, construída com arquitetura moderna e boas práticas.

---

## Stack Tecnológica

| Componente | Tecnologia |
|---|---|
| Linguagem | Kotlin 1.9.10 |
| Framework | Quarkus 3.5.1 (reativo) |
| Build | Gradle com Kotlin DSL |
| Banco | MySQL com cliente reativo (Vertx) |
| Serialização | Kotlinx Serialization + Jackson |
| Deploy | Docker + Docker Compose |

---

## Arquitetura

O projeto implementa **quatro padrões arquiteturais combinados**:

1. **DDD (Domain-Driven Design)** — Aggregate Root `Loan`, Value Objects, Domain Events
2. **Arquitetura Hexagonal** — Ports & Adapters separando domínio de infraestrutura
3. **Event Sourcing** — Eventos como fonte de verdade, com reconstrução do agregado
4. **CQRS** — Write side via handlers/commands, read side via queries dedicadas

---

## Endpoints da API

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/loans/initialize` | Inicializa um empréstimo com dados do cliente |
| `POST` | `/loans/proposals/generate` | Gera propostas para um empréstimo |
| `GET` | `/loans/{loanId}/available` | Lista propostas disponíveis |

---

## Regras de Negócio

Três tipos de empréstimo com regras distintas:

- **Personal Loan** (alto risco) — Até 25% da renda mensal, validade 7 dias, sem SPC/SERASA, taxa 0.9–1.5%
- **Secured Loan** (baixo risco) — Até 50% do valor do colateral, validade 30 dias, taxa 0.9–1.2%
- **Payroll Loan/Consignado** (baixo risco) — Até 30% do salário bruto, validade 30 dias, não disponível para autônomos

**Perfis de cliente:** `EMPLOYEE`, `PUBLIC_SERVER`, `RETIREE`, `PENSIONER`, `SELF_EMPLOYED`

---

## Estrutura de Diretórios

```
src/main/kotlin/
├── application/
│   ├── commands/        # Objetos de comando
│   ├── domain/          # Modelos, eventos, exceções
│   ├── handlers/        # Handlers de casos de uso
│   └── ports/           # Interfaces inbound/outbound
├── driven/
│   ├── database/        # Repositório MySQL com event sourcing
│   └── logger/          # Adapter de logging
├── driver/
│   └── http/            # Endpoints REST
└── query/               # Read-side (CQRS)
```

---

## Máquina de Estados do Empréstimo

```
INITIALIZED ──issueProposals()──> AVAILABLE ──request()──> REQUESTED
                                      │
                              (ABANDONED | COMPLETED | CANCELED)
```

---

## Pontos de Destaque

- **Event Sourcing** com tabela `outbox_event` para confiabilidade e auditoria
- **Máquina de estados** no agregado `Loan` com transições controladas
- **Programação reativa** com coroutines e Vertx non-blocking I/O
- **84 arquivos Kotlin** bem organizados em camadas lógicas
- Padrões **Factory** e **Strategy** para as regras de empréstimo