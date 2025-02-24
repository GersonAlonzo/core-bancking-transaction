// TipoMovimiento.java
package com.banpais.api.infraestructure.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "TIPO_MOVIMIENTOS")
@Data
public class TipoMovimiento implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotBlank
    @Size(max = 10)
    @Column(name = "CODIGO", length = 10, nullable = false, unique = true)
    private String codigo;

    @NotBlank
    @Size(max = 255)
    @Column(name = "DESCRIPCION", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDateTime fechaRegistro;
}