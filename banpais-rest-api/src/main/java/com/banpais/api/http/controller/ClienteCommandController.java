package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapClienteClient;
import com.banpais.api.command.model.RegistrarClienteCommandModel;
import com.banpais.api.http.utils.ClienteValidator;
import com.banpais.soap.client.ActualizarClienteResponse;
import com.banpais.soap.client.EliminarClienteResponse;
import com.banpais.soap.client.RegistrarClienteResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteCommandController {

    private final SoapClienteClient soapClienteClient;

    @Autowired
    public ClienteCommandController(SoapClienteClient soapClienteClient) {
        this.soapClienteClient = soapClienteClient;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistrarClienteCommandModel request) {
        try {
            // Validación movida al ClienteValidator
            ClienteValidator.validarCliente(request);
            RegistrarClienteResponse soapResponse = soapClienteClient.registrarCliente(request);
            return procesarRespuestaSoap(soapResponse, "Cliente registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarCliente(@RequestBody RegistrarClienteCommandModel request) {
        try {
            // Validación movida al ClienteValidator
            ClienteValidator.validarCliente(request);
            
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El ID del cliente es obligatorio para actualizar."));
            }
            
            ActualizarClienteResponse soapResponse = soapClienteClient.actualizarCliente(request);
            return procesarRespuestaSoap(soapResponse, "Cliente actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String clienteId) {
        try {
            if (clienteId == null || clienteId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El ID del cliente es obligatorio."));
            }
            
            EliminarClienteResponse soapResponse = soapClienteClient.eliminarCliente(clienteId);
            return procesarRespuestaSoap(soapResponse, "Cliente eliminado exitosamente");
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

        if (soapResponse instanceof RegistrarClienteResponse) {
            RegistrarClienteResponse resp = (RegistrarClienteResponse) soapResponse;
            codigo = resp.getCodigo();
            mensaje = resp.getMensaje();
        } else if (soapResponse instanceof ActualizarClienteResponse) {
            ActualizarClienteResponse resp = (ActualizarClienteResponse) soapResponse;
            codigo = resp.getCodigo();
            mensaje = resp.getMensaje();
        } else if (soapResponse instanceof EliminarClienteResponse) {
            EliminarClienteResponse resp = (EliminarClienteResponse) soapResponse;
            codigo = resp.getCodigo();
            mensaje = resp.getMensaje();
        }

        if (codigo != null && mensaje != null) {
            if ("000".equals(codigo)) {
                return ResponseEntity.ok(new ClienteResponse(codigo, mensajeExito));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(codigo, mensaje));
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ERROR_INESPERADO", "Error procesando la respuesta del servicio"));
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class ClienteResponse {
        private String codigo;
        private String mensaje;
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class ErrorResponse {
        private String codigo;
        private String mensaje;
    }
}