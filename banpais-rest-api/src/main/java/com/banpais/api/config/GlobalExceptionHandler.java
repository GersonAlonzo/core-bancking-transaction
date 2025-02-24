package com.banpais.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException; // Importa esta excepción
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import org.hibernate.exception.ConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Not Found - Recurso no encontrado (e.g., /api/movimientos)
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        logger.warn("Recurso no encontrado (ruta no válida): {}", ex.getMessage());
        return new ResponseEntity<>("Recurso no encontrado", HttpStatus.NOT_FOUND);
    }


    //  404 - Not Found - Elemento no encontrado (en base de datos)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        return new ResponseEntity<>("Recurso no encontrado", HttpStatus.NOT_FOUND);
    }

    // 400 - Bad Request - IllegalArgumentException (ej: ID nulo)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Argumento inválido: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - Bad Request - InputMismatchException (ej: formato incorrecto)
    @ExceptionHandler(InputMismatchException.class)
    @ResponseBody
    public ResponseEntity<String> handleInputMismatchException(InputMismatchException ex, WebRequest request) {
        logger.warn("Error en el formato de la entrada: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - Bad Request - DataIntegrityViolationException (ej: clave única duplicada)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Error de integridad de datos: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Error de integridad de datos. Verifique los datos ingresados.", HttpStatus.BAD_REQUEST); // o 409 Conflict
    }

    // 400 - Bad Request - ConstraintViolationException (ej: falló validación @Valid)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        logger.warn("Error de validación: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - Bad Request - HttpMessageNotReadableException (ej: JSON mal formado)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("Error al leer el cuerpo de la solicitud (JSON mal formado o similar): {}", ex.getMessage());
        return new ResponseEntity<>("Error al procesar la solicitud. Verifique el formato de los datos enviados.", HttpStatus.BAD_REQUEST);
    }

    // 400 - Bad Request - MethodArgumentTypeMismatchException (ej: tipo de dato incorrecto en la ruta)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Error de tipo de dato en la ruta: {}", ex.getMessage());
        return new ResponseEntity<>("Error en la solicitud. Verifique los parámetros en la URL.", HttpStatus.BAD_REQUEST);
    }

    // 500 - Internal Server Error - DataAccessException (ej: error de conexión a la base de datos)
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Error de acceso a datos (conexión a la base de datos, etc.): {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Error al acceder a los datos. Intente nuevamente más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 500 - Internal Server Error - Catch-all para otras excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Error interno del servidor", ex);
        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}