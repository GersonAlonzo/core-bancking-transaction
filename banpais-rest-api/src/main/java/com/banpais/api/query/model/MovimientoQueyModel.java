package com.banpais.api.query.model;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class MovimientoQueyModel {
    private String cuentaOrigen;
    private String cuentaDestino;
    private String fechaMovimiento; 
    private String tipoMovimiento; 
    private BigDecimal monto;
    private String numeroReferencia; 
}