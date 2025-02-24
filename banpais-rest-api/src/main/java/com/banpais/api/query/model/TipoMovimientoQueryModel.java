// TipoMovimientoQueryModel.java
package com.banpais.api.query.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TipoMovimientoQueryModel {
    private Long id;
    private String codigo;
    private String descripcion;
    private LocalDateTime fechaRegistro;
}