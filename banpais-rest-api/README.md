# 📦 Proyecto Spring Boot: API Híbrida (REST + SOAP) para Transacciones Bancarias (BANPAIS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3-orange)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT) <!-- O la licencia que aplique -->
<!-- Añade más badges según sea necesario (ej. build status, code coverage) -->

## 📖 Descripción

**BANPAIS API** es un microservicio desarrollado con **Spring Boot** que proporciona una interfaz **híbrida REST y SOAP** para gestionar clientes, cuentas y movimientos bancarios.

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

## ⚠️ Importante: Formato de Trama (SOAP)

Los endpoints SOAP utilizan una **trama de texto plano**.  Los detalles concretos (longitudes de campo, etc.) son *parametrizables*.  Consulta la sección [Formato de Trama (SOAP)](#-formato-de-trama-soap) y la configuración del servicio.

## 🛠️ Tecnologías Utilizadas

*   Spring Boot 3.4.3
*   Spring Web Services / Apache CXF (SOAP)
*   Spring MVC / Jersey (REST)
*   Spring Data JPA
*   MySQL Connector/J
*   Lombok
*   ModelMapper
*   SpringDoc OpenAPI
*   Mockito
*   JAXB
*  Caffeine
* Spring Security
* Spring Validation

    ```xml
    <!-- (Fragmento del pom.xml) -->
  <dependencies>
        <!-- ... otras dependencias ... -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jersey</artifactId>
        </dependency>
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.1</version>
        </dependency>
         <dependency>
            <groupId>jakarta.jws</groupId>
            <artifactId>jakarta.jws-api</artifactId>
            <version>3.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-frontend-jaxws</artifactId>
            <version>${cxf.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-transports-http</artifactId>
            <version>${cxf.version}</version>
        </dependency>
         <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
         <!--security-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!-- ... otras dependencias ... -->
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.cxf</groupId>
                <artifactId>cxf-codegen-plugin</artifactId>
                <version>${cxf.version}</version>
                <executions>
                    <execution>
                        <id>generate-sources</id>
                        <phase>generate-sources</phase>
                        <configuration>
                            <sourceRoot>${project.basedir}/src/main/java</sourceRoot>
                            <defaultOptions>
                                <bindingFiles>
                                    <bindingFile>${project.basedir}/src/main/resources/bindings.xml</bindingFile>
                                </bindingFiles>
                            </defaultOptions>
                            <wsdlOptions>
                                <wsdlOption>
                                    <wsdl>${project.basedir}/src/main/resources/banco.wsdl</wsdl>
                                    <extraargs>
                                        <extraarg>-p</extraarg>
                                        <extraarg>com.banpais.soap.client</extraarg>
                                        <extraarg>-autoNameResolution</extraarg>
                                        <extraarg>-verbose</extraarg>
                                    </extraargs>
                                </wsdlOption>
                            </wsdlOptions>
                        </configuration>
                        <goals>
                            <goal>wsdl2java</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```

## ⚙️ Configuración del Proyecto

### Pre-requisitos

*   Java 17+
*   Maven 3+
*   MySQL
*   IDE (IntelliJ IDEA, Eclipse, etc.)
*   Git (opcional)

### Instalación

1.  **Clonar (opcional):**

    ```bash
    git clone [URL]
    cd [directorio]
    ```
2.  **Base de datos:**
    *   Crear base de datos MySQL.
    *   Configurar credenciales en `application.properties` (o `.yml`).  **¡Usar variables de entorno en producción!**  Ver [Variables de Entorno](#-variables-de-entorno).

        ```properties
        # application.properties (ejemplo)
        spring.datasource.url=jdbc:mysql://localhost:3306/nombre_bd
        spring.datasource.username=usuario
        spring.datasource.password=contraseña
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect # O .MySQL8Dialect
        ```
3.  **Compilar:**

    ```bash
    mvn clean install
    ```
4.  **Configurar propiedades:** Revisar `application.properties` (o `.yml`) (base de datos, puerto, SOAP, REST, *formato de trama SOAP*).
    ```properties
     # Puerto del servidor (ejemplo)
     server.port=8081
    ```

5.  **Ejecutar:**

    ```bash
    mvn spring-boot:run
    ```

    API REST: `http://localhost:8081`
    Swagger (REST): `http://localhost:8081/swagger-ui.html`
    WSDL (SOAP):  (ej. `http://localhost:8081/services/banpais?wsdl`)

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

**Ejemplos (REST):**

*   **Registrar Cliente:**

    ```http
    POST /api/clientes/registrar
    Content-Type: application/json

    {
        "nombre": "Nuevo Cliente",
        "identificacion": "123456789",
        "tipoIdentificacion": "CI",
        "fechaNacimiento": "2000-01-01"
    }
    ```

    ```json
    // Respuesta (éxito)
    { "codigo": "000", "mensaje": "Cliente registrado exitosamente" }
    ```

*Consultar Swagger (`/swagger-ui.html`) para detalles completos (modelos, parámetros, respuestas).*

#### Endpoints SOAP

*   **Clientes:** `registrarCliente`, `actualizarCliente`, `eliminarCliente`
*   **Cuentas:** `registrarCuenta`, `actualizarCuenta`, `eliminarCuenta`
*   **Movimientos:** `registrarMovimiento`

### 📄 Formato de Trama (SOAP)

Los endpoints SOAP usan una trama de *texto plano*.  Los campos y su orden son fijos, pero *las longitudes y otros detalles son parametrizables y se definen en la configuración del servicio*.

**Esquema General (los campos concretos varían según la operación):**

*   **Clientes:**  Generalmente incluyen campos como ID de cliente, nombre, identificación, fecha de nacimiento.
*   **Cuentas:**  Número de cuenta, ID de cliente, fecha y hora de apertura, estado, saldo.
*   **Movimientos:** Cuenta origen, cuenta destino, fecha y hora, tipo de movimiento, monto.
* **Respuesta:** Generalmente, un codigo y un mensaje.

**Ejemplos (ilustrativos, *no* definitivos):**

*   **`registrarClienteRequest` (ejemplo):**

    ```xml
    <soapenv:Envelope ...>
        <soapenv:Body>
            <ban:registrarClienteRequest>
                <ban:trama>...datos del cliente en formato de texto plano...</ban:trama>
            </ban:registrarClienteRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```

*   **`registrarCuentaRequest` (ejemplo):**

    ```xml
    <soapenv:Envelope ...>
        <soapenv:Body>
            <ban:registrarCuentaRequest>
                <ban:trama>...datos de la cuenta en formato de texto plano...</ban:trama>
            </ban:registrarCuentaRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```

*   **Respuesta (ejemplo):**

    ```xml
    <SOAP-ENV:Envelope ...>
        <SOAP-ENV:Body>
            <ns2:registrarClienteResponse ...>
                <ns2:codigo>000</ns2:codigo>
                <ns2:mensaje>Cliente registrado</ns2:mensaje>
            </ns2:registrarClienteResponse>
        </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>
    ```

**Importante:**

*   Los espacios en blanco pueden ser significativos (relleno).
*   Los montos suelen tener decimales implícitos.
*   *Consulta la configuración del servicio para obtener los detalles precisos del formato de trama.*  Esto puede incluir archivos de configuración, clases Java que definen el formato, o documentación específica del proyecto.  *No* asumas longitudes fijas; verifica siempre la configuración.

## 📦 Variables de Entorno

**¡Crucial!**  Usa **variables de entorno** para configuraciones sensibles (credenciales, URLs, claves).

**Ejemplo (base de datos):**

En lugar de:

```properties
# application.properties (¡NO!)
spring.datasource.url=jdbc:mysql://...
spring.datasource.username=usuario
spring.datasource.password=secreto  # ❌