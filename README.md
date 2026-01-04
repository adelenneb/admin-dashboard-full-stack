# Admin Dashboard (Angular + Spring Boot)

![Angular](https://img.shields.io/badge/Angular-18-red?logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![H2](https://img.shields.io/badge/DB-H2-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)
![Angular Material](https://img.shields.io/badge/UI-Angular%20Material-3f51b5?logo=angular&logoColor=white)

Admin/user dashboard with JWT auth, role-based access, and user CRUD.

## Frontend (Angular 18)
- Dev: `npm install && ng serve` (from `admin-dashboard/`)
- Dev URL: `http://localhost:4200`
- API config: `src/environments/environment.ts` (`apiUrl` default `http://localhost:8080/api`)
- Routes: `/login` (public), `/dashboard` (protected), `/users` (protected + ADMIN)
- UI: Angular Material (sidenav, toolbar, buttons), charts (ng2-charts), reactive forms

## Backend (Spring Boot 3, Java 21)
- Folder: `backend/`
- Run: `mvn clean spring-boot:run` (needs JDK 21)
- DB: H2 in-memory
  - JDBC: `jdbc:h2:mem:admin`
  - User: `sa` (empty password)
  - Console: `http://localhost:8080/h2-console`
- Auth: JWT (`security.jwt.secret`, `security.jwt.expiration` in `application.properties`)
  - Login: `POST /api/auth/login` `{ "username": "...", "password": "..." }`
  - Admin seed (dev): `admin / admin123`
- Endpoints:
  - `/api/dashboard` (ADMIN or USER)
  - `/api/users` CRUD (ADMIN)

## Docker (backend hidden, frontend exposed)
- Build & run: `docker compose down && docker compose up --build`
- Frontend: `http://localhost:4200`
- Backend: not exposed externally; frontend proxies `/api` to `backend:8080` via Nginx.

## Backend tests
- From `backend/`: `mvn test`
- Scenarios: login, protected dashboard access, role-based access to `/api/users`.

## Troubleshooting
- 404 `/api/...` from frontend (outside docker): check `environment.apiUrl` and backend running on 8080.
- H2: `jdbc:h2:mem:admin`, user `sa`, empty password, driver `org.h2.Driver`.
- Invalid token: change `security.jwt.secret` (>=32 chars) and restart.
- In Docker: use `http://localhost:4200`; `/api` calls are proxied to the internal backend.
