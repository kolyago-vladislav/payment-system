cd /kafka-connect

CONNECT_URL="http://kafka-connect:8083/connectors"
CONFIG_FILE="connector.json"

if [ ! -f "$CONFIG_FILE" ]; then
  echo "Файл конфигурации $CONFIG_FILE не найден!"
  exit 1
fi

echo "Отправляем конфигурацию коннектора в Kafka Connect..."

curl -X POST -H "Content-Type: application/json" --data @"$CONFIG_FILE" "$CONNECT_URL" && \
echo -e "\nКоннектор успешно создан!" || \
echo -e "\nОшибка при создании коннектора."