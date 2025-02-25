package com.banpais.api.config;

import com.banpais.api.http.utils.ErrorResponse;
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
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import org.hibernate.exception.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        logger.warn("Recurso no encontrado (ruta no válida): {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", "El recurso solicitado no existe"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", "El recurso solicitado no se encuentra disponible"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(InputMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInputMismatchException(InputMismatchException ex, WebRequest request) {
        logger.warn("Error en el formato de la entrada: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("FORMAT_ERROR", "El formato de los datos proporcionados es inválido"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Error de integridad de datos: {}", ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("INTEGRITY_ERROR", "Los datos proporcionados violan las restricciones de integridad"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        logger.warn("Error de validación: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("CONSTRAINT_ERROR", "Los datos no cumplen con las restricciones establecidas"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("Error al leer el cuerpo de la solicitud (JSON mal formado o similar): {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("PARSING_ERROR", "No se pudo procesar el formato de los datos enviados"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Error de tipo de dato en la ruta: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("TYPE_MISMATCH_ERROR", "Los parámetros proporcionados tienen un tipo de dato inválido"));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Error de acceso a datos (conexión a la base de datos, etc.): {}", ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("DATABASE_ERROR", "Ocurrió un error al acceder a los datos. Por favor, intente nuevamente más tarde"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Error interno del servidor", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_ERROR", "Ocurrió un error inesperado. Detalles: " + ex.getMessage()));
    }
}