# 📦 Proyecto Spring Boot: API SOAP para Transacciones Bancarias (BANPAIS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3-orange)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT) <!-- O la licencia que aplique -->
<!-- Añade más badges según sea necesario (ej. build status, code coverage) -->

## 📖 Descripción

**TRANSACTIONAL SOAP API** es un microservicio desarrollado con **Spring Boot** que proporciona una interfaz SOAP para gestionar clientes, cuentas y movimientos bancarios.  Este servicio permite realizar operaciones CUD (Crear, Actualizar y Eliminar) sobre estas entidades, utilizando un formato de *trama* de texto plano dentro de los mensajes SOAP.

**Características Principales:**

*   **CUD de Clientes:**  Registrar, actualizar y eliminar clientes.
*   **CUD de Cuentas:**  Registrar, actualizar y eliminar cuentas bancarias.
*   **Registro de Movimientos:**  Registrar movimientos transaccionales (ej. transferencias).
*   **Formato de Trama:**  Utiliza un formato de trama de texto plano para las solicitudes y respuestas SOAP, simplificando la integración con sistemas existentes.
*   **Spring Boot 3.4.3:** Construido sobre la última versión estable de Spring Boot, aprovechando sus características y mejoras de rendimiento.
*   **SOAP con Spring Web Services:**  Implementa servicios web SOAP utilizando Spring Web Services.
*   **Persistencia con Spring Data JPA:**  Interactúa con una base de datos MySQL a través de Spring Data JPA.
*  **Cache con Caffeine:** Implementa el uso de cache con Caffeine para mejorar el rendimiento.
* **Seguridad con Spring Security:** El servicio web esta protegido con Spring Security.

## ⚠️ Importante: Formato de Trama

Este servicio utiliza un formato de **trama de texto plano** para la comunicación SOAP, lo que difiere del enfoque tradicional basado en XML estructurado.  Esto requiere un *parsing* y *formateo* específicos de las tramas en el lado del cliente y del servidor.  Los detalles del formato de la trama se describen en la sección [Formato de Trama](#-formato-de-trama).

## 🛠️ Tecnologías Utilizadas

*   **Spring Boot 3.4.3:** Framework principal para la construcción de la aplicación.
*   **Spring Web Services:** Para la implementación de servicios web SOAP.
*   **Spring Data JPA:** Para la persistencia de datos.
*   **MySQL Connector/J:**  Controlador JDBC para MySQL.
*   **Lombok:**  Para reducir el código boilerplate (getters, setters, constructores, etc.).
*   **ModelMapper:**  Para mapear entre objetos de dominio y DTOs (si aplica).
*   **SpringDoc OpenAPI:**  Para generar documentación de la API (si se expone una API REST además de SOAP).
*   **Mockito:**  Para pruebas unitarias (específicamente *mocks*).
*   **JAXB (Jakarta XML Binding):** Para el manejo de XML (generación de clases a partir de XSD, *marshalling* y *unmarshalling*).
*   **WSDL4J:**  Para manipular archivos WSDL.
*   **Caffeine:** Para la implementación de caché.
*    **Spring Security:** Para proteger el servicio web.
* **Spring Validation:** Para validaciones
## ⚙️ Configuración del Proyecto

### Pre-requisitos

*   **Java 17** o superior.
*   **Maven 3** o superior.
*   **MySQL:**  Una instancia de MySQL en ejecución y accesible.
*   **IDE:**  Un entorno de desarrollo integrado (IDE) como IntelliJ IDEA, Eclipse o Spring Tool Suite (STS).
*   **Git** (opcional, para clonar el repositorio).


3.  **Compilar el proyecto:**

    ```bash
    mvn clean install
    ```
4. **Configurar las propiedades de la aplicación:** Revisar y ajustar las propiedades en `src/main/resources/application.properties` (o .yml), especialmente las relacionadas con la base de datos, el puerto del servidor, y la configuración de SOAP (WSDL, endpoints).
    *   **Puerto del servidor:**
    ```properties
        server.port=8080
    ```
5. **Ejecutar la Aplicación:**

    ```bash
    mvn spring-boot:run
    ```
    La aplicación estará disponible en `http://localhost:8080` (o el puerto configurado).
    El WSDL se encontrará, por defecto, en `http://localhost:8080/ws/banco.wsdl` 

### 🌐 Endpoints SOAP

El servicio expone los siguientes endpoints SOAP:

*   **Clientes:**
    *   `registrarCliente`:  Registra un nuevo cliente.
    *   `actualizarCliente`:  Actualiza un cliente existente.
    *   `eliminarCliente`:  Elimina un cliente.
*   **Cuentas:**
    *   `registrarCuenta`:  Registra una nueva cuenta.
    *   `actualizarCuenta`:  Actualiza una cuenta existente.
    *   `eliminarCuenta`:  Elimina una cuenta.
*   **Movimientos:**
    *   `registrarMovimiento`:  Registra un movimiento transaccional.

La definición completa de los endpoints, incluyendo los tipos de datos y operaciones, se encuentra en el archivo WSDL (`banco.wsdl`).

### 📄 Formato de Trama

Los endpoints SOAP de este servicio utilizan un formato de trama de texto plano para los datos de entrada y salida, parametrizados a nivel de base de datos.  A continuación, se detalla la estructura de las tramas para cada operación.  *Todos los campos son de longitud fija.*

**1.  ejemplo:**

*   **`registrarClienteRequest` y `actualizarClienteRequest`:**

    | Campo          | Longitud (caracteres) | Descripción                                   | Ejemplo       |
    | -------------- | --------------------- | --------------------------------------------- | ------------- |
    | ID Cliente      | 36                    | Identificador único del cliente (UUID)         | `000000000000000000000000000000000000` |
    | Nombre Completo | 40                    | Nombre completo del cliente                    | `Juan Perez           `                  |
    | Identificación  | 20                    | Número de identificación del cliente           | `42345678901234CI          `                  |
    | Fecha Nacimiento| 8                     | Fecha de nacimiento (AAAAMMDD)                | `19900515`    |
    | **Ejemplo de uso:**                                                                                                                                |
     ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://www.example.com/banco">
        <soapenv:Header/>
        <soapenv:Body>
            <ban:registrarClienteRequest>
                <ban:trama>000000000000000000000000000000000000 Juan Perez            42345678901234CI          19900515  </ban:trama>
            </ban:registrarClienteRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```


## 📦 Variables de Entorno

**¡Crucial para la seguridad y la configuración!**  Las propiedades de configuración sensibles (como las credenciales de la base de datos, URLs de servicios externos, claves secretas, etc.) *no deben* estar hardcodeadas en el archivo `application.properties` o `application.yml` en un entorno de producción. En su lugar, se deben utilizar **variables de entorno**.

**Ejemplo (para la base de datos):**

En lugar de:

```properties
# application.properties (NO USAR EN PRODUCCIÓN)
spring.datasource.url=jdbc:mysql://localhost:3306/mi_base_de_datos
spring.datasource.username=mi_usuario
spring.datasource.password=mi_contraseña_secreta  