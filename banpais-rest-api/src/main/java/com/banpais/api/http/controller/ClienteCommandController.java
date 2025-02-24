package com.banpais.api.http.controller;


import com.banpais.api.command.config.client.SoapClienteClient;
import com.banpais.api.command.model.RegistrarClienteCommandModel;
import com.banpais.soap.client.ActualizarClienteResponse;
import com.banpais.soap.client.EliminarClienteResponse;
import com.banpais.soap.client.RegistrarClienteResponse;
import java.util.ArrayList;
import java.util.List;
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
            validarCliente(request);
            RegistrarClienteResponse soapResponse = soapClienteClient.registrarCliente(request);
            return procesarRespuestaSoap(soapResponse, "Cliente registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarCliente(@RequestBody RegistrarClienteCommandModel request) {
        try {
            validarCliente(request);
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("VALIDATION_ERROR", "El ID del cliente es obligatorio para actualizar."));
            }
            ActualizarClienteResponse soapResponse = soapClienteClient.actualizarCliente(request);
            return procesarRespuestaSoap(soapResponse, "Cliente actualizado exitosamente");
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

    private void validarCliente(RegistrarClienteCommandModel request) {
        List<String> errores = new ArrayList<>();
        
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            errores.add("El nombre es obligatorio");
        }
        if (request.getIdentificacion() == null || request.getIdentificacion().trim().isEmpty()) {
            errores.add("La identificación es obligatoria");
        }
        if (request.getTipoIdentificacion() == null || request.getTipoIdentificacion().trim().isEmpty()) {
            errores.add("El tipo de identificación es obligatorio");
        }
        if (request.getFechaNacimiento() == null) {
            errores.add("La fecha de nacimiento es obligatoria");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }

    private ResponseEntity<?> procesarRespuestaSoap(Object soapResponse, String mensajeExito) {
        if (soapResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula"));
        }

        String mensaje = null;
        if (soapResponse instanceof RegistrarClienteResponse) {
            mensaje = ((RegistrarClienteResponse) soapResponse).getMensaje();
        } else if (soapResponse instanceof ActualizarClienteResponse) {
            mensaje = ((ActualizarClienteResponse) soapResponse).getMensaje();
        } else if (soapResponse instanceof EliminarClienteResponse) {
            mensaje = ((EliminarClienteResponse) soapResponse).getMensaje();
        }

        if (mensaje != null) {
            String codigoRespuesta = mensaje.substring(0, 3);
            String mensajeRespuesta = mensaje.substring(3).trim();

            if ("000".equals(codigoRespuesta)) {
                return ResponseEntity.ok(new ClienteResponse(codigoRespuesta, mensajeExito));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(codigoRespuesta, mensajeRespuesta));
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