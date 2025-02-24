// Cuenta.java
package com.banpais.api.infraestructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Entity
@Table(name = "CUENTAS")
@Data
public class Cuenta implements Serializable{

    @Id
    @NotBlank
    @Size(max = 16)
    @Column(name = "NUMERO_CUENTA", length = 16, nullable = false)
    private String numeroCuenta;

    @NotBlank
    @Size(max = 36)
//    @ManyToOne(fetch = FetchType.LAZY)LAZY
    @Column(name = "FK_ID_CLIENTE", nullable = false)
    private String cliente;

	@Column(name = "FECHA_APERTURA", nullable = false)
    private LocalDateTime fechaApertura;

    @NotNull
    @Column(name = "HORA_APERTURA", nullable = false)
    private LocalTime horaApertura;

    @NotBlank
    @Size(max = 50)
    @Column(name = "ESTADO_CUENTA", nullable = false)
    private String estadoCuenta;

    @NotNull
    @Column(name = "SALDO", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;
}