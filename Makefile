DOCKER_COMPOSE = docker-compose
NEXUS_URL = http://localhost:8081

.PHONY: all up build-artifacts start stop clean logs

all: up build-artifacts start

ifeq ($(OS),Windows_NT)
WAIT_CMD = powershell -Command "while ($$true) { \
		try { \
			Invoke-WebRequest -UseBasicParsing -Uri $(NEXUS_URL)/service/rest/v1/status -ErrorAction Stop; \
			break \
		} \
		catch { \
			Write-Host 'Nexus not ready, sleeping...'; \
			Start-Sleep -Seconds 5 \
		} \
	}"
else
WAIT_CMD = until curl -sf $(NEXUS_URL)/service/rest/v1/status; do \
echo 'Nexus not ready, sleeping...'; sleep 5; \
done
endif

up:
	$(DOCKER_COMPOSE) up -d nexus nexus-change-password
	@echo "Waiting for Nexus to be healthy..."
	@$(WAIT_CMD)
	@echo "Nexus is healthy!"

build-artifacts:
	@$(DOCKER_COMPOSE) run --rm common

# Шаг 3: Запуск всех остальных сервисов
start:
	$(DOCKER_COMPOSE) up -d

# Остановка и очистка
#stop:
# $(DOCKER_COMPOSE) down
#
#clean: stop
# $(DOCKER_COMPOSE) rm -f
# docker volume rm $$(docker volume ls -qf dangling=true) 2>/dev/null || true
# rm -rf ./person-service/build ./common/build
#
## Просмотр логов
#logs:
# $(DOCKER_COMPOSE) logs -f --tail=200
#
## Полный rebuild (полная очистка, пересборка и старт)
#rebuild: clean all