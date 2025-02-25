// Cliente.java
package com.banpais.api.infraestructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "CLIENTES")
@Data
public class Cliente {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @NotBlank 
    @Size(max = 255) 
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 255)
    @Column(name = "IDENTIFICACION", nullable = false, unique = true)
    private String identificacion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "TIPO_IDENTIFICACION", nullable = false)
    private String tipoIdentificacion;

    @NotNull 
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;
}