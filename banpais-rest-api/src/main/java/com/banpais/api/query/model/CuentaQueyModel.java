package com.banpais.api.query.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class CuentaQueyModel {

    private String numeroCuenta;
    private String clienteId; // ID del cliente como String
    private LocalDateTime fechaApertura;
    private LocalTime horaApertura;
    private String estadoCuenta;
    private BigDecimal saldo;
}