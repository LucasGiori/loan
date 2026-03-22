# Integrations Guide

## Fluxo Esperado

```
POST /loans/initialize  →  POST /loans/proposals/generate  →  GET /loans/{loanId}/available
```

---

## Endpoints

### 1. Inicializar Empréstimo

```bash
curl --request POST \
  --url http://localhost:80/loans/initialize \
  --header 'Content-Type: application/json' \
  --data '{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "customer": {
      "name": "João da Silva",
      "document": "123.456.789-00",
      "age": 35,
      "location": "São Paulo",
      "profile": "EMPLOYEE",
      "income": 5000.00
    }
  }'
```

**Resposta:** `201 Created` sem body.

---

### 2. Gerar Propostas

```bash
curl --request POST \
  --url http://localhost:80/loans/proposals/generate \
  --header 'Content-Type: application/json' \
  --data '{
    "id": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

**Resposta:** `201 Created` sem body. O `id` deve ser o mesmo usado no initialize.

---

### 3. Consultar Propostas Disponíveis

```bash
curl --request GET \
  --url http://localhost:80/loans/550e8400-e29b-41d4-a716-446655440000/available \
  --header 'Accept: application/json'
```

**Resposta `200 OK`:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "AVAILABLE",
  "proposals": [...],
  "version": 1
}
```

**Resposta `404 Not Found`:** quando o loan não existir.

---

## Referência

**Valores válidos para `profile`:** `EMPLOYEE`, `PUBLIC_SERVER`, `RETIREE`, `PENSIONER`, `SELF_EMPLOYED`