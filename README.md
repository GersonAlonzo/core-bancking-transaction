# 📦 Proyecto Transaccional Banpais: API SOAP, API REST y Frontend

[![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-orange?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![React](https://img.shields.io/badge/React-19.0.0-blue?style=for-the-badge&logo=react)](https://reactjs.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.0.9-blue?style=for-the-badge&logo=tailwindcss)](https://tailwindcss.com/)
[![Vite](https://img.shields.io/badge/Vite-6.2.0-purple?style=for-the-badge&logo=vite)](https://vitejs.dev/)

## 📖 Descripción

**Transactional Backing** es un proyecto para gestión de transacciones bancarias que consta de:

* **API SOAP:** Microservicio para procesamiento de transacciones con protocolos SOAP
* **API REST:** Servicio backend para comunicación con clientes modernos a través de REST y consumo soap
* **Frontend React:** Interfaz de usuario moderna e intuitiva para administración

Esta aplicación proporciona una solución integral para la gestión de transacciones bancarias, incluyendo:

* **Arquitectura Moderna:** Separación clara entre servicios backend (SOAP/REST) y frontend, utilizando APIs para la comunicación.
* **Base de Datos Relacional:** Uso de MySQL para el almacenamiento persistente de datos.
* **Documentación Integrada:** Swagger UI para una fácil exploración y prueba de las APIs.
* **Seguridad Implementada:** Autenticación básica para proteger los endpoints de ambos servicios.

## ✨ Características Principales

* **CRUD Completo:** 
  * Gestión de transacciones bancarias
  * Administración de usuarios y clientes
  * Consulta y gestión de cuentas

* **Backend (Spring Boot):**
  * API RESTful bien definida
  * Servicio SOAP para integración con sistemas 
  * Spring Data JPA para interactuar con MySQL
  * Manejo de errores y respuestas informativas
  * Documentación con Swagger (OpenAPI)
  * ModelMapper 
  * Sistema de Caché con Caffeine

* **Frontend (React):**
  * Interfaz intuitiva y responsiva (Tailwind CSS)
  * Componentes reutilizables
  * Comunicación con el backend vía Axios
  * Tablas interactivas con AG Grid
  * React Hook Form para validación de formularios
  * Construcción rápida con Vite

## 🛠️ Tecnologías Utilizadas

### API SOAP

* Java 17
* Spring Boot 3.4.3
* Spring Data JPA
* Spring Security
* JAXB (Jakarta XML Binding)
* wsdl4j
* Lombok
* ModelMapper
* MySQL Connector/J
* SpringDoc OpenAPI (Swagger)

### API REST

* Java 17
* Spring Boot 3.4.3
* Spring Data JPA
* Spring Security
* Jakarta XML/WS API
* Apache CXF 4.0.3
* MySQL Connector/J
* Lombok
* ModelMapper
* SpringDoc OpenAPI (Swagger)

### Frontend

* React 19.0.0
* React Router 7.2.0
* React Hook Form 7.54.2
* Axios 1.7.9
* Tailwind CSS 4.0.9
* AG Grid 33.1.1
* Vite 6.2.0

## 📁 Estructura del Proyecto

```
banpais-transactional-project/
├── api-soap/              # Servicio SOAP
├── api-rest/              # Servicio REST
├── frontend/              # Aplicación React
├── Database/              # Configuracion base de datos
├── docker-compose.yml     # Configuración Docker Compose
└── README.md              # Este archivo
```


## 🚀 Instalación y Ejecución

### Prerrequisitos

* **Docker:** Motor de Docker instalado y en ejecución (Docker Desktop es la forma más fácil).
* **Docker Compose:** Herramienta para definir y ejecutar aplicaciones multi-contenedor (normalmente se incluye con Docker Desktop).
* **Git:** Para clonar el repositorio.

### Pasos

1. **Clonar el Repositorio:**

   ```bash
   git clone https://github.com/YourUsername/banpais-transactional-project.git
   cd ./banpais-transactional-project
   ```

2. **Ejecutar con Docker Compose:**

   Desde el directorio raíz del proyecto (donde está `docker-compose.yml`):

   ```bash
   docker-compose up --build -d
   ```

3. **Validar la conexión a la base de datos:**
   
   Conectarse mediante un cliente MySQL a:
   * Host: localhost
   * Puerto: 3306
   * Usuario: root
   * Contraseña: root

4. **Validar la estabilidad del proyecto:**
   
   Asegurarse que todos los contenedores estén funcionando correctamente por al menos 2 minutos:
   
   ```bash
   docker-compose ps
   ```

5. **Acceder a la Aplicación:**

   * **Frontend:** Abre tu navegador y ve a `http://localhost:5173` (o el puerto configurado)
   * **Backend REST (Swagger UI):** `http://localhost:8080/swagger-ui.html`
   * **Backend SOAP (Swagger UI):** `http://localhost:8080/ws/banco.wsdl`

6. **Detener los Contenedores:**

   ```bash
   docker-compose down
   ```

   Esto detendrá y eliminará los contenedores y la red.

7. **Detener los Contenedores y Eliminar Volúmenes:**

   ```bash
   docker-compose down -v
   ```

   Esto detendrá y eliminará los contenedores, la red, y los volúmenes con datos.

## 🔐 Datos de Seguridad

### Acceso al Servicio SOAP
```
Username: soap.admin
Password: soap.service!00_2
```

### Acceso al Servicio REST
```
Username: admin
Password: admin
```

### Acceso a MySQL
```
Username: root
Password: root
```


## 📖 Documentación API

* **SOAP API Swagger UI:** http://localhost:8081/swagger-ui.html
* **REST API Swagger UI:** http://localhost:8080/ws/banco.wsdl


