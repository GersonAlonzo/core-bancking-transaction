package com.banpais.api.http.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SoapResponseProcessor {

    public static ResponseEntity<?> procesarRespuestaSoap(Object soapResponse, String mensajeExito) {
        if (soapResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula"));
        }

        String codigo = null;
        String mensaje = null;

        try {
            // Usa reflection para obtener los métodos getCodigo() y getMensaje()
            codigo = (String) soapResponse.getClass().getMethod("getCodigo").invoke(soapResponse);
            mensaje = (String) soapResponse.getClass().getMethod("getMensaje").invoke(soapResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("REFLECTION_ERROR", "Error al procesar la respuesta SOAP"));
        }

        if (codigo != null && mensaje != null) {
            if ("000".equals(codigo)) {
                return ResponseEntity.ok(new SuccessResponse(codigo, mensajeExito));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(codigo, mensaje));
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ERROR_INESPERADO", "Error procesando la respuesta del servicio"));
    }

    // Constructor privado para evitar instanciación
    private SoapResponseProcessor() {
    }
}