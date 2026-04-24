# Conflict Tracker

Aplicació de seguiment de conflictes actius al món.

## Enllaç públic

> **Frontend:** https://conflict-tracker.vercel.app *(actualitzar un cop desplegat)*

---

## Arquitectura

```
┌─────────────────────┐     HTTPS      ┌──────────────────────┐     JDBC      ┌─────────────────────┐
│   Frontend (Vue 3)  │ ─────────────► │  Backend (Spring Boot)│ ────────────► │  PostgreSQL (Neon /  │
│   Vercel            │                │  Railway / Render     │               │  Supabase / Railway) │
└─────────────────────┘                └──────────────────────┘               └─────────────────────┘
   VITE_API_URL=                          PORT, DB_URL…                           Persistent data
   https://backend.railway.app
```

---

## Variables d'entorn

### Backend (Railway / Render / Fly.io)

| Variable             | Descripció                                              | Exemple                                             |
|----------------------|---------------------------------------------------------|-----------------------------------------------------|
| `DB_URL`             | JDBC URL de PostgreSQL                                  | `jdbc:postgresql://host:5432/db`                   |
| `DB_DRIVER`          | Driver JDBC                                             | `org.postgresql.Driver`                            |
| `DB_USERNAME`        | Usuari de la BD                                         | `postgres`                                          |
| `DB_PASSWORD`        | Contrasenya de la BD                                    | `*****`                                             |
| `DB_DIALECT`         | Dialect Hibernate                                       | `org.hibernate.dialect.PostgreSQLDialect`          |
| `DDL_AUTO`           | Estratègia DDL (`create` o `update`)                    | `update`                                            |
| `CORS_ALLOWED_ORIGINS` | URL del frontend (sense `/` final)                  | `https://conflict-tracker.vercel.app`              |
| `PORT`               | Port del servidor (Railway ho injecta automàticament)   | `8080`                                              |

### Frontend (Vercel)

| Variable        | Descripció                          | Exemple                                |
|-----------------|-------------------------------------|----------------------------------------|
| `VITE_API_URL`  | URL base del backend (sense `/`)    | `https://conflict-tracker.railway.app` |

---

## Execució local

### Backend
```bash
./gradlew bootRun
# Arrenca amb H2 en memòria a http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# Arrenca a http://localhost:5173
```

Per al dev local, `VITE_API_URL` es deixa buida a `frontend/.env` perquè el proxy de Vite redirigeix `/api` → `localhost:8080`.

---

## Modificacions per al desplegament

### Backend

#### 1. `application.yaml` — Variables d'entorn en lloc de valors fixos

**Error inicial:** El backend tenia tots els valors de connexió (H2, port, etc.) codificats directament. En desplegar a Railway/Render, la base de dades és PostgreSQL i les credencials les proporciona el servei via variables d'entorn.

**Solució:** Totes les propietats de configuració ara utilitzen `${VAR:default}` per llegir variables d'entorn amb fallback a H2 per a dev local:
```yaml
datasource:
  url: ${DB_URL:jdbc:h2:mem:conflictdb;...}
  driver-class-name: ${DB_DRIVER:org.h2.Driver}
  username: ${DB_USERNAME:sa}
  password: ${DB_PASSWORD:}
```

#### 2. `WebConfig.java` — Configuració CORS per a producció

**Error inicial:** Sense configuració CORS, el navegador bloquejava totes les peticions des del frontend de Vercel al backend amb:
```
Access to XMLHttpRequest at 'https://backend.railway.app/api/v1/conflicts'
from origin 'https://conflict-tracker.vercel.app' has been blocked by CORS policy
```

**Solució:** Creat `src/main/java/.../config/WebConfig.java` que llegeix els orígens permesos des d'una variable d'entorn (`CORS_ALLOWED_ORIGINS`), evitant el `*` genèric en producció:
```java
registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigins.split(","))
        ...
```

#### 3. `build.gradle` — Driver PostgreSQL

**Error inicial:** El backend arrencava però fallava en connectar a PostgreSQL amb `No suitable driver found for jdbc:postgresql://...`.

**Solució:** Afegida la dependència del driver:
```groovy
runtimeOnly 'org.postgresql:postgresql'
```

### Frontend

#### 4. `api.js` — URL base configurable

**Error inicial:** La `baseURL` d'axios era `/api/v1` (relativa), cosa que funciona en dev amb el proxy de Vite però no en producció, on el frontend i backend estan en dominis diferents.

**Solució:** La URL base ara combina la variable d'entorn `VITE_API_URL` amb el path:
```js
baseURL: (import.meta.env.VITE_API_URL || '') + '/api/v1'
```
En local, `VITE_API_URL` és buida i la ruta relativa continua funcionant via proxy. En producció, apunta al backend desplegat.

#### 5. `vercel.json` — Routing SPA

**Error inicial:** En fer F5 o accedir directament a una ruta com `/conflicts/1`, Vercel retornava un error **404** perquè intentava trobar un fitxer estàtic en aquella ruta.

**Solució:** Creat `frontend/vercel.json` per redirigir totes les rutes a `index.html`:
```json
{
  "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
}
```

---

## Consola H2 (només local)

URL: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:conflictdb`  
User: `sa` / Password: *(buit)*
