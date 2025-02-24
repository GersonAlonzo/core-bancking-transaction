package com.banpais.api.command.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramaParametroCommandModel implements Serializable {
    private Integer idParametro;
    private String nombreTrama;
    private String nombreCampo;
    private Integer posicionInicio;
    private Integer longitud;
}