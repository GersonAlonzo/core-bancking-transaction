# 📦 Proyecto Spring Boot: API SOAP para Transacciones Bancarias (BANPAIS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3-orange)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT) <!-- O la licencia que aplique -->
<!-- Añade más badges según sea necesario (ej. build status, code coverage) -->

## 📖 Descripción

**BANPAIS SOAP API** es un microservicio desarrollado con **Spring Boot** que proporciona una interfaz SOAP para gestionar clientes, cuentas y movimientos bancarios.  Este servicio permite realizar operaciones CUD (Crear, Actualizar y Eliminar) sobre estas entidades, utilizando un formato de *trama* de texto plano dentro de los mensajes SOAP.

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

    ```xml
    <!-- (Fragmento del pom.xml) -->
    <dependencies>
        <!-- ... otras dependencias ... -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web-services</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>wsdl4j</groupId>
            <artifactId>wsdl4j</artifactId>
            <version>1.6.3</version>
        </dependency>
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>4.0.5</version>
            <scope>runtime</scope>
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
            <!-- ... otros plugins ... -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>jaxb2-maven-plugin</artifactId>
                <version>3.1.0</version>
                <executions>
                    <execution>
                        <id>xjc</id>
                        <goals>
                            <goal>xjc</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <sources>
                        <source>${project.basedir}/src/main/resources/xsd</source>
                    </sources>
					<outputDirectory>${project.basedir}/src/main/java</outputDirectory>
                    <clearOutputDir>false</clearOutputDir>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```

## ⚙️ Configuración del Proyecto

### Pre-requisitos

*   **Java 17** o superior.
*   **Maven 3** o superior.
*   **MySQL:**  Una instancia de MySQL en ejecución y accesible.
*   **IDE:**  Un entorno de desarrollo integrado (IDE) como IntelliJ IDEA, Eclipse o Spring Tool Suite (STS).
*   **Git** (opcional, para clonar el repositorio).

### Instalación

1.  **Clonar el repositorio (opcional):**

    ```bash
    git clone [URL del repositorio]
    cd [nombre del directorio del proyecto]
    ```
2.  **Configurar la base de datos:**
    *   Crear una base de datos MySQL.
    *   Configurar las credenciales de la base de datos en el archivo `application.properties` (o `application.yml`) ubicado en `src/main/resources`.  **¡Importante para producción!** Ver la sección [Variables de Entorno](#-variables-de-entorno).

        ```properties
        # application.properties (ejemplo)
        spring.datasource.url=jdbc:mysql://localhost:3306/nombre_de_la_base_de_datos
        spring.datasource.username=usuario
        spring.datasource.password=contraseña
        spring.jpa.hibernate.ddl-auto=update  # O create, validate, etc. según sea necesario
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect #O .MySQL8Dialect
        ```

        ```yaml
        # application.yml (ejemplo)
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/nombre_de_la_base_de_datos
            username: usuario
            password: contraseña
          jpa:
            hibernate:
              ddl-auto: update  # O create, validate, etc. según sea necesario
            properties:
              hibernate:
                dialect: org.hibernate.dialect.MySQLDialect # O .MySQL8Dialect
        ```
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
    El WSDL se encontrará, por defecto, en `http://localhost:8080/ws/banpais.wsdl` (revisar y ajustar en `WebServiceConfig.java` la ruta).

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

La definición completa de los endpoints, incluyendo los tipos de datos y operaciones, se encuentra en el archivo WSDL (`banpais.wsdl`).

### 📄 Formato de Trama

Los endpoints SOAP de este servicio utilizan un formato de trama de texto plano para los datos de entrada y salida.  A continuación, se detalla la estructura de las tramas para cada operación.  *Todos los campos son de longitud fija.*

**1.  Clientes:**

*   **`registrarClienteRequest` y `actualizarClienteRequest`:**

    | Campo          | Longitud (caracteres) | Descripción                                   | Ejemplo       |
    | -------------- | --------------------- | --------------------------------------------- | ------------- |
    | ID Cliente      | 36                    | Identificador único del cliente (UUID)         | `000000000000000000000000000000000000` |
    | Nombre Completo | 40                    | Nombre completo del cliente                    | `Cuan Perez           `                  |
    | Identificación  | 20                    | Número de identificación del cliente           | `42345678901234CI          `                  |
    | Fecha Nacimiento| 8                     | Fecha de nacimiento (AAAAMMDD)                | `19900515`    |
    | **Ejemplo de uso:**                                                                                                                                |
     ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://www.example.com/banco">
        <soapenv:Header/>
        <soapenv:Body>
            <ban:registrarClienteRequest>
                <ban:trama>000000000000000000000000000000000000 Cuan Perez            42345678901234CI          19900515  </ban:trama>
            </ban:registrarClienteRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```

*   **`eliminarClienteRequest`:**
  | Campo        | Longitud (caracteres) | Descripción                             | Ejemplo                                  |
    | ------------ | --------------------- | --------------------------------------- | ---------------------------------------- |
    | ID Cliente    | 36                    | Identificador único del cliente (UUID)  | `f47ac10b-58cc-4372-a567-0e02b2c3d479` |
     | **Ejemplo de uso:**                                                                                                                                |
     ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://www.example.com/banco">
        <soapenv:Header/>
        <soapenv:Body>
            <ban:eliminarClienteRequest>
                <ban:trama>f47ac10b-58cc-4372-a567-0e02b2c3d479</ban:trama>
            </ban:eliminarClienteRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```

*   **Respuesta (ejemplo para `registrarClienteResponse`):**

    | Campo   | Longitud | Descripción                        | Ejemplo                      |
    | ------- | -------- | ---------------------------------- | ---------------------------- |
    | Código  | 3        | Código de respuesta (000 = éxito)   | `000`                        |
    | Mensaje | \*       | Mensaje descriptivo de la respuesta | `Cliente registrado exitosamente` |

     ```xml
    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
        <SOAP-ENV:Header/>
        <SOAP-ENV:Body>
            <ns2:registrarClienteResponse xmlns:ns2="http://www.example.com/banco">
                <ns2:codigo>000</ns2:codigo>
                <ns2:mensaje>Cliente registrado exitosamente</ns2:mensaje>
            </ns2:registrarClienteResponse>
        </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>
    ```

**2.  Cuentas:**

*   **`registrarCuentaRequest` y `actualizarCuentaRequest`:**

    | Campo           | Longitud (caracteres) | Descripción                                       | Ejemplo             |
    | --------------- | --------------------- | ------------------------------------------------- | ------------------- |
    | Número Cuenta   | 16                    | Número de cuenta                                   | `0000000000000000`    |
    | ID Cliente       | 36                    | Identificador único del cliente (UUID)             | `f47ac10b-58cc-4372-a567-0e02b2c3d471` |
    | Fecha apertura | 14                    | Fecha de apertura de la cuenta (AAAAMMDDHHMMSS)     | `19900515143000`    |
    | Hora apertura    |6                      |Hora de apertura, solo la hora (HHMMSS)           |     `000000` |
    | Estado Cuenta   | 10                    | Estado de la cuenta (ACTIVA, INACTIVA, etc.)       | `ACTIVA            `    |
    | Saldo           | 20                    | Saldo de la cuenta (con dos decimales, sin separador) | `200            `       |
     | **Ejemplo de uso:**                                                                                                                                |
    ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://www.example.com/banco">
        <soapenv:Header/>
        <soapenv:Body>
            <ban:registrarCuentaRequest>
                <ban:trama>0000000000000000 f47ac10b-58cc-4372-a567-0e02b2c3d471 19900515143000              000000    ACTIVA                                              200            </ban:trama>
            </ban:registrarCuentaRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```
*   **`eliminarCuentaRequest`:**

    | Campo         | Longitud (caracteres) | Descripción                | Ejemplo              |
    | ------------- | --------------------- | -------------------------- | -------------------- |
    | Número Cuenta | 16                    | Número de cuenta a eliminar | `1234567890123456`   |

**3.  Movimientos:**

*   **`registrarMovimientoRequest`:**

    | Campo             | Longitud (caracteres) | Descripción                                        | Ejemplo          |
    | ----------------- | --------------------- | -------------------------------------------------- | ---------------- |
    | Cuenta Origen     | 16                    | Número de cuenta origen                            | `1234567892012345` |
    | Cuenta Destino    | 16                    | Número de cuenta destino                           | `2234567892012345` |
    | Fecha             |  14                    | Fecha y hora de la transacción  (AAAAMMDDHHMMSS)     |`19900515143000`|
    | Tipo Movimiento   | 10                    | Tipo de movimiento (TRANSFER, DEPOSITO, RETIRO, etc.) | `TRANSFER  `     |
    | Monto             | 20                    | Monto de la transacción (con dos decimales, sin separador)| `000000010000.00` |
    | **Ejemplo de uso:**                                                                                                                                |

     ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://www.example.com/banco">
        <soapenv:Header/>
        <soapenv:Body>
            <ban:registrarMovimientoRequest>
                <!-- Trama de entrada para realizarMovimiento -->
                <ban:trama>12345678920123452234567892012345  19900515143000            TRANSFER  000000010000.00</ban:trama>
            </ban:registrarMovimientoRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    ```

**Notas sobre el formato:**

*   Los espacios en blanco son significativos para rellenar los campos de longitud fija.
*   Los montos se representan como cadenas de texto con dos decimales implícitos (sin punto decimal).  Por ejemplo, `10000.00` se representa como `000000010000.00`.
*   Las fechas en registrar y actualizar cuenta tienen dos campos, uno para AAAAMMDDHHMMSS, y otro solo para HHMMSS
*   Es *crucial* que los clientes que consuman este servicio construyan las tramas correctamente, respetando las longitudes y los formatos especificados.

## 📦 Variables de Entorno

**¡Crucial para la seguridad y la configuración!**  Las propiedades de configuración sensibles (como las credenciales de la base de datos, URLs de servicios externos, claves secretas, etc.) *no deben* estar hardcodeadas en el archivo `application.properties` o `application.yml` en un entorno de producción. En su lugar, se deben utilizar **variables de entorno**.

**Ejemplo (para la base de datos):**

En lugar de:

```properties
# application.properties (NO USAR EN PRODUCCIÓN)
spring.datasource.url=jdbc:mysql://localhost:3306/mi_base_de_datos
spring.datasource.username=mi_usuario
spring.datasource.password=mi_contraseña_secreta  # ❌ ¡MAL!