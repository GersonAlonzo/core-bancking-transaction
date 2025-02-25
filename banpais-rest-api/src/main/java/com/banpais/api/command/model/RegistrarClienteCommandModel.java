package com.banpais.api.command.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistrarClienteCommandModel {
    private String id;
    private String nombre;
    private String identificacion;
    private String tipoIdentificacion;
    private LocalDate fechaNacimiento;
}