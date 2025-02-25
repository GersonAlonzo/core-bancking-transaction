package com.banpais.api.http.utils;

import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CuentaValidator {

    public static void validarRegistroCuenta(RegistrarCuentaCommandModel request) {
        if (request == null) {
            throw new IllegalArgumentException("El objeto de cuenta no puede ser nulo");
        }

        if (request.getNumeroCuenta() == null || request.getNumeroCuenta().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta es obligatorio.");
        }

        if (request.getClienteId() == null || request.getClienteId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }

        if (request.getEstadoCuenta() == null || request.getEstadoCuenta().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la cuenta es obligatorio.");
        }

        if (request.getSaldo() == null || request.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo.");
        }
    }

    public static void validarEliminacionCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta es obligatorio.");
        }
    }

    // Constructor privado para evitar instanciación
//    private CuentaValidator() {
//    }
}