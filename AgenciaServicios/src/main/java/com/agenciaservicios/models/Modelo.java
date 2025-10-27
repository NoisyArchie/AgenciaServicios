package com.agenciaservicios.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Modelos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_modelo")
    private Integer idModelo;

    // Relación Many-to-One con Marca
    //Eager fetch type para cargar la marca junto con el modelo
    //joincolumn indica la columna que actúa como clave foránea
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_marca", nullable=false)
    @JsonIgnoreProperties({"modelos"})
    private Marca marca;




    @Column(name="nombre_modelo", nullable=false, length=50)
    private String nombreModelo;

    @Column(nullable=false)
    private Boolean activo = true;
}
