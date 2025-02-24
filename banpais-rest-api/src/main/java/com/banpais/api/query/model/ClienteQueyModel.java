// ClienteDTO.java
package com.banpais.api.query.model;


import java.time.LocalDate;
import lombok.Data;

@Data
public class ClienteQueyModel {
    private String id;
    private String nombre;
    private String identificacion;
    private String tipoIdentificacion;
    private LocalDate fechaNacimiento;
}