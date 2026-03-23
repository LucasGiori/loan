# Kafka + Debezium + Outbox — Setup Guide

## Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────────────────────┐
│                          Aplicação Quarkus                          │
│                                                                     │
│  Handler → Repository → MySQL (loan) + MySQL (outbox_event)        │
└───────────────────────────────┬─────────────────────────────────────┘
                                │ INSERT
                                ▼
                    ┌──────────────────────┐
                    │   MySQL outbox_event  │
                    └──────────┬───────────┘
                               │ CDC (binlog)
                               ▼
                    ┌──────────────────────┐
                    │   Debezium Connect   │
                    │ (Outbox Event Router) │
                    └──────────┬───────────┘
                               │ publica
                               ▼
                    ┌──────────────────────┐
                    │        Kafka         │
                    │  loan.Loan.events    │
                    └──────────┬───────────┘
                               │ consome
                               ▼
                    ┌──────────────────────┐
                    │  Consumer(s)         │
                    │  (outros serviços)   │
                    └──────────────────────┘
```

O padrão **Transactional Outbox** garante que o evento só chega ao Kafka se a transação no banco foi commitada com sucesso. O Debezium lê o binlog do MySQL (CDC) e publica automaticamente.

---

## Avro ou JSON?

### Avro
**Prós:**
- Schema evolution com backward/forward compatibility
- Payload binário (menor tamanho)
- Validação de contrato entre produtor e consumidor
- Integra com Schema Registry

**Contras:**
- Requer Schema Registry como infraestrutura adicional
- Consumidores precisam de biblioteca Avro
- Mais complexo de debugar (payload não é legível)

### JSON ✅ (escolha deste projeto)
**Prós:**
- Já usado em todo o projeto (payload do outbox já é JSON)
- Legível diretamente no Kafka UI
- Sem dependência de Schema Registry
- Consumidores em qualquer linguagem parseiam facilmente

**Contras:**
- Payload maior (irrelevante neste contexto)
- Sem validação de schema em runtime

**Conclusão:** Para este projeto, JSON é a escolha correta. O payload na tabela `outbox_event` já é JSON serializado, e o Debezium vai publicá-lo diretamente no tópico sem transformação adicional. Avro faz sentido quando há múltiplos times consumindo o mesmo tópico com contratos de schema rigorosos.

---

## Serviços adicionados ao docker-compose

| Serviço | Imagem | Porta | Função |
|---|---|---|---|
| `zookeeper` | `confluentinc/cp-zookeeper:7.6.0` | 2181 | Coordenação do Kafka |
| `kafka` | `confluentinc/cp-kafka:7.6.0` | 29092 (externo) | Broker de mensagens |
| `kafka-connect` | `debezium/connect:2.7` | 8083 | Lê binlog e publica no Kafka |
| `init-connector` | `curlimages/curl` | — | Registra o connector automaticamente |
| `kafka-ui` | `provectus/kafka-ui` | 8090 | Interface web para visualizar tópicos |

> **Nota sobre Control Center:** O Confluent Control Center faz parte da Confluent Platform Enterprise e requer licença para uso em produção. O `kafka-ui` da Provectus é gratuito, open-source e oferece as mesmas funcionalidades relevantes para desenvolvimento.

---

## Pré-requisitos

O MySQL precisa ter o binlog habilitado com formato ROW. Isso já está configurado no `docker-compose.yml` via flags:

```yaml
command:
  - --server-id=1
  - --log-bin=mysql-bin
  - --binlog-format=ROW
  - --binlog-row-image=FULL
```

O usuário `debezium` com permissões de replicação é criado automaticamente via `debezium/debezium-user.sql` no init do container MySQL.

---

## Como subir o ambiente

```bash
# 1. Criar a rede (se ainda não existir)
docker network create loan_app

# 2. Subir todos os serviços
docker-compose up -d

# 3. Acompanhar o registro do connector
docker logs loan-init-connector -f
```

O serviço `init-connector` aguarda o Kafka Connect estar saudável e registra o connector automaticamente. Você verá no log:

```
Kafka Connect is ready.
Registering connector 'loan-outbox-connector'...
Connector registered successfully.
```

---

## Registrar o connector manualmente (opcional)

Caso queira registrar ou recriar o connector sem reiniciar o compose:

```bash
# Verificar se o Connect está pronto
curl http://localhost:8083/connectors

# Registrar o connector
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d @debezium/connector.json

# Verificar status
curl http://localhost:8083/connectors/loan-outbox-connector/status | jq

# Deletar e recriar (se necessário)
curl -X DELETE http://localhost:8083/connectors/loan-outbox-connector
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d @debezium/connector.json
```

---

## Tópicos gerados

O Debezium Outbox Event Router roteia cada evento com base no campo `des_aggregate_type` da tabela `outbox_event`.

| `des_aggregate_type` | Tópico Kafka |
|---|---|
| `Loan` | `loan.Loan.events` |

O padrão de nomenclatura é configurado em `connector.json`:
```json
"transforms.outbox.route.topic.replacement": "loan.${routedByValue}.events"
```

Para adicionar novos aggregates no futuro, basta que o campo `des_aggregate_type` tenha o valor correto — o tópico será criado automaticamente.

---

## Estrutura da mensagem no tópico

**Key** (string): valor do campo `identity` da tabela — o UUID do loan.

**Value** (JSON string): valor do campo `payload` — o evento serializado.

Exemplo de mensagem no tópico `loan.Loan.events`:

```
Key:   "550e8400-e29b-41d4-a716-446655440000"

Value: {
  "loanId": { "value": "550e8400-e29b-41d4-a716-446655440000" },
  "version": { "value": 3 },
  "proposals": [...],
  "amount": { "value": 15000.00 },
  "tax": { "value": 0.029 }
}
```

---

## Interface Web — Kafka UI

Acesse: [http://localhost:8090](http://localhost:8090)

Funcionalidades:
- Visualizar tópicos e mensagens em tempo real
- Inspecionar consumers e consumer groups
- Verificar status do Kafka Connect e connectors
- Produzir mensagens manualmente para testes

---

## Configuração do connector (`debezium/connector.json`)

```json
{
  "name": "loan-outbox-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "snapshot.mode": "schema_only",          ← não re-publica eventos antigos
    "table.include.list": "loan.outbox_event", ← monitora apenas a tabela de outbox
    "transforms": "outbox",
    "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
    "transforms.outbox.table.field.event.id": "cod_outbox_event",
    "transforms.outbox.table.field.event.key": "identity",
    "transforms.outbox.table.field.event.payload": "payload",
    "transforms.outbox.route.by.field": "des_aggregate_type",
    "transforms.outbox.route.topic.replacement": "loan.${routedByValue}.events"
  }
}
```

> **`snapshot.mode: schema_only`** — O Debezium aprende a estrutura da tabela mas não re-publica registros existentes. Apenas INSERTs novos são publicados. Use `initial` se quiser replay completo do outbox (não recomendado em produção).

---

## Troubleshooting

### Connector em estado FAILED
```bash
curl http://localhost:8083/connectors/loan-outbox-connector/status | jq
# Verifique o campo "tasks[0].trace" para o erro
```

### Verificar se o binlog está habilitado no MySQL
```bash
docker exec loan-mysql mysql -u root -pdev -e "SHOW VARIABLES LIKE 'log_bin';"
docker exec loan-mysql mysql -u root -pdev -e "SHOW VARIABLES LIKE 'binlog_format';"
```

### Consumir mensagens do tópico pelo terminal
```bash
docker exec loan-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic loan.Loan.events \
  --from-beginning \
  --property print.key=true
```

### Listar tópicos existentes
```bash
docker exec loan-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list
```
