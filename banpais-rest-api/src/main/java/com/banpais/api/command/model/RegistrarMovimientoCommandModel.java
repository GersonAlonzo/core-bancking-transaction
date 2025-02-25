/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banpais.api.command.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author gcalvaradoa
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarMovimientoCommandModel {
    
    
    private String cuentaOrigen;
    private String cuentaDestino;
    private LocalDateTime fechaMovimiento; 
    private String tipoMovimiento;
    private BigDecimal monto;
    
}
