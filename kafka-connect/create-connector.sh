cd /kafka-connect

CONNECT_URL="http://kafka-connect:8083/connectors"
CONFIG_FILE_SHAR_0="connector-shard-0.json"

if [ ! -f "$CONFIG_FILE_SHAR_0" ]; then
  echo "Файл конфигурации $CONFIG_FILE_SHAR_0 не найден!"
  exit 1
fi

echo "Отправляем конфигурацию коннектора в Kafka Connect..."

curl -X POST -H "Content-Type: application/json" --data @"$CONFIG_FILE_SHAR_0" "$CONNECT_URL" && \
echo -e "\nКоннектор успешно создан!" || \
echo -e "\nОшибка при создании коннектора."

CONFIG_FILE_SHAR_1="connector-shard-1.json"

if [ ! -f "$CONFIG_FILE_SHAR_1" ]; then
  echo "Файл конфигурации $CONFIG_FILE_SHAR_1 не найден!"
  exit 1
fi

echo "Отправляем конфигурацию коннектора в Kafka Connect..."

curl -X POST -H "Content-Type: application/json" --data @"$CONFIG_FILE_SHAR_1" "$CONNECT_URL" && \
echo -e "\nКоннектор успешно создан!" || \
echo -e "\nОшибка при создании коннектора."