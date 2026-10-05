.DEFAULT_GOAL := help

DEV := docker compose -f compose.yaml -f compose.dev.yaml
PROD_DIR := $(HOME)/Proyectos/DyDApp/prod
PROD := docker compose --project-directory $(PROD_DIR) -f $(PROD_DIR)/compose.yaml
BACKUP_DIR := $(HOME)/Proyectos/DyDApp/backups
GRADLE := ./gradlew

.PHONY: help
help: ## Muestra esta ayuda
	@awk 'BEGIN {FS = ":.*## "} /^##@/ {printf "\n\033[1m%s\033[0m\n", substr($$0, 5)} /^[a-zA-Z_-]+:.*## / {printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

##@ Desarrollo

.PHONY: dev
dev: ## Levanta dev completo en Docker (db + app en localhost:8081, Swagger en /swagger-ui.html)
	$(DEV) up -d --build --wait

.PHONY: dev-db
dev-db: ## Levanta solo la base de dev (localhost:5435)
	$(DEV) up -d --wait db

.PHONY: run
run: dev-db ## Corre la app con Gradle contra la base de dev (localhost:8082, más rápido que rebuildear la imagen)
	DB_URL=jdbc:postgresql://localhost:5435/app \
	db_password="$$(cat secrets/dev_db_password.txt)" \
	SPRING_PROFILES_ACTIVE=dev SERVER_PORT=8082 \
	$(GRADLE) bootRun

.PHONY: dev-down
dev-down: ## Baja dev (los datos se mantienen)
	$(DEV) down

.PHONY: dev-reset
dev-reset: ## Baja dev y BORRA su base
	$(DEV) down -v

.PHONY: dev-logs
dev-logs: ## Sigue los logs de dev
	$(DEV) logs -f

.PHONY: dev-ps
dev-ps: ## Estado de los contenedores de dev
	$(DEV) ps

.PHONY: psql
psql: ## Consola psql en la base de dev
	$(DEV) exec db psql -U app -d app

.PHONY: openapi
openapi: ## Guarda el spec OpenAPI de dev en build/openapi.json (dev tiene que estar corriendo)
	@mkdir -p build
	curl -fsS http://localhost:8081/v3/api-docs -o build/openapi.json
	@echo "Spec en build/openapi.json"

##@ Calidad

.PHONY: build
build: ## Build completo: compila, tests, lint y coverage mínimo (lo mismo que el CI)
	$(GRADLE) build

.PHONY: test
test: ## Corre los tests (T=Patron para filtrar, ej: make test T='*CharacterTest*')
	$(GRADLE) test $(if $(T),--tests '$(T)')

.PHONY: lint
lint: ## ktlint + detekt
	$(GRADLE) ktlintCheck detekt

.PHONY: format
format: ## Autoformatea con ktlint
	$(GRADLE) ktlintFormat

.PHONY: coverage
coverage: ## Reporte de coverage HTML y verificación del mínimo
	$(GRADLE) koverHtmlReport koverVerify
	@echo "Reporte en build/reports/kover/html/index.html"

.PHONY: clean
clean: ## Borra los artefactos de build
	$(GRADLE) clean

##@ Prod (solo lectura; el deploy es automático al mergear a main)

.PHONY: prod-status
prod-status: ## Estado de prod, commit desplegado y health
	@$(PROD) ps
	@echo "Commit desplegado: $$(git -C $(PROD_DIR) log --oneline -1)"
	@curl -fsS http://localhost:8080/actuator/health && echo

.PHONY: prod-logs
prod-logs: ## Sigue los logs de la app de prod
	$(PROD) logs -f app

.PHONY: deployer-logs
deployer-logs: ## Sigue los logs del deployer
	docker logs -f dydapp-deployer-deployer-1

.PHONY: backups
backups: ## Lista los backups de la base de prod
	@ls -lht $(BACKUP_DIR)
