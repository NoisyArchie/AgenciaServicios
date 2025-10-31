package com.agenciaservicios.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicio_refacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRefaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_refaccion")
    private Integer idServicioRefaccion;

    @ManyToOne
    @JoinColumn(name = "folio_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_refaccion", nullable = false)
    private Refaccion refaccion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAplicado;

    @PrePersist
    protected void onCreate() {
        // Si no se especifica un precio, tomar el precio unitario de la refacción
        if (precioAplicado == null && refaccion != null && refaccion.getPrecioUnitario() != null) {
            precioAplicado = refaccion.getPrecioUnitario();
        }
        if (precioAplicado == null) {
            precioAplicado = BigDecimal.ZERO;
        }
        if (cantidad == null) {
            cantidad = 1;
        }
    }

    // Método auxiliar para calcular el subtotal
    @Transient
    public BigDecimal getSubtotal() {
        return precioAplicado.multiply(new BigDecimal(cantidad));
    }
}
