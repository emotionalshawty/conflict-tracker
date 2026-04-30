# Conflict Tracker

App per seguir conflictes actius al món. Frontend a Vercel, backend a Railway, base de dades PostgreSQL a Neon.

**Frontend:** https://conflict-tracker-tau.vercel.app

---

## Arquitectura

```
Frontend (Vue + Vite)  →  Backend (Spring Boot)  →  PostgreSQL (Neon)
      Vercel                      Railway
```

---

## Variables d'entorn

### Backend (Railway)

| Variable | Valor |
|---|---|
| `DB_URL` | `jdbc:postgresql://host/db?sslmode=require` |
| `DB_DRIVER` | `org.postgresql.Driver` |
| `DB_USERNAME` | el teu usuari de Neon |
| `DB_PASSWORD` | la teva contrasenya de Neon |
| `DB_DIALECT` | `org.hibernate.dialect.PostgreSQLDialect` |
| `DDL_AUTO` | `update` |
| `CORS_ALLOWED_ORIGINS` | `https://conflict-tracker-tau.vercel.app` |
| `PORT` | `8080` |

### Frontend (Vercel)

| Variable | Valor |
|---|---|
| `VITE_API_URL` | URL pública del backend de Railway (sense `/api/v1` al final) |

---

## Executar en local

**Backend:**
```bash
./gradlew bootRun
# http://localhost:8080
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
# http://localhost:5173
```

En local deixa `VITE_API_URL` buida a `frontend/.env` — el proxy de Vite ja redirigeix `/api` cap al port 8080.

H2 console (només local): http://localhost:8080/h2-console — JDBC URL: `jdbc:h2:mem:conflictdb`, user: `sa`, password: *(buit)*

---

## Desplegament a Vercel

### Configuració del projecte

A **Settings → General** del projecte de Vercel:

| Camp | Valor |
|---|---|
| Root Directory | `frontend` |
| Build Command | `npm run build` |
| Install Command | `npm install` |
| Output Directory | `dist` |

El `vercel.json` ha d'estar dins de `frontend/` (no a l'arrel del repo), si no Vercel l'ignora.

---

## Errors que van passar (i com es van solucionar)

### `cd: frontend: No such file or directory` a Vercel
El `vercel.json` estava a l'arrel del repo però Vercel tenia el Root Directory a `frontend/`, així que l'ignorava i usava comandes velles en caché. Solució: moure `vercel.json` dins de `frontend/`.

### `Permission denied` al binari de vite
`node_modules` estava commitat a git i els permisos dels binaris no sobreviuen a git. Solució: crear `frontend/.gitignore` amb `node_modules/` i `dist/`, i fer `git rm -r --cached frontend/node_modules frontend/dist`.

### `Driver org.h2.Driver claims to not accept jdbcUrl postgresql://`
Faltaven `DB_DRIVER` i `DB_DIALECT` a Railway. Solució: afegir `org.postgresql.Driver` i `org.hibernate.dialect.PostgreSQLDialect`.

### `data.sql` peta amb duplicate key en cada restart
Spring Boot executava `data.sql` a cada arrencada perquè `sql.init.mode` estava a `always`. Les IDs ja existien a Neon → error. Solució: canviar a `${SPRING_SQL_INIT_MODE:never}` a `application.yaml` perquè per defecte mai executi el script.

### `DDL_AUTO=create` esborrava totes les taules a cada restart
Hibernate amb `create` esborra i recrea les taules cada vegada que arrenca. Solució: posar `DDL_AUTO=update` a Railway perquè només faci ALTER si cal, sense esborrar res.

### La URL del backend a Vercel era incorrecta
`VITE_API_URL` estava posat a `https://conflict-tracker.railway.app` que no existia. La URL real de Railway té el format `https://conflict-tracker-production-xxxx.up.railway.app`. Solució: copiar la URL exacta des de Railway → Settings → Networking.

### `VITE_API_URL` amb el path `/api/v1` inclòs
L'axios ja afegeix `/api/v1` a la URL base. Si `VITE_API_URL` ja incloïa `/api/v1`, les peticions anaven a `/api/v1/api/v1/conflicts` → 404. Solució: posar només la URL base sense cap path al final.

### 403 Forbidden per CORS
`CORS_ALLOWED_ORIGINS` a Railway no coincidia amb el domini real de Vercel. El domini de producció de Vercel és el que apareix a **Settings → Domains** del projecte (p.ex. `conflict-tracker-tau.vercel.app`), no necessàriament `conflict-tracker.vercel.app`. Solució: actualitzar `CORS_ALLOWED_ORIGINS` amb el domini exacte. El `WebConfig.java` accepta múltiples orígens separats per comes si cal.

---

## Canvis al codi per fer funcionar el desplegament

**Backend:**
- `application.yaml` — tot configurat via variables d'entorn amb fallback a H2 per local
- `WebConfig.java` — CORS configurat amb `CORS_ALLOWED_ORIGINS` separats per comes
- `build.gradle` — afegit `runtimeOnly 'org.postgresql:postgresql'`

**Frontend:**
- `api.js` — `baseURL` combina `VITE_API_URL` + `/api/v1`
- `frontend/vercel.json` — rewrites a `index.html` per al routing de la SPA
- `frontend/.gitignore` — exclou `node_modules/` i `dist/`
