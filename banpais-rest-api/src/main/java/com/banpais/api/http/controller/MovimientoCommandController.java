package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapMovimientoClient;
import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import com.banpais.soap.client.RegistrarMovimientoResponse; // Importa la clase de respuesta SOAP
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoCommandController {

    private final SoapMovimientoClient soapMovimientoClient;

    @Autowired
    public MovimientoCommandController(SoapMovimientoClient soapMovimientoClient) {
        this.soapMovimientoClient = soapMovimientoClient;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(@RequestBody RegistrarMovimientoCommandModel request) {
        try {
            // 1. Validaciones (igual que antes)
            if (request.getCuentaOrigen() == null || request.getCuentaOrigen().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "La cuenta origen es obligatoria."));
            }
            if (request.getCuentaDestino() == null || request.getCuentaDestino().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "La cuenta destino es obligatoria."));
            }
            if (request.getTipoMovimiento() == null || request.getTipoMovimiento().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "El tipo de movimiento es obligatorio."));
            }
            if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "El monto debe ser mayor que cero."));
            }
            

            // 2. Llamar al SoapClient
            RegistrarMovimientoResponse soapResponse = soapMovimientoClient.registrarMovimiento(request);

            // 3. Procesar la respuesta SOAP (PARSEANDO LA TRAMA)
            if (soapResponse != null && soapResponse.getTrama() != null) {
                String tramaRespuesta = soapResponse.getTrama();

                // Extraer el código de respuesta (los primeros 3 caracteres)
                String codigoRespuesta = tramaRespuesta.substring(0, 3);

                // Extraer el mensaje (el resto de la cadena)
                String mensajeRespuesta = tramaRespuesta.substring(3).trim(); // trim() para eliminar espacios en blanco

                if ("000".equals(codigoRespuesta)) {
                    // Éxito
                    return ResponseEntity.ok(new MovimientoRegistradoResponse(codigoRespuesta, mensajeRespuesta));
                } else {
                    // Error
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(codigoRespuesta, mensajeRespuesta));
                }
            } else {
                //Respuesta Inesperada
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula o incompleta."));
            }

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use yyyy-MM-ddTHH:mm:ss");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }
     // DTO para una respuesta exitosa (crea esta clase en un paquete apropiado)
      // MovimientoRegistradoResponse.java
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MovimientoRegistradoResponse {
            private String codigo;
            private String mensaje;
        }

        // ErrorResponse.java (DTO para la respuesta de error)
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ErrorResponse {
            private String codigo;
            private String mensaje; // Mensaje de error
        }
}