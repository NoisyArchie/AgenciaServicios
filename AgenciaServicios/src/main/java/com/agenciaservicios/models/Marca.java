package com.agenciaservicios.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Marcas")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Marca {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_marca")
    private Integer idMarca;

    @Column(name="nombre_marca",unique=true,nullable=false,length=50)
    private String nombreMarca;

    @Column(nullable=false)
    private Boolean activo = true;

    //Relacion uno a muchos con Modelo
    //Si borro una marca, se borran todos sus modelos asociados
    @OneToMany(mappedBy = "marca", cascade=CascadeType.ALL)
    @JsonIgnoreProperties({"marca"})
    private java.util.List<Modelo> modelos;
}
