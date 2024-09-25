# Sender-app

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


  
