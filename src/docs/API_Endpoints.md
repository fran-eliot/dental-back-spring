# 🦷 SMYLE – API REST de Gestión de Clínica Dental
**Versión:** 1.0  
**Backend:** Java Spring Boot  
**Frontend compatible:** Angular (dental-front)  
**Autor:** Fran Ramírez Martín  
**Fecha:** Noviembre 2025  

---

## 🌐 BASE URL
http://localhost:8080

---

## 🔐 AUTENTICACIÓN (`/auth`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **POST** | `/auth/login` | Inicia sesión con email y contraseña |
| **POST** | `/auth/register-patient` | Registra un nuevo paciente |
| **GET** | `/auth/me` | Devuelve los datos del usuario autenticado |

### 🧩 Ejemplo Login
**Request**
```json
POST /auth/login
{
  "email": "admin@smyle.com",
  "password": "1234"
}
```

**Response**
```json
{
  "token": "jwt_token_generado"
}
```

---

## 👤 PACIENTES (`/patients`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **GET** | `/patients?query=` | Lista pacientes o busca por nombre/NIF |
| **GET** | `/patients/:id` | Obtiene un paciente por su ID |
| **POST** | `/patients` | Crea un nuevo paciente |
| **PUT** | `/patients/:id` | Actualiza un paciente |
| **DELETE** | `/patients/:id` | Desactiva un paciente (soft delete) |

---

## 🧑‍⚕️ PROFESIONALES (`/professionals`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **GET** | `/professionals?query=` | Lista o busca por nombre/licencia |
| **GET** | `/professionals/:id` | Obtiene un profesional |
| **POST** | `/professionals` | Crea un nuevo profesional |
| **PUT** | `/professionals/:id` | Actualiza un profesional |
| **DELETE** | `/professionals/:id` | Desactiva un profesional (soft delete) |

---

## 💊 TRATAMIENTOS (`/treatments`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **GET** | `/treatments?query=&visibleOnly=true` | Lista tratamientos o filtra por nombre/visibilidad |
| **GET** | `/treatments/:id` | Devuelve un tratamiento |
| **POST** | `/treatments` | Crea un tratamiento |
| **PUT** | `/treatments/:id` | Actualiza un tratamiento |
| **DELETE** | `/treatments/:id` | Soft delete (marca como no visible) |

---

## 📅 DISPONIBILIDADES (`/availabilities`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **GET** | `/availabilities?professionalId=&date=` | Lista por profesional y fecha |
| **GET** | `/availabilities/:id` | Devuelve una disponibilidad |
| **POST** | `/availabilities` | Crea una disponibilidad |
| **PUT** | `/availabilities/:id` | Actualiza una disponibilidad |
| **DELETE** | `/availabilities/:id` | Marca como “no disponible” (soft delete) |

---

## 🗓️ CITAS (`/appointments`)

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **POST** | `/appointments` | Crea una nueva cita |
| **GET** | `/appointments/by-patient/:id` | Lista citas de un paciente |
| **GET** | `/appointments/by-professional/:id` | Lista citas de un profesional |
| **PATCH** | `/appointments/:id/status` | Cambia el estado (`pendiente`, `confirmada`, `realizada`, `cancelada`) |
| **DELETE** | `/appointments/:id` | Cancela una cita (body con motivo) |

---

## 👥 USUARIOS (`/users`) *(solo admin)*

| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| **GET** | `/users` | Lista todos los usuarios |
| **GET** | `/users/:id` | Obtiene un usuario |
| **PATCH** | `/users/:id/role` | Cambia el rol (`admin`, `dentista`, `paciente`) |
| **PATCH** | `/users/:id/password` | Cambia la contraseña |
| **DELETE** | `/users/:id` | Desactiva un usuario (soft delete) |

---

© 2025 **Fran Ramírez Martín** — Proyecto educativo de gestión de clínica dental (Smyle)
