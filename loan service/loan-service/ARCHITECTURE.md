# LOAN-SERVICE - ARQUITECTURA LIMPIA

## 🎯 **RESPONSABILIDAD ÚNICA**
Este microservicio se enfoca EXCLUSIVAMENTE en:
- ✅ Gestión de préstamos (CRUD)
- ✅ Gestión de usuarios (necesarios para préstamos)
- ✅ Validaciones de negocio
- ✅ API de datos para reporting-service

## ❌ **LO QUE NO HACE (POR DISEÑO)**
- ❌ Analytics/Reportes (responsabilidad de reporting-service)
- ❌ Gestión de aulas (responsabilidad de classroom-service)

## 📊 **ENDPOINTS DISPONIBLES (28 TOTAL)**

### 🔵 **LoanController** - 15 endpoints CRUD
```
POST   /api/loans                      - Crear préstamo
GET    /api/loans                      - Listar todos los préstamos
GET    /api/loans/{id}                 - Obtener préstamo por ID
PUT    /api/loans/{id}                 - Actualizar préstamo
DELETE /api/loans/{id}                 - Eliminar préstamo
PATCH  /api/loans/{id}/status          - Cambiar estado genérico
PATCH  /api/loans/{id}/activate        - Activar préstamo
PATCH  /api/loans/{id}/cancel          - Cancelar préstamo
GET    /api/loans/user/{userCode}      - Préstamos por usuario
GET    /api/loans/classroom/{code}     - Préstamos por aula
GET    /api/loans/status/{status}      - Préstamos por estado
GET    /api/loans/date-range           - Préstamos en rango de fechas
GET    /api/loans/active               - Préstamos activos
GET    /api/loans/reserved             - Préstamos reservados
GET    /api/loans/cancelled            - Préstamos cancelados
```

### � **UserController** - 4 endpoints usuarios
```
POST   /api/users                      - Crear usuario
GET    /api/users                      - Listar todos los usuarios
GET    /api/users/{code}               - Obtener usuario por código
PUT    /api/users/{code}               - Actualizar usuario
```

### 📊 **LoanDataController** - 8 endpoints de datos crudos
```
GET    /api/loans/data/raw           - Todos los préstamos (datos crudos)
GET    /api/loans/data/by-date-range - Préstamos por rango de fechas (sin análisis)
GET    /api/loans/data/by-status     - Préstamos por estado (sin análisis)
GET    /api/loans/data/by-user       - Préstamos por usuario (sin análisis)
GET    /api/loans/data/by-classroom  - Préstamos por aula (sin análisis)
GET    /api/loans/data/active        - Préstamos activos (sin conteos)
GET    /api/loans/data/reserved      - Préstamos reservados (sin conteos)
GET    /api/loans/data/cancelled     - Préstamos cancelados (sin conteos)
```

## 🏗️ **ARQUITECTURA DE MICROSERVICIOS**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│  CLASSROOM      │    │   LOAN-SERVICE   │    │   REPORTING     │
│   SERVICE       │    │   (ESTE SERV.)   │    │   SERVICE       │
│                 │    │                  │    │                 │
│ - Gestión aulas │◄──►│ - CRUD préstamos │───►│ - Analytics     │
│ - Horarios      │    │ - Usuarios       │    │ - Reportes DTIC │
│ - Capacidad     │    │ - Validaciones   │    │ - Dashboard     │
│                 │    │ - API datos      │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

## 🔄 **COMUNICACIÓN ENTRE SERVICIOS**

### **loan-service → classroom-service**
- Validar disponibilidad de aulas
- Obtener información de aulas

### **reporting-service → loan-service**
- Consumir `/api/loans/data/*` endpoints
- Obtener datos crudos para procesamiento externo

## ⚙️ **CONFIGURACIÓN**

### **Base de datos**
- MySQL 8.0
- Puerto: 8082
- Database: loan_service_db

### **Properties**
```properties
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/loan_service_db
spring.jpa.hibernate.ddl-auto=update
```

## 📋 **ESTADO ACTUAL**
- ✅ Arquitectura limpia implementada
- ✅ Separación de responsabilidades (DIRECTRIZ DEL DIRECTOR)
- ✅ 27 endpoints funcionales (solo datos crudos)
- ✅ Base de datos configurada
- ✅ Sin dockerización (acuerdo del equipo)
- ✅ Cumple directriz: SOLO DATOS, NO ANÁLISIS
- 🔄 Rama: `loan-service-clean-architecture`

---
**Fecha**: Noviembre 6, 2025  
**Versión**: Clean Architecture v1.0  
**Autor**: Sistema de Reservas UPTC