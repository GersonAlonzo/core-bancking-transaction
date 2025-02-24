package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapCuentaClient;
import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import com.banpais.soap.client.EliminarCuentaResponse;
import com.banpais.soap.client.RegistrarCuentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaCommandController {

    private final SoapCuentaClient soapCuentaClient;

    @Autowired
    public CuentaCommandController(SoapCuentaClient soapCuentaClient) {
        this.soapCuentaClient = soapCuentaClient;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCuenta(@RequestBody RegistrarCuentaCommandModel request) {
        try {
            // 1. Validaciones
            if (request.getNumeroCuenta() == null || request.getNumeroCuenta().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El número de cuenta es obligatorio."));
            }
            if (request.getClienteId() == null || request.getClienteId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El ID del cliente es obligatorio."));
            }
            if (request.getEstadoCuenta() == null || request.getEstadoCuenta().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El estado de la cuenta es obligatorio."));
            }
            if (request.getSaldo() == null || request.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El saldo no puede ser negativo."));
            }

            // 2. Llamar al SoapClient
            RegistrarCuentaResponse soapResponse = soapCuentaClient.registrarCuenta(request);

            // 3. Procesar la respuesta SOAP
            if (soapResponse != null && soapResponse.getMensaje() != null) {
                String tramaRespuesta = soapResponse.getMensaje();

                // Extraer el código de respuesta (los primeros 3 caracteres)
                String codigoRespuesta = tramaRespuesta.substring(0, 3);

                // Extraer el mensaje (el resto de la cadena)
                String mensajeRespuesta = tramaRespuesta.substring(3).trim();

                if ("000".equals(codigoRespuesta)) {
                    // Éxito
                    return ResponseEntity.ok(new CuentaRegistradaResponse(codigoRespuesta, mensajeRespuesta));
                } else {
                    // Error
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(codigoRespuesta, mensajeRespuesta));
                }
            } else {
                // Respuesta Inesperada
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula o incompleta."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable String numeroCuenta) {
        try {
            // 1. Validación básica
            if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El número de cuenta es obligatorio."));
            }

            // 2. Llamar al SoapClient
            EliminarCuentaResponse soapResponse = soapCuentaClient.eliminarCuenta(numeroCuenta);

            // 3. Procesar la respuesta SOAP
            if (soapResponse != null && soapResponse.getMensaje() != null) {
                String tramaRespuesta = soapResponse.getMensaje();
                String codigoRespuesta = tramaRespuesta.substring(0, 3);
                String mensajeRespuesta = tramaRespuesta.substring(3).trim();

                if ("000".equals(codigoRespuesta)) {
                    return ResponseEntity.ok(new CuentaEliminadaResponse(codigoRespuesta, mensajeRespuesta));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(codigoRespuesta, mensajeRespuesta));
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula o incompleta."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }


    // DTO para una respuesta exitosa
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CuentaRegistradaResponse {
        private String codigo;
        private String mensaje;
    }

    // DTO para la respuesta de error
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {
        private String codigo;
        private String mensaje;
    }
    
    
    // DTO adicional para respuesta de eliminación
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CuentaEliminadaResponse {
        private String codigo;
        private String mensaje;
    }
}