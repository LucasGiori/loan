#!/bin/sh
set -e

CONNECT_URL="http://kafka-connect:8083"

echo "Waiting for Kafka Connect..."
until curl -s "${CONNECT_URL}/connectors" > /dev/null 2>&1; do
  echo "  Not ready yet, retrying in 5s..."
  sleep 5
done

echo "Kafka Connect is ready."

CONNECTOR_NAME="loan-outbox-connector"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" "${CONNECT_URL}/connectors/${CONNECTOR_NAME}")

if [ "$STATUS" = "200" ]; then
  echo "Connector '${CONNECTOR_NAME}' already exists. Skipping."
else
  echo "Registering connector '${CONNECTOR_NAME}'..."
  curl -s -X POST "${CONNECT_URL}/connectors" \
    -H "Content-Type: application/json" \
    -d @/debezium/connector.json
  echo ""
  echo "Connector registered successfully."
fi
