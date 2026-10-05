#!/usr/bin/env bash
# Deployer por polling: cuando main avanza y el CI de ese commit pasó, hace backup de la base y redeploya prod.
# Corre en un contenedor (ver deploy/compose.yaml) con acceso al Docker del host.
set -uo pipefail

: "${REPO_DIR:?}" "${BACKUP_DIR:?}" "${GITHUB_REPO:?}"
BRANCH=${BRANCH:-main}
CI_CHECK_NAME=${CI_CHECK_NAME:-build}
POLL_INTERVAL_SECONDS=${POLL_INTERVAL_SECONDS:-300}
BACKUPS_TO_KEEP=${BACKUPS_TO_KEEP:-14}

last_seen=""     # último commit nuevo logueado, para no repetir el mismo mensaje en cada poll
last_rejected="" # commit cuyo CI o deploy falló; no se reintenta hasta que llegue otro

log() { echo "$(date -u +%FT%TZ) $*"; }

compose() { docker compose --project-directory "$REPO_DIR" -f "$REPO_DIR/compose.yaml" "$@"; }

# Imprime success, pending o failure según el check de CI del commit.
ci_status() {
    local auth=()
    [ -n "${GITHUB_TOKEN:-}" ] && auth=(-H "Authorization: Bearer $GITHUB_TOKEN")
    curl -fsS "${auth[@]}" -H "Accept: application/vnd.github+json" \
        "https://api.github.com/repos/$GITHUB_REPO/commits/$1/check-runs?check_name=$CI_CHECK_NAME" |
        jq -r '
            if .total_count == 0 or any(.check_runs[]; .status != "completed") then "pending"
            elif all(.check_runs[]; .conclusion == "success") then "success"
            else "failure" end'
}

backup_db() {
    if [ -z "$(compose ps -q --status running db)" ]; then
        log "la db no está corriendo, no hay nada que backupear"
        return 0
    fi
    local file
    file="$BACKUP_DIR/dndapp-$(date -u +%Y%m%dT%H%M%SZ)-${1:0:7}.dump"
    if ! compose exec -T db pg_dump -U app -d app -Fc >"$file.tmp"; then
        rm -f "$file.tmp"
        return 1
    fi
    mv "$file.tmp" "$file"
    log "backup: $file"
    ls -1t "$BACKUP_DIR"/dndapp-*.dump | tail -n +$((BACKUPS_TO_KEEP + 1)) | xargs -r rm --
}

up() { compose up -d --build --wait --wait-timeout 180; }

deploy_once() {
    if ! git -C "$REPO_DIR" fetch --quiet origin "$BRANCH"; then
        log "ERROR: falló git fetch"
        return
    fi
    local current target status
    current=$(git -C "$REPO_DIR" rev-parse HEAD)
    target=$(git -C "$REPO_DIR" rev-parse "origin/$BRANCH")
    [ "$current" = "$target" ] || [ "$target" = "$last_rejected" ] && return

    if ! status=$(ci_status "$target"); then
        log "ERROR: no se pudo consultar el CI de ${target:0:7}"
        return
    fi
    case $status in
    pending)
        [ "$target" != "$last_seen" ] && log "nuevo commit ${target:0:7}, esperando que termine el CI"
        last_seen=$target
        return
        ;;
    failure)
        log "el CI de ${target:0:7} falló, no se despliega"
        last_rejected=$target
        return
        ;;
    esac

    log "desplegando ${current:0:7} -> ${target:0:7}"
    if ! backup_db "$target"; then
        log "ERROR: falló el backup, se aborta el deploy (se reintenta en el próximo poll)"
        return
    fi
    git -C "$REPO_DIR" reset --hard --quiet "$target"
    if up; then
        log "deploy OK: ${target:0:7}"
        return
    fi

    # Vuelve al código anterior. Las migraciones que se hayan aplicado NO se revierten: para eso está el backup.
    log "ERROR: el deploy de ${target:0:7} falló, volviendo a ${current:0:7}"
    last_rejected=$target
    git -C "$REPO_DIR" reset --hard --quiet "$current"
    if up; then log "rollback OK: ${current:0:7}"; else log "ERROR: también falló el rollback"; fi
}

trap 'log "deployer detenido"; exit 0' TERM INT
log "deployer iniciado: $GITHUB_REPO@$BRANCH cada ${POLL_INTERVAL_SECONDS}s en $REPO_DIR"
while true; do
    deploy_once
    sleep "$POLL_INTERVAL_SECONDS" &
    wait $!
done
