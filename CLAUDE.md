# CLAUDE.md

Orientações para agentes de IA ao trabalhar com o código deste repositório.

## Orquestração de Trabalho

1. **Modo de Planejamento por Padrão**
   - Entre em modo de planejamento para QUALQUER tarefa não trivial (3+ passos ou decisões arquiteturais)
   - Se algo der errado, PARE e replaneje imediatamente — não continue empurrando
   - Use o modo de planejamento para etapas de verificação, não apenas para construção
   - Escreva especificações detalhadas antecipadamente para reduzir ambiguidade

2. **Estratégia de Subagentes**
   - Use subagentes liberalmente para manter o contexto principal limpo
   - Delegue pesquisa, exploração e análise paralela a subagentes
   - Para problemas complexos, utilize mais capacidade computacional via subagentes
   - Uma tarefa por subagente para execução focada

3. **Verificação Antes de Concluir**
   - Nunca marque uma tarefa como concluída sem provar que funciona
   - Execute a suíte completa de testes antes de considerar o trabalho pronto
   - Verifique suas alterações em relação ao comportamento existente
   - Pergunte a si mesmo: "Um engenheiro sênior aprovaria isso?"

4. **Exija Elegância (com equilíbrio)**
   - Para mudanças não triviais: pause e pergunte "há uma forma mais elegante?"
   - Se uma correção parecer um hack: "Com tudo que sei agora, implemente a solução elegante"
   - Pule isso para correções simples e óbvias — não complique desnecessariamente
   - Questione seu próprio trabalho antes de apresentá-lo

5. **Correção Autônoma de Bugs**
   - Ao receber um relatório de bug: corrija-o. Não peça orientação passo a passo
   - Execute os testes para identificar a causa raiz
   - Zero troca de contexto necessária do usuário
   - Corrija testes falhando sem precisar ser instruído sobre como fazê-lo

## Princípios Fundamentais

- **Simplicidade Primeiro:** Torne cada mudança o mais simples possível. Impacto mínimo no código.
- **Sem Preguiça:** Encontre causas raiz. Sem gambiarras temporárias. Padrão de desenvolvedor sênior.
- **Impacto Mínimo:** Mudanças devem tocar apenas o necessário. Evite introduzir bugs.

## Visão Geral

Sistema de originação de empréstimos desenvolvido como projeto de aprendizado de Kotlin com arquitetura de produção. Implementa **DDD + Hexagonal + Event Sourcing + CQRS** de forma combinada.

**Stack:**
- Kotlin 1.9.10 + Quarkus 3.5.1 + JDK 17
- MySQL 8.4.8 (Vertx reactive client)
- Kafka + Debezium (Outbox Pattern)
- kotlinx-serialization (domínio), Jackson (HTTP)
- JUnit 5 (testes)

---

## Arquitetura

### As 4 camadas (Hexagonal)

```
driver/         → Inbound adapters (REST Actions, Request DTOs)
application/    → Core: Commands, Handlers, Ports, Domain
driven/         → Outbound adapters (MySQL repository, Outbox DAO, Logger)
query/          → Read side (CQRS — handlers + DAOs de leitura)
starter/        → Config, serializers, exception handlers
```

### Fluxo de um comando (write side)

```
HTTP Request
  → Action (@Path)
  → Request.toCommand()
  → UseCase<Command>.execute()  (Port inbound)
  → Handler (implements Port + Handler<Command>)
  → repository.pull(id)         (reconstrói aggregate do DB)
  → aggregate.transition()      (retorna DomainEvent)
  → repository.push(event)      (persiste + outbox em transação)
```

### Fluxo de leitura (read side)

```
HTTP GET → Action → Handler → DAO (SQL direto, sem aggregate)
```

---

## Outbox Pattern

**Nunca publicar diretamente no Kafka.** Todo evento passa pelo outbox:

```
Handler.push(event)
  → INSERT loan (novo estado)         ─┐ mesma
  → INSERT outbox_event (payload)     ─┘ transação
       ↓
  MySQL binlog → Debezium CDC → Kafka (loan.outbox_event)
```

O Debezium usa `ExtractNewRecordState` para entregar apenas o `payload` (JSON limpo, sem envelope CDC).

---

## Convenções de Nomenclatura

| Tipo | Padrão | Exemplo |
|---|---|---|
| REST resource | `{Ação}Action` | `LoanInitAction` |
| DTO de entrada | `{Ação}Request` | `LoanInitRequest` |
| Porta inbound | `{Ação}Port` | `LoanInitPort` |
| Command | `{Ação}Command` | `LoanInitCommand` |
| Handler | `{Ação}Handler` | `LoanInitHandler` |
| Evento de domínio | `Loan{Particípio}Event` | `LoanInitializedEvent` |
| Estado do aggregate | `{Adjetivo}Loan` | `InitializedLoan` |
| Repository impl | `Mysql{Nome}Repository` | `MysqlLoanRepository` |
| DAO impl | `{Nome}MysqlDAO` | `GetLoanAvailableMysqlDAO` |

---

## Serialização

- **Domínio** (aggregate, eventos, value objects): `@Serializable` do `kotlinx-serialization`
  - Sealed types usam `@SerialName("NomeDaClasse")`
  - Tipos especiais precisam de serializer customizado em `starter/serializer/`
- **HTTP** (Request/Response DTOs): Jackson (sem anotações, só data classes)
- **BigDecimal, LocalDateTime, UUIDv4**: sempre usar os serializers customizados existentes

---

## Testes

- **Localização:** `src/test/kotlin/application/domain/`
- Todos os testes de domínio são **puros** — sem `@QuarkusTest`, sem I/O, sem banco
- Fixtures compartilhadas em `TestFixtures.kt`
- Comparar `BigDecimal` com `.compareTo()`, nunca com `==`
- Rodar: `make test`

---

## Como subir o projeto

```bash
# 1. Criar rede (só na primeira vez)
docker network create loan_app

# 2. Subir tudo
docker compose up -d

# 3. Rodar testes
make test
```
