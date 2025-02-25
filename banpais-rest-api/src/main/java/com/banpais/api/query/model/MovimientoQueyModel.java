package com.banpais.api.query.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MovimientoQueyModel {
    private String cuentaOrigen;
    private String cuentaDestino;
    private String fechaMovimiento; 
    private String tipoMovimiento; 
    private BigDecimal monto;
    private String numeroReferencia; 
    
    public MovimientoQueyModel(String numeroReferencia, String cuentaOrigen, 
                             String cuentaDestino, LocalDateTime fechaMovimiento, 
                             BigDecimal monto, String tipoMovimiento) {
        this.numeroReferencia = numeroReferencia;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        
        // Formatear la fecha si no es nula
        if (fechaMovimiento != null) {
            this.fechaMovimiento = fechaMovimiento.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento != null ? tipoMovimiento : "N/A";
    }
}