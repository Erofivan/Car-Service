# Car Service

Микросервисный backend-проект на Java/Spring Boot для управления автокаталогом и заказами.

## Состав проекта

- `storage-service` - каталог машин, комплектующих и сборочных заказов.
- `order-service` - заказы на склад, кастомные заказы и тест-драйвы.
- `contracts` - общие Kafka-события и контракты между сервисами.
- `e2e-tests` - end-to-end тесты всего потока через Docker Compose.

## Технологии

- Java 21
- Spring Boot 3.4
- Spring Security (OAuth2 Resource Server) + Keycloak
- PostgreSQL + Liquibase
- Kafka
- OpenTelemetry + Grafana/Tempo/Prometheus
- JUnit 5 + Testcontainers
- Gradle (wrapper)

## Предварительные требования

- JDK 21
- Docker + Docker Compose
- (Опционально) `make`, Postman/Insomnia для ручной проверки API

## Быстрый старт

1. Скопируйте переменные окружения:

```bash
cp .env.example .env
```

2. Поднимите инфраструктуру и сервисы:

```bash
docker compose up -d --build
```

Если `docker compose` сообщает об отсутствующих путях `services/order-service` или `services/storage-service`, обновите `build.context` в `docker-compose.yaml` на `./order-service` и `./storage-service` соответственно.

3. Проверьте доступность API:

- Order service: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Storage service: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

4. Остановите окружение:

```bash
docker compose down
```

## Локальный запуск без Docker

Каждый сервис может запускаться отдельно через `local` профиль (по умолчанию).  
По умолчанию ожидаются:

- `order-service` на `8081`
- `storage-service` на `8082`
- PostgreSQL, Kafka и Keycloak на локальных адресах из `application.yml`

Примеры запуска:

```bash
./gradlew :storage-service:bootRun
./gradlew :order-service:bootRun
```

## Тесты

Полная проверка:

```bash
./gradlew clean assemble
./gradlew test
./gradlew integrationTest
./gradlew :e2e-tests:e2eTest
```

`integrationTest` и `e2eTest` используют Testcontainers / Docker.

## Наблюдаемость

В `docker-compose.yaml` подключены компоненты observability:

- Grafana: [http://localhost:3000](http://localhost:3000)
- Prometheus: [http://localhost:9090](http://localhost:9090)
- Tempo: [http://localhost:3200](http://localhost:3200)

Конфиги находятся в `observability/`.

## Аутентификация

Для авторизации используется Keycloak. Realm импортируется из:

- `keycloak/realm-export.json`

Параметры администратора берутся из `.env` (`KC_BOOTSTRAP_ADMIN_USERNAME`, `KC_BOOTSTRAP_ADMIN_PASSWORD`).

## Полезные команды

```bash
# Сборка всех модулей
./gradlew clean build

# Юнит-тесты
./gradlew test

# Интеграционные тесты
./gradlew integrationTest

# E2E тесты
./gradlew :e2e-tests:e2eTest
```
