# Aplicação de empréstimos

## Descrição

A proposta da aplicação que vamos desenvolver em conjunto é disponibilizar a uma pessoa as modalidades de empréstimo as quais ela tem acesso de acordo com algumas variáveis.


### Empréstimo pessoal:
    - Nível de risco: alto
    - Regras:
        - Nessa modalidade, o valor máximo do empréstimo é de 25% da renda mensal do cliente baseado na média dos últimos 12 meses.
        - Nessa modalidade, o cliente não pode ter nenhuma restrição no SPC ou SERASA.
        - Pode ser disponibilizado para:
            - pensionista
            - aposentado
            - assalariado
            - servidor público
            - autonomo
    - Taxas:
        - 1.5% a.m. para clientes com renda mensal de até R$ 2.000,00
        - 1.3% a.m. para clientes com renda mensal de até R$ 5.000,00
        - 1.1% a.m. para clientes com renda mensal de até R$ 10.000,00
        - 0.9% a.m. para clientes com renda mensal acima de R$ 10.000,00

### Empréstimo com garantia:
    - Nível de risco: baixo
    - Regras:
        - Nessa modalidade, o valor máximo do empréstimo é de 50% do valor do bem dado em garantia.
        - Pode ser disponibilizado para:
            - pensionista
            - aposentado
            - assalariado
            - servidor público
            - autonomo
    - Taxas:
        - 1.2% a.m. para clientes com renda mensal de até R$ 2.000,00
        - 1.1% a.m. para clientes com renda mensal de até R$ 5.000,00
        - 1.0% a.m. para clientes com renda mensal de até R$ 10.000,00
        - 0.9% a.m. para clientes com renda mensal acima de R$ 10.000,00

### Empréstimo consignado:
    - Nível de risco: baixo
    - Regras:
        - Nessa modalidade, o valor máximo do empréstimo é de 30% do salário bruto do cliente.
        - Pode ser disponibilizado para:
            - pensionista
            - aposentado
            - assalariado
            - servidor público
    - Taxas:
        - 1.4% a.m. para clientes com renda mensal de até R$ 2.000,00
        - 1.3% a.m. para clientes com renda mensal de até R$ 5.000,00
        - 1.2% a.m. para clientes com renda mensal de até R$ 10.000,00
        - 1.1% a.m. para clientes com renda mensal acima de R$ 10.000,00
        - 0.9% a.m. para clientes com renda mensal acima de R$ 10.000,00

## Utilização da aplicação:
A aplicação deve receber como entrada essas informações:

**input:**
```json
{
  "customer": {
    "name": "Erikaya",
    "cpf": "123.456.789-10",
    "age": 29,
    "location": "BH",
    "profile": "assalariado",
    "income": 3000
  }
}
```

E deve responder essas informações:

**output:**

```json
{
  "customer": {
    "name": "Erikaya",
    "cpf": "123.456.789-10",
    "age": 29,
    "location": "BH",
    "income": 3000
  },
  "loans": [
    {
      "type": "personal",
      "value": 750,
      "tax": 1.5
    },
    {
      "type": "guaranteed",
      "value": 1500,
      "tax": 1.2
    },
    {
      "type": "consignment",
      "value": 900,
      "tax": 1.4
    }
  ]
}
```
