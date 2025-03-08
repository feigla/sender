# Sender-app

**Sender-app** — это многомодульное приложение, построенное с использованием **Spring Boot, PostgreSQL, RabbitMQ и Redis**. Оно обрабатывает задачи, отправленные клиентами в асинхронном режиме и обеспечивает эффективную параллельную обработку с помощью **workers**.

## 📌 Содержание
1. [Архитектура](#архитектура)
2. [Модули](#модули)
   - [Client-api](#client-api)
   - [Dispatcher](#dispatcher)
   - [Worker](#worker)
   - [Дополнительные компоненты (RabbitMQ, Redis, База данных)](#дополнительные-компоненты-rabbitmq-redis-база-данных)

---

## 🏗 Архитектура

Приложение **Sender-app** состоит из нескольких взаимосвязанных модулей:

1. **Client-api** – принимает и сохраняет задачи в базе данных **PostgreSQL**.
2. **Dispatcher** – извлекает необработанные задачи из базы данных и отправляет их в очередь сообщений **RabbitMQ**.
3. **Worker** – получает задачи из очереди и выполняет их. Может быть запущено несколько worker-ов одновременно.
4. **Дополнительные компоненты** – включает конфигурации для **RabbitMQ, Redis** и базы данных.

---

## ⚙️ Модули

### 📌 Client-api

Модуль **Client-api** отвечает за:
- Приём новых задач через **REST API**.
- Сохранение задач в базе данных **PostgreSQL**.
- Делегирование обработки задач модулям **Dispatcher** и **Worker**.

---

### 📌 Dispatcher

Модуль **Dispatcher**:
- Периодически (каждые **5 секунд**) запрашивает из базы данных **ID** необработанных задач.
- Отправляет эти **ID** в очередь сообщений **RabbitMQ** для обработки worker-ами.

---

### 📌 Worker

Модуль **Worker**:
- Получает **ID** задач из очереди **RabbitMQ**.
- Выполняет задачи на основе полученного **ID**.
- Использует **Redis** для предотвращения одновременной обработки одной и той же задачи несколькими worker-ами (блокировка задач во время выполнения).

---

### 📌 Дополнительные компоненты (RabbitMQ, Redis, База данных)

Этот раздел включает:
- **Конфигурацию RabbitMQ** – для установления соединения с брокером сообщений.
- **Конфигурацию Redis** – для управления распределёнными блокировками и предотвращения обработки одной и той же задачи разными worker-ами.
- **Хранилище PostgreSQL** – для постоянного хранения задач.

---

**Sender-app** is a multi-module application built with Spring Boot, PostgreSQL, RabbitMQ, and Redis. It processes tasks submitted by clients asynchronously and ensures efficient parallel processing using workers.

## Table of Contents
1. [Architecture](#architecture)
2. [Modules](#modules)
   - [Client-api](#client-api)
   - [Dispatcher](#dispatcher)
   - [Worker](#worker)
   - [Miscellaneous](#miscellaneous-rabbitmq-redis-store)

## Architecture

The `Sender-app` is composed of several interconnected modules:

1. **Client-api**: Receives and stores tasks in a PostgreSQL database.
2. **Dispatcher**: Polls unprocessed tasks from the database and pushes them to a RabbitMQ message queue.
3. **Worker**: Picks up tasks from the queue and processes them. Multiple workers can run concurrently.
4. **Miscellaneous**: Handles configurations for RabbitMQ, Redis, and database storage.

## Modules

### Client-api

The `Client-api` module is responsible for:
- Accepting new tasks through REST endpoints.
- Storing tasks in the PostgreSQL database.
- Offloading processing logic by delegating it to the Dispatcher and Workers.


### Dispatcher

The `Dispatcher` module:
- Periodically (every 5 seconds) queries the database for unprocessed task IDs.
- Sends these task IDs to a RabbitMQ queue for worker processing.
- Handles message broker connection using RabbitMQ configuration.

### Worker

The `Worker` module:
- Gets task IDs from the RabbitMQ queue.
- Processes tasks based on the given ID.
- Redis is used to prevent multiple workers from processing the same task simultaneously by locking tasks during execution.


### Miscellaneous (RabbitMQ, Redis, Store)

This section includes:
- **RabbitMQ configuration**: To establish message broker connections.
- **Redis configuration**: To manage distributed locks and ensure tasks are processed only once.
- **PostgreSQL store**: For persistent task storage.


  
