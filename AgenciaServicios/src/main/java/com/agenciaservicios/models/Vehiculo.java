package com.agenciaservicios.models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name="vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_vehiculo")
    private Integer idVehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_cliente", nullable=false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_modelo", nullable=false)
    private Modelo modelo;

    @Column(name="anio")
    private Integer anio;

    @Column(name="numero_serie", unique=true, nullable=false, length=50)
    private String numeroSerie;

    @Column(name="placas")
    private String placas;

    @Column(name="color")
    private String color;

    @Column(name="kilometraje")
    private Integer kilometraje;

    @Column(name="fecha_registro")
    private LocalDate fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }
}
