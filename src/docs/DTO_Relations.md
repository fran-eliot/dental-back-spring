
# 🦷 Mapa de Relaciones entre DTOs — Clínica Dental (Spring Boot)

## 📘 1️⃣ Estructura General de DTOs

Tu backend organiza los DTOs de forma limpia, jerárquica y coherente. Cada módulo (patients, professionals, treatments, etc.) tiene:

| Tipo | Ejemplo | Propósito |
|------|----------|-----------|
| DTO principal (salida) | `PatientDTO`, `AppointmentDTO` | Devuelve datos completos (usado en GET). |
| CreateRequest | `CreatePatientRequest` | Valida entrada en POST. |
| UpdateRequest | `UpdatePatientRequest` | Valida entrada en PUT/PATCH. |
| SimpleDTO | `SimplePatientDTO` | Devuelve solo datos esenciales cuando está embebido. |

---

## 🩺 2️⃣ Mapa de Relaciones entre DTOs

### 🔹 AppointmentDTO

Representa una cita dental (relación entre paciente, profesional y tratamiento).

| Campo | Tipo | Relación | Descripción |
|--------|------|-----------|--------------|
| `id` | `Long` | — | Identificador único de la cita |
| `date` | `LocalDateTime` | — | Fecha y hora de la cita |
| `duration` | `Integer` | — | Duración en minutos |
| `status` | `String` | Enum `AppointmentStatus` (`pendiente`, `confirmada`, etc.) |
| `createdBy` | `String` | Enum `CreatedBy` (`admin`, `professional`) |
| `patient` | `SimplePatientDTO` | 🔗 | Paciente asociado |
| `professional` | `SimpleProfessionalDTO` | 🔗 | Profesional asignado |
| `treatment` | `SimpleTreatmentDTO` | 🔗 | Tratamiento realizado |

```
AppointmentDTO
 ├── SimplePatientDTO
 ├── SimpleProfessionalDTO
 └── SimpleTreatmentDTO
```

---

### 🔹 AvailabilityDTO

Representa la disponibilidad horaria de un profesional.

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | Identificador |
| `professionalId` | `Long` | ID del profesional |
| `slotId` | `Long` | ID del horario (slot) |
| `date` | `LocalDate` | Fecha de la disponibilidad |
| `status` | `String` | Estado (`libre`, `reservado`, `no disponible`) |

```
AvailabilityDTO
 └── (IDs referencian Professional y Slot, sin anidación de DTO)
```

---

### 🔹 PatientDTO

Información detallada del paciente (solo visible para personal autorizado).

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | Identificador |
| `nif` | `String` | Documento de identidad |
| `firstName` | `String` | Nombre |
| `lastName` | `String` | Apellido |
| `email` | `String` | Correo electrónico |
| `phone` | `String` | Teléfono |
| `active` | `Boolean` | Si el paciente sigue activo en el sistema |

```
PatientDTO (sin sub-DTO)
```

---

### 🔹 ProfessionalDTO

Información completa del profesional (dentista, higienista, cirujano, etc.).

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | Identificador |
| `nif` | `String` | Documento de identidad |
| `licence` | `String` | Nº de colegiado o licencia |
| `name` | `String` | Nombre |
| `lastName` | `String` | Apellido |
| `email` | `String` | Correo |
| `phone` | `String` | Teléfono |
| `room` | `String` | Sala asignada |
| `active` | `Boolean` | Estado del profesional |

```
ProfessionalDTO (sin sub-DTO)
```

---

### 🔹 TreatmentDTO

Información completa de los tratamientos disponibles.

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | Identificador |
| `name` | `String` | Nombre del tratamiento |
| `type` | `String` | Tipo o categoría |
| `duration` | `Integer` | Duración por defecto (minutos) |
| `price` | `Double` | Precio base |
| `visible` | `Boolean` | Si es visible para pacientes |

```
TreatmentDTO (sin sub-DTO)
```

---

### 🔹 SimplePatientDTO

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | ID del paciente |
| `firstName` | `String` | Nombre |
| `lastName` | `String` | Apellido |

```
Usado en: AppointmentDTO.patient
```

---

### 🔹 SimpleProfessionalDTO

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | ID del profesional |
| `name` | `String` | Nombre |
| `lastName` | `String` | Apellido |

```
Usado en: AppointmentDTO.professional
```

---

### 🔹 SimpleTreatmentDTO

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | ID del tratamiento |
| `name` | `String` | Nombre del tratamiento |

```
Usado en: AppointmentDTO.treatment
```

---

### 🔹 UserDTO

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `id` | `Long` | ID del usuario |
| `email` | `String` | Correo de inicio de sesión |
| `role` | `String` | Rol (`admin`, `dentista`, `paciente`) |
| `active` | `Boolean` | Estado del usuario |

```
Usado en: AuthResponse.user
```

---

### 🔹 AuthResponse

| Campo | Tipo | Descripción |
|--------|------|-------------|
| `token` | `String` | Token JWT generado |
| `user` | `UserDTO` | Usuario autenticado |

```
AuthResponse
 └── UserDTO
```

---

## 🧭 3️⃣ Diagrama textual simplificado de relaciones

```
AuthResponse
 └── UserDTO

AppointmentDTO
 ├── SimplePatientDTO
 ├── SimpleProfessionalDTO
 └── SimpleTreatmentDTO

AvailabilityDTO
 └── (referencia a Professional y Slot IDs)

PatientDTO
ProfessionalDTO
TreatmentDTO
```

---

## ✅ 4️⃣ Recomendaciones finales

- Mantén `SimpleDTOs` solo con los campos estrictamente necesarios.  
- Usa `@Schema(description, example)` en todos los campos para una documentación Swagger clara.  
- En los `Create` y `Update` DTOs aplica validaciones (`@NotBlank`, `@NotNull`); en los de salida, no.  
- Añade este documento en tu repositorio para referencia rápida.
