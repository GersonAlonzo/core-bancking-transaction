// TramaParametro.java
package com.banpais.api.infraestructure.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PARAMETROS_TRAMA")
@Getter
@Setter
public class TramaParametro implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARAMETRO")
    private Integer idParametro;

    @NotBlank
    @Size(max = 50)
    @Column(name = "NOMBRE_TRAMA", nullable = false)
    private String nombreTrama;

    @NotBlank
    @Size(max = 50)
    @Column(name = "NOMBRE_CAMPO", nullable = false)
    private String nombreCampo;

    @NotNull
    @Column(name = "POSICION_INICIO", nullable = false)
    private Integer posicionInicio;

    @NotNull
    @Column(name = "LONGITUD", nullable = false)
    private Integer longitud;
}