package com.banpais.api.query.model;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class MovimientoQueyModel {
    private String cuentaOrigen;
    private String cuentaDestino;
    private String fechaMovimiento; // String, porque viene de la trama
    private String tipoMovimiento; // String:  "DEPOSITO", "RETIRO", "TRANSFER"
    private BigDecimal monto;
    private String numeroReferencia; //Se usa en la salida
}