# Gestión de Competiciones - API REST con Spring Boot

## Descripción del proyecto
Este proyecto implementa una API REST para la gestión de competiciones deportivas, incluyendo equipos y partidos.
Permite:

- Crear competiciones y equipos.
- Registrar equipos en competiciones.
- Generar y consultar partidos de la primera jornada.
- Consultar equipos no asignados a partidos.
- Documentación de la API con Swagger.

Está desarrollado en Spring Boot (Java 11+), con persistencia en H2 en memoria, y pruebas unitarias con JUnit 5 y Mockito.

---

## Estructura del proyecto

El repositorio contiene los siguientes elementos principales:

- **Carpeta `gestion-competiciones/`**: Contiene todo el código fuente del proyecto, incluyendo:
  - Controladores, servicios, repositorios, modelos y DTOs.
  - Configuración de Spring Boot y base de datos H2.
  - Test unitarios para los servicios.
  - `DataLoader` para inicializar datos de prueba.
  
- **Fichero `gestion-competiciones.postman_collection.json`**: Colección de Postman que contiene todas las peticiones para probar los endpoints de la API.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5 + Mockito
- Swagger/OpenAPI
- Postman (colección para pruebas)

---

## Ejecución del proyecto
1. Clonar repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd gestion-competiciones
```
2. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```
- La aplicación arranca en `puerto 8080`.
- Se cargan datos de prueba automáticamente con `DataLoader`.

---

## Documentación de la API
Una vez arrancada la aplicación, la documentación de la API está disponible en Swagger UI:
```bash
http://localhost:8080/swagger-ui/index.html
```

---

## Pruebas

Se incluyen pruebas unitarias para los servicios utilizando `JUnit 5 y Mockito`.
Además, se entrega una colección de `Postman` con todas las peticiones para probar los endpoints.

---

## Base de datos
- La aplicación utiliza `H2 en memoria`, por lo que los datos se pierden al cerrar la aplicación.
- Se puede acceder a la consola H2 en:
```bash
http://localhost:8080/h2-console
```
- Configuración de conexión:
  - JDBC URL: jdbc:h2:mem:competicionesdb
  - Usuario: sa
  - Contraseña: (vacía)
