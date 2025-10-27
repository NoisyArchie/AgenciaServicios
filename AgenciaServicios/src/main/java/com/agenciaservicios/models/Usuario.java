package com.agenciaservicios.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer idUsuario;

    @Column(unique = true,nullable = false,length=50)
    private String username;

    @Column(nullable = false,length=255)
    private String password;

    @Column(name="nombre_completo",nullable = false,length=100)
    private String nombreCompleto;

    @Column(length = 100)
    private String email;

    @Column(name="fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean activo = true;
}
