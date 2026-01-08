# Medlink Backend

Spring Boot REST API for managing people records (patients/persons) with PostgreSQL and Liquibase
for schema management.

## Requirements

- Without Docker:
    - JDK 17+
    - Gradle
    - PostgreSQL v16+
- With Docker: Docker Engine and Docker Compose plugin.

## Running without Docker

1) Start PostgreSQL manually (or `docker-compose up postgres`):
    - DB: `medlink`
    - User/Password: `medlinkuser` / `medlinkpass`
    - Host: `localhost`
2) Update `src/main/resources/application.properties` if your DB host/credentials differ.
3) Run the app:

```bash
./gradlew bootRun
```

App will be available at http://localhost:8080/api.

## Running with Docker/Compose

Docker Compose provisions PostgreSQL and the medlink-be container.

```bash
docker compose up --build
```

- App: http://localhost:8080/api
- DB: `postgres:5432`, database `medlink`, user `medlinkuser`, password `medlinkpass`

Stop and remove containers.

```bash
docker compose down
```

## Key Endpoints

- `GET /api/v1/persons` – list with filters/paging
- `GET /api/v1/persons/{id}` – get by id
- `POST /api/v1/persons` – create
- `PUT /api/v1/persons/{id}` – update
- `DELETE /api/v1/persons/{id}` – soft delete

## Testing

```bash
./gradlew test
```

## Notes

- Liquibase changelog: `src/main/resources/db/changelog/master.xml`
- Soft deletes use `deleted_at`; queries exclude deleted rows via entity restriction.
