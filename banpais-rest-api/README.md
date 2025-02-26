# 📦 Proyecto Spring Boot: API Híbrida (REST + SOAP) para Transacciones Bancarias 

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3-orange)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT) <!-- O la licencia que aplique -->
<!-- Añade más badges según sea necesario (ej. build status, code coverage) -->

## 📖 Descripción

**REST API** es un microservicio desarrollado con **Spring Boot** que proporciona una interfaz **híbrida REST y SOAP** para gestionar clientes, cuentas y movimientos bancarios.

*   **Interfaz SOAP:** Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre clientes y cuentas, y registrar movimientos. Utiliza un formato de *trama* de texto plano dentro de los mensajes SOAP, en lugar del XML estructurado tradicional.
*   **Interfaz REST:** Expone los mismos recursos (clientes, cuentas, movimientos) a través de una API RESTful, utilizando JSON para las solicitudes y respuestas.

Esta combinación ofrece flexibilidad para diferentes tipos de clientes y casos de uso.

**Características Principales:**

*   **CRUD de Clientes:** Registrar, actualizar, eliminar y consultar clientes (REST y SOAP).
*   **CRUD de Cuentas:** Registrar, actualizar, eliminar y consultar cuentas (REST y SOAP).
*   **Registro de Movimientos:** Registrar movimientos transaccionales (ej. transferencias) (REST y SOAP).
*   **Formato de Trama (SOAP):** Formato de trama de texto plano, simplificando la integración. *Los detalles específicos del formato, como las longitudes de los campos, son parametrizables y se definen en la configuración del servicio.*
*   **API RESTful (REST):** Interfaz REST con endpoints para todas las operaciones, usando JSON.
*   **Spring Boot 3.4.3:** Última versión estable de Spring Boot.
*   **SOAP con Apache CXF:** Implementación de servicios web SOAP.
*   **JAXB (Jakarta XML Binding):** Manejo de XML (SOAP).
*   **REST con Spring MVC y Jersey:** Implementación de la API REST.
*   **Spring Data JPA:** Persistencia de datos.
*   **MySQL Connector/J:** Controlador JDBC para MySQL.
*   **Cache con Caffeine.**
* **Spring Security:** Protección de endpoints.
*   **Documentación con OpenAPI (Swagger):** Documentación interactiva de la API REST (`/swagger-ui.html`).


## 📖 Documentación API

* **SOAP API Swagger UI:** http://localhost:8081/swagger-ui.html

### 🌐 Endpoints

#### Endpoints REST

##### Clientes

*   `GET /api/clientes`: Todos los clientes.
*   `GET /api/clientes/{identificacion}`: Cliente por identificación.
*   `POST /api/clientes/registrar`: Registrar cliente.
*   `PUT /api/clientes/actualizar`: Actualizar cliente (requiere `id`).
*   `DELETE /api/clientes/{id}`: Eliminar cliente.

##### Cuentas

*   `GET /api/cuentas`: Todas las cuentas.
*   `GET /api/cuentas/{numeroCuenta}`: Cuenta por número.
*  `GET /api/cuentas/cliente/{clienteId}`: Obtiene las cuentas de un cliente por su ID.
*   `POST /api/cuentas/registrar`: Registrar cuenta.
* `DELETE /api/cuentas/{numeroCuenta}`: Eliminar cuenta

##### Movimientos

*   `GET /api/movimientos`: Todos los movimientos.
*   `GET /api/movimientos/{numeroCuenta}`: Movimientos de una cuenta.
*   `POST /api/movimientos/registrar`: Registrar un nuevo movimiento.


## 📦 Variables de Entorno

**¡Crucial!**  Usa **variables de entorno** para configuraciones sensibles (credenciales, URLs, claves).

**Ejemplo (base de datos):**

En lugar de:

```properties
# application.properties (¡NO!)
spring.datasource.url=jdbc:mysql://...
spring.datasource.username=usuario
spring.datasource.password=secreto  # ❌