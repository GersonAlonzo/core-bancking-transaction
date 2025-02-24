package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapCuentaClient;
import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import com.banpais.api.http.utils.CuentaValidator;
import com.banpais.soap.client.EliminarCuentaResponse;
import com.banpais.soap.client.RegistrarCuentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            CuentaValidator.validarRegistroCuenta(request);

            // 2. Llamar al SoapClient
            RegistrarCuentaResponse soapResponse = soapCuentaClient.registrarCuenta(request);

            // 3. Procesar la respuesta SOAP
            return procesarRespuestaSoap(soapResponse, "Cuenta registrada exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable String numeroCuenta) {
        try {
            // 1. Validación
            CuentaValidator.validarEliminacionCuenta(numeroCuenta);

            // 2. Llamar al SoapClient
            EliminarCuentaResponse soapResponse = soapCuentaClient.eliminarCuenta(numeroCuenta);

            // 3. Procesar la respuesta SOAP
            return procesarRespuestaSoap(soapResponse, "Cuenta eliminada exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }

    private ResponseEntity<?> procesarRespuestaSoap(Object soapResponse, String mensajeExito) {
        if (soapResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula"));
        }

        String codigo = null;
        String mensaje = null;

        if (soapResponse instanceof RegistrarCuentaResponse) {
            RegistrarCuentaResponse resp = (RegistrarCuentaResponse) soapResponse;
            codigo = resp.getCodigo();
            mensaje = resp.getMensaje();
        } else if (soapResponse instanceof EliminarCuentaResponse) {
            EliminarCuentaResponse resp = (EliminarCuentaResponse) soapResponse;
            codigo = resp.getCodigo();
            mensaje = resp.getMensaje();
        }

        if (codigo != null && mensaje != null) {
            if ("000".equals(codigo)) {
                return ResponseEntity.ok(new CuentaResponse(codigo, mensajeExito));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(codigo, mensaje));
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ERROR_INESPERADO", "Error procesando la respuesta del servicio"));
    }

    // DTO para respuestas de cuenta
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CuentaResponse {
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
}