package com.banpais.api.http.utils;

import com.banpais.api.command.model.RegistrarClienteCommandModel;
import java.util.ArrayList;
import java.util.List;

public class ClienteValidator {

    public static void validarCliente(RegistrarClienteCommandModel request) {
        List<String> errores = new ArrayList<>();
        
        if (request == null) {
            throw new IllegalArgumentException("El objeto de cliente no puede ser nulo");
        }
        
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

    private ClienteValidator() {
        // Constructor privado para evitar instanciación
    }
}