package com.agenciaservicios.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folio_servicio")
    private Integer folioServicio;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_estimada_entrega")
    private LocalDate fechaEstimadaEntrega;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "tipo_mantenimiento", length = 50)
    private String tipoMantenimiento;

    @Column(name = "estatus", length = 20)
    private String estatus;

    @Column(name = "kilometraje_actual")
    private Integer kilometrajeActual;

    @Column(name = "proximo_servicio_km")
    private Integer proximoServicioKm;

    @Column(name = "fecha_proximo_servicio")
    private LocalDate fechaProximoServicio;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "costo_total", precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "quien_entrego", length = 100)
    private String quienEntrego;

    @Column(name = "telefono_quien_entrego", length = 20)
    private String telefonoQuienEntrego;

    @PrePersist
    protected void onCreate() {
        if (fechaIngreso == null) {
            fechaIngreso = LocalDateTime.now();
        }
        if (estatus == null) {
            estatus = "PENDIENTE";
        }
        if (costoTotal == null) {
            costoTotal = BigDecimal.ZERO;
        }
    }
}
