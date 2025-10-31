package com.agenciaservicios.services;

import com.agenciaservicios.models.ServicioRefaccion;
import com.agenciaservicios.repositories.ServicioRefaccionRepository;
import com.agenciaservicios.repositories.RefaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioRefaccionService {

    @Autowired
    private ServicioRefaccionRepository servicioRefaccionRepository;

    @Autowired
    private RefaccionRepository refaccionRepository;

    private void validarServicioRefaccion(ServicioRefaccion servicioRefaccion) {
        if (servicioRefaccion == null) {
            throw new IllegalArgumentException("La refacción del servicio no puede ser nula");
        }

        if (servicioRefaccion.getServicio() == null || servicioRefaccion.getServicio().getFolioServicio() == null) {
            throw new IllegalArgumentException("El servicio es requerido");
        }

        if (servicioRefaccion.getRefaccion() == null || servicioRefaccion.getRefaccion().getIdRefaccion() == null) {
            throw new IllegalArgumentException("La refacción es requerida");
        }

        if (servicioRefaccion.getCantidad() == null || servicioRefaccion.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (servicioRefaccion.getPrecioAplicado() == null) {
            throw new IllegalArgumentException("El precio aplicado es requerido");
        }

        if (servicioRefaccion.getPrecioAplicado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio aplicado no puede ser negativo");
        }

        // Validar que haya stock suficiente (solo al crear)
        if (servicioRefaccion.getIdServicioRefaccion() == null) {
            var refaccion = refaccionRepository.findById(servicioRefaccion.getRefaccion().getIdRefaccion())
                    .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada"));

            if (refaccion.getStock() < servicioRefaccion.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + refaccion.getStock() +
                        ", Solicitado: " + servicioRefaccion.getCantidad());
            }
        }
    }

    public List<ServicioRefaccion> obtenerTodos() {
        return servicioRefaccionRepository.findAll();
    }

    public Optional<ServicioRefaccion> obtenerPorId(Integer id) {
        return servicioRefaccionRepository.findById(id);
    }

    public List<ServicioRefaccion> obtenerPorServicio(Integer folioServicio) {
        return servicioRefaccionRepository.findByServicioFolioServicio(folioServicio);
    }

    public List<ServicioRefaccion> obtenerPorRefaccion(Integer idRefaccion) {
        return servicioRefaccionRepository.findByRefaccionIdRefaccion(idRefaccion);
    }

    public BigDecimal calcularTotalRefacciones(Integer folioServicio) {
        BigDecimal total = servicioRefaccionRepository.calcularTotalRefaccionesPorServicio(folioServicio);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public ServicioRefaccion guardar(ServicioRefaccion servicioRefaccion) {
        validarServicioRefaccion(servicioRefaccion);

        // Reducir el stock de la refacción
        var refaccion = refaccionRepository.findById(servicioRefaccion.getRefaccion().getIdRefaccion())
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada"));

        refaccion.setStock(refaccion.getStock() - servicioRefaccion.getCantidad());
        refaccionRepository.save(refaccion);

        return servicioRefaccionRepository.save(servicioRefaccion);
    }

    @Transactional
    public ServicioRefaccion actualizar(ServicioRefaccion servicioRefaccion) {
        if (servicioRefaccion.getIdServicioRefaccion() == null) {
            throw new IllegalArgumentException("El ID es requerido para actualizar");
        }

        // Obtener la refacción anterior para ajustar el stock
        var servicioRefaccionAnterior = servicioRefaccionRepository.findById(servicioRefaccion.getIdServicioRefaccion())
                .orElseThrow(() -> new IllegalArgumentException("Servicio refacción no encontrado"));

        // Restaurar el stock de la cantidad anterior
        var refaccionAnterior = refaccionRepository.findById(servicioRefaccionAnterior.getRefaccion().getIdRefaccion())
                .orElseThrow(() -> new IllegalArgumentException("Refacción anterior no encontrada"));
        refaccionAnterior.setStock(refaccionAnterior.getStock() + servicioRefaccionAnterior.getCantidad());
        refaccionRepository.save(refaccionAnterior);

        validarServicioRefaccion(servicioRefaccion);

        // Reducir el stock con la nueva cantidad
        var refaccionNueva = refaccionRepository.findById(servicioRefaccion.getRefaccion().getIdRefaccion())
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada"));
        refaccionNueva.setStock(refaccionNueva.getStock() - servicioRefaccion.getCantidad());
        refaccionRepository.save(refaccionNueva);

        return servicioRefaccionRepository.save(servicioRefaccion);
    }

    @Transactional
    public void eliminar(Integer id) {
        var servicioRefaccion = servicioRefaccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio refacción no encontrado"));

        // Restaurar el stock
        var refaccion = refaccionRepository.findById(servicioRefaccion.getRefaccion().getIdRefaccion())
                .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada"));
        refaccion.setStock(refaccion.getStock() + servicioRefaccion.getCantidad());
        refaccionRepository.save(refaccion);

        servicioRefaccionRepository.deleteById(id);
    }

    @Transactional
    public void eliminarPorServicio(Integer folioServicio) {
        // Restaurar el stock de todas las refacciones antes de eliminar
        List<ServicioRefaccion> refacciones = servicioRefaccionRepository.findByServicioFolioServicio(folioServicio);
        for (ServicioRefaccion sr : refacciones) {
            var refaccion = refaccionRepository.findById(sr.getRefaccion().getIdRefaccion())
                    .orElseThrow(() -> new IllegalArgumentException("Refacción no encontrada"));
            refaccion.setStock(refaccion.getStock() + sr.getCantidad());
            refaccionRepository.save(refaccion);
        }

        servicioRefaccionRepository.deleteByServicioFolioServicio(folioServicio);
    }
}
