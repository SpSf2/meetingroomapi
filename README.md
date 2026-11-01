# Meeting Room API

API REST para la gestión de reservas de salas de reuniones, construida con **Spring Boot 4**, **Spring Data JPA** y **MySQL**. Permite crear, consultar, actualizar y eliminar reservas y categorías, asociando cada reserva a una categoría y a un conjunto de equipos. Incluye validación de datos, manejo global de errores, paginación y documentación interactiva con Swagger/OpenAPI.

---

## Tabla de contenidos

- [Características](#características)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Modelo de datos](#modelo-de-datos)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Cómo ejecutar](#cómo-ejecutar)
- [Endpoints de la API](#endpoints-de-la-api)
- [Endpoints de categorías](#endpoints-de-categorías)
- [Ejemplos de uso](#ejemplos-de-uso)
- [Paginación y ordenamiento](#paginación-y-ordenamiento)
- [Manejo de errores](#manejo-de-errores)
- [Documentación interactiva (Swagger)](#documentación-interactiva-swagger)
- [Pruebas](#pruebas)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Posibles mejoras](#posibles-mejoras)

---

## Características

- CRUD completo de reservas (`GET`, `POST`, `PUT`, `DELETE`).
- CRUD completo de categorías de reserva (`GET`, `POST`, `PUT`, `DELETE`).
- Listado de reservas paginado con ordenamiento configurable por cualquier campo.
- Asociación de reservas con una **categoría** obligatoria y varios **equipos** opcionales mediante relaciones JPA.
- Validación de existencia de la categoría y de todos los equipos enviados en la reserva.
- Protección al eliminar categorías: no se permite borrar una categoría con reservas asociadas.
- DTOs de entrada y salida para desacoplar la API del modelo de persistencia.
- Validación de datos con Jakarta Bean Validation (`@Valid`).
- Manejo centralizado de excepciones con respuestas JSON consistentes.
- Registro en logs cuando una reserva se crea o actualiza sin equipos.
- Documentación OpenAPI 3 generada automáticamente con springdoc.
- Endpoints de monitoreo vía Spring Boot Actuator.
- Pruebas unitarias de servicio y de controlador con JUnit 5 y Mockito.

---

## Stack tecnológico

| Componente    | Versión / Tecnología                 |
|---------------|--------------------------------------|
| Lenguaje      | Java 25                              |
| Framework     | Spring Boot 4.1.1                    |
| Persistencia  | Spring Data JPA / Hibernate          |
| Base de datos | MySQL (`mysql-connector-j`)          |
| Validación    | Jakarta Validation                   |
| Documentación | springdoc-openapi 2.8.5 (Swagger UI) |
| Monitoreo     | Spring Boot Actuator                 |
| Pruebas       | JUnit 5, Mockito, Spring Test        |
| Build         | Maven (con wrapper `mvnw`)           |

---

## Arquitectura del proyecto

El proyecto sigue una arquitectura en capas:

```
Controller  →  Service  →  Repository  →  Base de datos
     ↑            ↑
   DTOs      Entidades JPA
```

- **Controller**: expone los endpoints REST y valida la entrada (`ReservationController` y `ReservationCategoryController`).
- **Service**: contiene la lógica de negocio y la conversión entidad → DTO (`ReservationService` y `ReservationCategoryService`).
- **Repository**: interfaces de Spring Data JPA para el acceso a datos.
- **Entity**: modelo de persistencia (`Reservation`, `ReservationCategory`, `Equipment`).
- **DTO**: contratos de entrada (`ReservationRequest`, `CategoryRequest`) y salida (`ReservationResponse`, `ReservationCategoryResponse`, `EquipmentResponse`).
- **Exception**: excepción personalizada, excepción de solicitud incorrecta y manejador global de errores.

---

## Modelo de datos

```
ReservationCategory (1) ──── (N) Reservation (N) ──── (N) Equipment
```

- **Reservation**
  - `id` (Long, autogenerado)
  - `roomName` (String)
  - `reservedBy` (String)
  - `category` (`ManyToOne` → `ReservationCategory`, obligatoria a nivel de API)
  - `equipment` (`ManyToMany` → `Equipment`, carga `LAZY`, tabla intermedia `reservation_equipment`)
- **ReservationCategory**
  - `id`, `name`
  - `reservations` (`OneToMany` → `Reservation`)
- **Equipment**
  - `id`, `name`
  - `reservations` (`ManyToMany` mapeado por `Reservation.equipment`)

> El esquema se crea/actualiza automáticamente gracias a `spring.jpa.hibernate.ddl-auto=update`.

---

## Requisitos previos

- **JDK 25** instalado (`java -version`).
- **MySQL** en ejecución.
- No es necesario instalar Maven: el proyecto incluye el wrapper `./mvnw`.

---

## Configuración

La configuración se encuentra en `src/main/resources/application.properties`:

```properties
spring.application.name=meetingroomapi

spring.datasource.url=jdbc:mysql://localhost:3307/meetingroomdb
spring.datasource.username=root
spring.datasource.password=Temp2026
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.jpa.open-in-view=false

# Logs de depuración
logging.level.org.springdoc=DEBUG
logging.level.org.springframework.web=DEBUG
```

Antes de arrancar la aplicación:

1. Crea la base de datos (Hibernate crea las tablas, no la base):

   ```sql
   CREATE DATABASE meetingroomdb;
   ```

2. Ajusta `url`, `username` y `password` a tu entorno. Ten en cuenta que el puerto por defecto configurado es **3307** (no el 3306 habitual).

> **Recomendación:** no versionar credenciales reales. Usa variables de entorno, por ejemplo:
>
> ```properties
> spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3307/meetingroomdb}
> spring.datasource.username=${DB_USER:root}
> spring.datasource.password=${DB_PASSWORD:}
> ```

---

## Cómo ejecutar

```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# O generar el JAR y ejecutarlo
./mvnw clean package
java -jar target/meetingroomapi-0.0.1-SNAPSHOT.jar
```

La aplicación se inicia por defecto en `http://localhost:8080`.

---

## Endpoints de la API

| Método   | Ruta                 | Descripción                          | Código de éxito  |
|----------|----------------------|--------------------------------------|------------------|
| `GET`    | `/reservations`      | Lista reservas paginadas y ordenadas | `200 OK`         |
| `GET`    | `/reservations/{id}` | Obtiene una reserva por ID           | `200 OK`         |
| `POST`   | `/reservations`      | Crea una nueva reserva               | `201 Created`    |
| `PUT`    | `/reservations/{id}` | Actualiza una reserva existente      | `200 OK`         |
| `DELETE` | `/reservations/{id}` | Elimina una reserva                  | `204 No Content` |

### Cuerpo de la petición (`ReservationRequest`)

```json
{
  "roomName": "Sala Norte",
  "reservedBy": "Ana Pérez",
  "categoryId": 1,
  "equipmentIds": [10, 11]
}
```

| Campo          | Tipo         | Reglas                                                       |
|----------------|--------------|--------------------------------------------------------------|
| `roomName`     | String       | Obligatorio, entre 2 y 50 caracteres                         |
| `reservedBy`   | String       | Obligatorio, entre 2 y 50 caracteres, solo letras y espacios |
| `categoryId`   | Long         | Obligatorio; debe existir una categoría con ese ID           |
| `equipmentIds` | List\<Long\> | Opcional; si se envía, todos los IDs deben existir           |

### Respuesta (`ReservationResponse`)

```json
{
  "id": 100,
  "roomName": "Sala Norte",
  "reservedBy": "Ana Pérez",
  "category": {
    "id": 1,
    "name": "Reunión"
  },
  "equipment": [
    { "id": 10, "name": "Proyector" },
    { "id": 11, "name": "Portátil" }
  ]
}
```

> Si la categoría o alguno de los equipos indicados no existe, la API devuelve `404 Not Found`. Los equipos deben existir previamente en la base de datos (tabla `equipment`), ya que todavía no se gestionan desde la API.

---

## Endpoints de categorías

| Método   | Ruta               | Descripción                                  | Código de éxito  |
|----------|--------------------|----------------------------------------------|------------------|
| `GET`    | `/categories`      | Lista todas las categorías                   | `200 OK`         |
| `GET`    | `/categories/{id}` | Obtiene una categoría por ID                 | `200 OK`         |
| `POST`   | `/categories`      | Crea una nueva categoría                     | `201 Created`    |
| `PUT`    | `/categories/{id}` | Actualiza una categoría existente            | `200 OK`         |
| `DELETE` | `/categories/{id}` | Elimina una categoría sin reservas asociadas | `204 No Content` |

### Cuerpo de la petición (`CategoryRequest`)

```json
{
  "name": "Reuniones Ejecutivas"
}
```

| Campo  | Tipo   | Reglas                                                       |
|--------|--------|--------------------------------------------------------------|
| `name` | String | Obligatorio, entre 2 y 50 caracteres, solo letras y espacios |

### Respuesta (`ReservationCategoryResponse`)

```json
{
  "id": 1,
  "name": "Reuniones Ejecutivas"
}
```

> `DELETE /categories/{id}` devuelve `400 Bad Request` si la categoría tiene reservas asociadas.

---

## Ejemplos de uso

**Crear una reserva**

```bash
curl -X POST http://localhost:8080/reservations \
  -H "Content-Type: application/json" \
  -d '{
        "roomName": "Sala Norte",
        "reservedBy": "Ana Pérez",
        "categoryId": 1,
        "equipmentIds": [10, 11]
      }'
```

**Obtener una reserva por ID**

```bash
curl http://localhost:8080/reservations/100
```

**Listar reservas (paginado)**

```bash
curl "http://localhost:8080/reservations?page=0&size=5&sortBy=roomName&direction=asc"
```

**Actualizar una reserva**

```bash
curl -X PUT http://localhost:8080/reservations/100 \
  -H "Content-Type: application/json" \
  -d '{
        "roomName": "Sala Sur",
        "reservedBy": "Luis Gómez",
        "categoryId": 2,
        "equipmentIds": [11]
      }'
```

**Eliminar una reserva**

```bash
curl -X DELETE http://localhost:8080/reservations/100
```

**Crear una categoría**

```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Reuniones Ejecutivas"
      }'
```

**Listar categorías**

```bash
curl http://localhost:8080/categories
```

**Eliminar una categoría**

```bash
curl -X DELETE http://localhost:8080/categories/3
```

---

## Paginación y ordenamiento

El endpoint `GET /reservations` acepta los siguientes parámetros de consulta:

| Parámetro   | Por defecto | Descripción                      |
|-------------|-------------|----------------------------------|
| `page`      | `0`         | Número de página (base 0)        |
| `size`      | `2`         | Cantidad de elementos por página |
| `sortBy`    | `id`        | Campo por el que ordenar         |
| `direction` | `asc`       | `asc` o `desc`                   |

La respuesta sigue el formato `Page` de Spring Data:

```json
{
  "content": [ ... ],
  "pageable": { ... },
  "totalElements": 25,
  "totalPages": 13,
  "size": 2,
  "number": 0,
  "first": true,
  "last": false,
  "empty": false
}
```

---

## Manejo de errores

Todas las excepciones se gestionan en `GlobalExceptionHandler` y devuelven JSON con `timestamp`, `status` y `message`.

| Situación                        | Código | Respuesta                                  |
|----------------------------------|--------|--------------------------------------------|
| Validación fallida (`@Valid`)    | `400`  | `message` + mapa `errors` por campo        |
| Recurso no encontrado            | `404`  | Mensaje de la excepción                    |
| Categoría o equipo inexistente   | `404`  | Mensaje indicando el recurso no encontrado |
| Categoría con reservas asociadas | `400`  | `message` + `details` con el motivo        |
| Tipo de parámetro incorrecto     | `400`  | Detalle del error de tipo                  |
| Error de base de datos           | `500`  | Mensaje + causa específica                 |
| Excepción no controlada          | `500`  | `Error interno del servidor` + detalle     |

Ejemplo de error de validación:

```json
{
  "timestamp": "2026-10-30T12:00:00",
  "status": 400,
  "message": "Errores de validación",
  "errors": {
    "roomName": "Room name is required",
    "reservedBy": "Reserved by must contain only letters and spaces",
    "categoryId": "Category is required"
  }
}
```

Ejemplo al intentar eliminar una categoría con reservas:

```json
{
  "timestamp": "2026-10-30T12:00:00",
  "status": 400,
  "message": "Solicitud incorrecta",
  "details": "No se puede eliminar la categoría porque tiene reservas asociadas."
}
```

---

## Documentación interactiva (Swagger)

Con la aplicación en ejecución:

- **Swagger UI:** <http://localhost:8080/swagger-ui.html>
- **OpenAPI JSON:** <http://localhost:8080/v3/api-docs>

La información general de la API (título, versión, descripción y contacto) se define en `OpenApiConfig`. Los endpoints aparecen agrupados en las etiquetas **Reservations** y **Categorías de Reserva**.

---

## Pruebas

```bash
./mvnw test
```

Se incluyen:

- `ReservationServiceTest`: pruebas unitarias de la lógica de negocio con Mockito (creación, consulta, actualización, eliminación y casos de error).
- `ReservationControllerTest`: pruebas de los endpoints REST con Spring Test.
- `MeetingroomapiApplicationTests`: verifica que el contexto de la aplicación carga correctamente.

---

## Estructura del proyecto

```
meetingroomapi/
├── pom.xml
├── mvnw / mvnw.cmd
├── README.md
└── src/
    ├── main/
    │   ├── java/com/practicerest/
    │   │   ├── MeetingroomapiApplication.java
    │   │   ├── config/
    │   │   │   └── OpenApiConfig.java
    │   │   ├── controller/
    │   │   │   ├── ReservationController.java
    │   │   │   └── ReservationCategoryController.java
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── ReservationRequest.java
    │   │   │   │   └── CategoryRequest.java
    │   │   │   └── response/
    │   │   │       ├── ReservationResponse.java
    │   │   │       ├── ReservationCategoryResponse.java
    │   │   │       └── EquipmentResponse.java
    │   │   ├── entity/
    │   │   │   ├── Reservation.java
    │   │   │   ├── ReservationCategory.java
    │   │   │   └── Equipment.java
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   └── BadRequestException.java
    │   │   ├── repository/
    │   │   │   ├── ReservationRepository.java
    │   │   │   ├── ReservationCategoryRepository.java
    │   │   │   └── EquipmentRepository.java
    │   │   └── service/
    │   │       ├── ReservationService.java
    │   │       └── ReservationCategoryService.java
    │   └── resources/
    │       └── application.properties
    └── test/java/com/practicerest/
        ├── MeetingroomapiApplicationTests.java
        ├── controller/ReservationControllerTest.java
        └── service/ReservationServiceTest.java
```

---

## Posibles mejoras

- Añadir controladores y servicios para gestionar equipos.
- Añadir pruebas unitarias y de integración para `ReservationCategoryService`.
- Incorporar paginación al listado de categorías.
- Incorporar un perfil `dev`/`prod` y externalizar credenciales.
- Añadir migraciones versionadas con Flyway o Liquibase.
- Incluir un `Dockerfile` y `docker-compose` con MySQL para facilitar el arranque.
