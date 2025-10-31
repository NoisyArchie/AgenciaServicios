package com.agenciaservicios.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "refacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refaccion")
    private Integer idRefaccion;

    @Column(name = "nombre_refaccion", nullable = false)
    private String nombreRefaccion;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "activo")
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        if (activo == null) {
            activo = true;
        }
        if (stock == null) {
            stock = 0;
        }
    }
}
