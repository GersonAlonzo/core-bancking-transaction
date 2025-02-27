// Movimiento.java
package com.banpais.api.infraestructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "MOVIMIENTOS")
@Getter
@Setter
public class Movimiento  implements Serializable{

    @Id
    @NotBlank
    @Size(max = 255)
    @Column(name = "NUMERO_REFERENCIA", nullable = false)
    private String numeroReferencia;

    @NotBlank
    @Size(max = 16)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_ORIGEN", nullable = false)
    private Cuenta cuentaOrigen;

    @NotBlank
    @Size(max = 16)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_DESTINO", nullable = false)
    private Cuenta cuentaDestino;

    @NotNull
    @Column(name = "FECHA_MOVIMIENTO", nullable = false)
    private LocalDateTime fechaMovimiento;

    
    @NotNull
    @Column(name = "HORA_MOVIMIENTO", nullable = false)
    private LocalTime horaMovimiento;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CODIGO_MOVIMIENTO", referencedColumnName = "codigo", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @NotNull
    @Column(name = "MONTO", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;
}