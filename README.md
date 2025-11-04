# Eron Challenge Backend 2025

## Description

Eron Back-End Developer Challenge REST API

## Instalation

### Maven using mvnw

    mvnw clean install
    bash mvnw clean install # In windows with MINGW64

### Docker with docker-compose

    docker-compose up --build

## Execution

### Root project

    mvnw spring-boot:run
    bash mvnw spring-boot:run # In windows with MINGW64

### Maven installed in system

    mvn spring-boot:run

### Access to Rest API (Browser or Postman)

    localhost:8080

## OpenApi Docs

    http://localhost:8080/swagger-ui.html

## Endpoints

### Return directors with movie count strictly greater than threshold

    GET <API_URL>/api/directors?threshold=x

### Clear cache

    DELETE <API_URL/api/cache/clear

## Tests

There are unit tests with mocks for DirectorsService

Execution:

    mvnw test

## Features

- Springboot
- Cache for external api data with clear cache endpoint
- Unit tests
- Error handling
- Docker compose for executing in other systems without Java JRE installed
- Swagger and OpenAPI

## Architecture

Layered Architecture with dto request and response models, controller, service, client for external api data (Wiremock), error handling and config for API and Cache.
