# DnDApp – Server

Backend de una app de character sheets de D&D. El cliente es una app Android (Jetpack Compose) que vive en **otro repo, en Windows**; este server corre en WSL. El contrato entre ambos es la API REST (ver "Contrato con la app").

## Stack

- Kotlin 2.3 + Spring Boot 4.1 (Web MVC, Data JPA, Validation, Actuator), Java 21
- PostgreSQL 16, migraciones con Flyway (`src/main/resources/db/migration`)
- Gradle (Kotlin DSL), Docker Compose para levantar db + app

## Comandos

Gradle requiere JDK 21 local (WSL Fedora: `sudo dnf install java-21-openjdk-devel`) y Docker (Testcontainers levanta Postgres para los tests). Para solo correr el server alcanza con Docker.

Todo pasa por el `Makefile` (`make` sin argumentos lista los targets):

```bash
make build          # lo mismo que el CI: compila, tests, ktlint, detekt y coverage mínimo (80%)
make test           # tests; filtrar con: make test T='*CharacterTest*'
make lint           # ktlint + detekt
make format         # autoformatea con ktlint
make coverage       # reporte en build/reports/kover/html
make dev            # dev en Docker: db + app en localhost:8081 (Swagger en /swagger-ui.html)
make run            # app con Gradle contra la db de dev, en localhost:8082 (iteración rápida)
make psql           # consola en la db de dev
make openapi        # guarda el spec de dev en build/openapi.json (para la app Android)
make prod-status    # estado de prod y commit desplegado (prod-logs, deployer-logs, backups)
```

Prod y dev son proyectos de compose distintos (`dydapp` / `dydapp-dev`), con volúmenes de base separados. Los nombres de infraestructura (proyectos de compose, volúmenes, deployer, carpetas) conservan `dydapp` a propósito: renombrar el proyecto de prod crearía un volumen nuevo y vacío.

Con el perfil `dev`, el spec OpenAPI está en `/v3/api-docs` y Swagger UI en `/swagger-ui.html` (apagados fuera de dev).

## Arquitectura: hexagonal (ports & adapters)

La "MVC" de Spring vive solo en el adapter de entrada web: el controller es un adapter, el "view" son los DTOs JSON. La lógica de D&D va en el dominio.

```
com.valsagnapp.dndapp
├── domain/                  # Kotlin puro: modelos, reglas de D&D, value objects. SIN Spring, SIN JPA.
├── application/
│   ├── port/inbound/        # casos de uso (interfaces): CreateCharacterUseCase, ...
│   ├── port/outbound/       # lo que la app necesita del exterior: CharacterRepository, ...
│   └── service/             # implementaciones de los casos de uso (sin anotaciones de Spring)
├── adapter/
│   ├── inbound/web/         # @RestController, request/response DTOs, mappers DTO <-> dominio
│   └── outbound/persistence/ # @Entity JPA, Spring Data repos, adapters que implementan port/outbound
└── config/                  # wiring de beans (los services se registran acá con @Bean), seguridad, etc.
```

`Character` (crear y obtener) es el slice de referencia: copiar su estructura para features nuevas.

Reglas:
- Las dependencias apuntan hacia adentro: `adapter -> application -> domain`. El dominio no importa nada de `org.springframework` ni `jakarta.persistence`.
- Las entidades JPA **no** son el modelo de dominio. Siempre mapear en el adapter de persistencia.
- Los DTOs web no salen del paquete `adapter/inbound/web`.
- Los controllers no tienen lógica: validan input, llaman a un puerto de entrada, mapean la respuesta.
- Errores: excepciones de dominio -> `ProblemDetail` en `ApiExceptionHandler`. Los `require()` del dominio salen como 400.
- Estas reglas están verificadas con Konsist en `architecture/ArchitectureTest.kt`. Si un test de arquitectura falla, se arregla el código, no el test.
- Reglas derivadas (modificadores de habilidad, bonus de competencia, CA, etc.) se calculan en el dominio, no se persisten salvo que haya una razón explícita.

## Testing

Objetivo: buen coverage, con la pirámide bien armada.
- **Dominio**: unit tests puros (sin Spring), rápidos y exhaustivos. Acá van la mayoría de los tests.
- **Servicios de aplicación**: unit tests con los puertos de salida falseados (fakes en memoria como `InMemoryCharacterRepository`, preferidos a mocks).
- **Web**: `@WebMvcTest` por controller (status codes, validación, forma del JSON).
- **Persistencia**: `@DataJpaTest` contra Postgres real vía `TestcontainersConfiguration`, no H2, para que las migraciones de Flyway se prueben de verdad.
- Builders de test con defaults en `domain/Fixtures.kt` (`character(...)`, `abilityScores(...)`).
- Coverage con Kover, mínimo 80% de líneas (falla el build). No bajar el umbral para que pase.
- Todo cambio de comportamiento viene con su test. Nombres de test descriptivos en backticks: ``fun `level up increases proficiency bonus at level 5`()``.

## Base de datos

- Nunca modificar una migración ya commiteada; siempre crear una nueva `V<n>__descripcion.sql`.
- `ddl-auto: validate`: el esquema lo manda Flyway, Hibernate solo valida.

## Contrato con la app

- Endpoints versionados bajo `/api/v1/...`.
- Todo endpoint nuevo bajo `/api` lleva su request en la colección de Postman (`postman/collections/DnDApp/<Nombre>.request.yaml`, con `url: "{{baseUrl}}/api/..."`, un script con `pm.test` y `order` para ubicarlo). `PostmanCollectionTest` falla si la colección y los endpoints no coinciden. Environments en `postman/environments` (dev 8081, run 8082, prod 8080).
- Cambios que rompan el contrato (renombrar/quitar campos, cambiar tipos) requieren nueva versión o coordinación explícita con la app Android.

## Despliegue y seguridad

- "Prod" es la PC del usuario (Windows + Docker Desktop + WSL Fedora), usada por una sola persona desde su celular vía Tailscale.
- La app publica solo en `127.0.0.1:8080`; se expone al tailnet con `tailscale serve --bg 8080` (HTTPS con certificado válido, solo dispositivos del tailnet).
- **No hay autenticación a nivel app a propósito**: el límite de seguridad es Tailscale. Nunca publicar puertos en `0.0.0.0` ni usar `tailscale funnel` (eso lo abriría a internet). Si eso cambia, hay que agregar auth antes.

### Deploy automático (`deploy/`)

- Prod corre desde un checkout **separado** en `~/Proyectos/DyDApp/prod` (nunca editarlo a mano). Este repo (`server/`) es solo para desarrollo.
- El contenedor `dydapp-deployer` revisa `main` cada 5 minutos. Si hay un commit nuevo y su check `build` del CI pasó, hace backup de la base, `git reset --hard` al commit y `docker compose up -d --build --wait`. Si el deploy falla, vuelve al commit anterior (las migraciones aplicadas **no** se revierten: para eso está el backup).
- Polling en vez de runner self-hosted porque el repo es público: GitHub nunca ejecuta código en la PC.
- Logs: `docker logs -f dydapp-deployer-deployer-1`.
- Cambios en `deploy/` no se autoaplican: después de mergearlos, `docker compose -f deploy/compose.yaml up -d --build` desde el checkout de prod.
- Backups: `pg_dump -Fc` antes de cada deploy en `~/Proyectos/DyDApp/backups` (se guardan los últimos 14). Están en el mismo disco: protegen de una migración mala, no de que se muera el disco. Para restaurar (desde el checkout de prod):
  ```bash
  docker compose stop app
  docker compose exec -T db pg_restore -U app -d app --clean --if-exists < ~/Proyectos/DyDApp/backups/<archivo>.dump
  docker compose start app
  ```

### Flujo de trabajo

- `main` está protegida: todo cambio entra por PR con el CI en verde. Mergear a `main` = deployar a prod.
- Trabajar en ramas (`feature/...`, `fix/...`) y nunca pushear directo a `main`.
- Cambios de esquema: pensar que el deploy aplica la migración sola. Preferir migraciones aditivas (agregar columnas/tablas) antes que destructivas.

## Secretos

- Las contraseñas van en `secrets/` (gitignored): `prod_db_password.txt` y `dev_db_password.txt`, y se montan como Docker secrets en `/run/secrets/`. Spring las lee con `configtree`.
- Nunca commitear `secrets/`, `.env`, ni hardcodear credenciales en `application.yaml`.

## Convenciones

- Código e identificadores en inglés; commits y docs pueden ir en español.
- Estilo: ktlint (`intellij_idea`, ver `.editorconfig`) + detekt (`config/detekt/detekt.yml`). No suprimir warnings sin motivo.
- Commits chicos y enfocados.
- CI: `.github/workflows/ci.yml` corre `./gradlew build` en cada push/PR.
