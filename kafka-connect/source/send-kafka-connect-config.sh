#!/bin/bash

CONFIG_FILE="jdbc-source-connector-config.json"

KAFKA_CONNECT_URL="http://localhost:8083/connectors"

if [ ! -f "$CONFIG_FILE" ]; then
  echo "Arquivo de configuração JSON não encontrado: $CONFIG_FILE"
  exit 1
fi

echo "Enviando configuração para o Kafka Connect..."
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" --data @"$CONFIG_FILE" "$KAFKA_CONNECT_URL"

if [ $? -eq 0 ]; then
  echo "Configuração enviada com sucesso!"
else
  echo "Falha ao enviar a configuração para o Kafka Connect."
  exit 1
fi