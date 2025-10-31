package com.agenciaservicios.services;

import com.agenciaservicios.models.Refaccion;
import com.agenciaservicios.repositories.RefaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RefaccionService {

    @Autowired
    private RefaccionRepository refaccionRepository;

    private void validarRefaccion(Refaccion refaccion) {
        if (refaccion == null) {
            throw new IllegalArgumentException("La refacción no puede ser nula");
        }

        if (refaccion.getNombreRefaccion() == null || refaccion.getNombreRefaccion().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la refacción es requerido");
        }

        if (refaccion.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("El precio unitario es requerido");
        }

        if (refaccion.getPrecioUnitario().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }

        if (refaccion.getStock() == null) {
            throw new IllegalArgumentException("El stock es requerido");
        }

        if (refaccion.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    public List<Refaccion> obtenerTodas() {
        return refaccionRepository.findAll();
    }

    public List<Refaccion> obtenerActivas() {
        return refaccionRepository.findByActivoTrue();
    }

    public List<Refaccion> obtenerDisponibles() {
        return refaccionRepository.findByActivoTrueAndStockGreaterThan(0);
    }

    public List<Refaccion> obtenerPorNombre(String nombre) {
        return refaccionRepository.findByNombreRefaccionContainingIgnoreCase(nombre);
    }

    public List<Refaccion> obtenerConStockBajo(Integer cantidad) {
        return refaccionRepository.findByStockLessThan(cantidad);
    }

    public Optional<Refaccion> obtenerPorId(Integer id) {
        return refaccionRepository.findById(id);
    }

    public Refaccion guardar(Refaccion refaccion) {
        validarRefaccion(refaccion);
        return refaccionRepository.save(refaccion);
    }

    public Refaccion actualizar(Refaccion refaccion) {
        if (refaccion.getIdRefaccion() == null) {
            throw new IllegalArgumentException("El ID de la refacción es requerido para actualizar");
        }

        if (!refaccionRepository.existsById(refaccion.getIdRefaccion())) {
            throw new IllegalArgumentException("Refacción no encontrada con id: " + refaccion.getIdRefaccion());
        }

        validarRefaccion(refaccion);
        return refaccionRepository.save(refaccion);
    }

    public void eliminar(Integer id) {
        if (!refaccionRepository.existsById(id)) {
            throw new IllegalArgumentException("Refacción no encontrada con id: " + id);
        }
        refaccionRepository.deleteById(id);
    }

    public Refaccion activar(Integer id) {
        return refaccionRepository.findById(id)
                .map(refaccion -> {
                    refaccion.setActivo(true);
                    return refaccionRepository.save(refaccion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada con id: " + id));
    }

    public Refaccion desactivar(Integer id) {
        return refaccionRepository.findById(id)
                .map(refaccion -> {
                    refaccion.setActivo(false);
                    return refaccionRepository.save(refaccion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada con id: " + id));
    }

    public Refaccion agregarStock(Integer id, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        return refaccionRepository.findById(id)
                .map(refaccion -> {
                    refaccion.setStock(refaccion.getStock() + cantidad);
                    return refaccionRepository.save(refaccion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada con id: " + id));
    }

    public Refaccion reducirStock(Integer id, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        return refaccionRepository.findById(id)
                .map(refaccion -> {
                    if (refaccion.getStock() < cantidad) {
                        throw new IllegalArgumentException("Stock insuficiente. Disponible: " + refaccion.getStock());
                    }
                    refaccion.setStock(refaccion.getStock() - cantidad);
                    return refaccionRepository.save(refaccion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada con id: " + id));
    }
}
