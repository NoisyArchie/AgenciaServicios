package com.agenciaservicios.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicio_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_detalle")
    private Integer idServicioDetalle;

    @ManyToOne
    @JoinColumn(name = "folio_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @PrePersist
    protected void onCreate() {
        // Si no se especifica un costo, tomar el costo base del tipo de servicio
        if (costo == null && tipoServicio != null && tipoServicio.getCostoBase() != null) {
            costo = tipoServicio.getCostoBase();
        }
        if (costo == null) {
            costo = BigDecimal.ZERO;
        }
    }
}
