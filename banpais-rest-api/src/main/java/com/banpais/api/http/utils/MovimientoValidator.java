package com.banpais.api.http.utils;

import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import java.math.BigDecimal;

public class MovimientoValidator {

    public static void validarRegistroMovimiento(RegistrarMovimientoCommandModel request) {
        if (request == null) {
            throw new IllegalArgumentException("El objeto de movimiento no puede ser nulo");
        }

        if (request.getCuentaOrigen() == null || request.getCuentaOrigen().trim().isEmpty()) {
            throw new IllegalArgumentException("La cuenta origen es obligatoria.");
        }

        if (request.getCuentaDestino() == null || request.getCuentaDestino().trim().isEmpty()) {
            throw new IllegalArgumentException("La cuenta destino es obligatoria.");
        }

        if (request.getTipoMovimiento() == null || request.getTipoMovimiento().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio.");
        }

        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
    }

    // Método auxiliar para parsear la trama de respuesta
    public static ParsedResponse parsearTrama(String trama) {
        if (trama == null || trama.trim().isEmpty()) {
            throw new IllegalArgumentException("La trama de respuesta no puede ser nula o vacía.");
        }

        try {
            // Extraer el código de respuesta (los primeros 3 caracteres)
            String codigoRespuesta = trama.substring(0, 3);

            // Extraer el mensaje (el resto de la cadena)
            String mensajeRespuesta = trama.substring(3).trim();

            return new ParsedResponse(codigoRespuesta, mensajeRespuesta);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Formato de trama inválido.");
        }
    }

    // Clase interna para representar la respuesta parseada
    public static class ParsedResponse {
        private final String codigo;
        private final String mensaje;

        public ParsedResponse(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    // Constructor privado para evitar instanciación
    private MovimientoValidator() {
    }
}