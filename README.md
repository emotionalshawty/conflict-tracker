# Conflict Tracker

App per seguir conflictes actius al món. Frontend a Vercel, backend a Railway, base de dades PostgreSQL.

**Frontend:** https://conflict-tracker.vercel.app

---

## Arquitectura

```
Frontend (React + Vite)  →  Backend (Spring Boot)  →  PostgreSQL
      Vercel                      Railway                 Neon/Railway
```

---

## Variables d'entorn

### Backend (Railway)

| Variable | Exemple |
|---|---|
| `DB_URL` | `jdbc:postgresql://host:5432/db` |
| `DB_DRIVER` | `org.postgresql.Driver` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `*****` |
| `DB_DIALECT` | `org.hibernate.dialect.PostgreSQLDialect` |
| `DDL_AUTO` | `update` |
| `CORS_ALLOWED_ORIGINS` | `https://conflict-tracker.vercel.app` |
| `PORT` | `8080` |

### Frontend (Vercel)

| Variable | Exemple |
|---|---|
| `VITE_API_URL` | `https://conflict-tracker.railway.app` |

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

### Configuració del projecte a Vercel

A **Settings → General** del projecte de Vercel:

| Camp | Valor |
|---|---|
| Root Directory | `frontend` |
| Build Command | `npm run build` |
| Install Command | `npm install` |
| Output Directory | `dist` |

El `vercel.json` ha d'estar dins de `frontend/` (no a l'arrel del repo), si no Vercel l'ignora.

### Coses importants

- **Eliminar de tracked `node_modules` i `dist`** — el `frontend/.gitignore` ja ho fa.
- La `baseURL` d'axios llegeix `VITE_API_URL` per funcionar tant en local com en producció: `baseURL: (import.meta.env.VITE_API_URL || '') + '/api/v1'`
- El `vercel.json` té les rewrites per a SPA (evita 404 en fer F5)

---

## Canvis per fer funcionar el desplegament

**Backend:**
- `application.yaml` — tot via variables d'entorn amb fallback a H2 per local
- `WebConfig.java` — CORS configurat amb `CORS_ALLOWED_ORIGINS` (no `*`)
- `build.gradle` — afegit `runtimeOnly 'org.postgresql:postgresql'`

**Frontend:**
- `api.js` — URL base combina `VITE_API_URL` + `/api/v1`
- `frontend/vercel.json` — rewrites a `index.html` per al routing de la SPA
- `frontend/.gitignore` — exclou `node_modules/` i `dist/`
