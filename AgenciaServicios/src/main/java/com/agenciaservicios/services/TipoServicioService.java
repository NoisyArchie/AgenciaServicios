package com.agenciaservicios.services;

import com.agenciaservicios.models.TipoServicio;
import com.agenciaservicios.repositories.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipoServicioService {

    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    private void validarTipoServicio(TipoServicio tipoServicio) {
        if (tipoServicio == null) {
            throw new IllegalArgumentException("El tipo de servicio no puede ser nulo");
        }

        if (tipoServicio.getNombreServicio() == null || tipoServicio.getNombreServicio().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es requerido");
        }

        if (tipoServicio.getCostoBase() == null) {
            throw new IllegalArgumentException("El costo base es requerido");
        }

        if (tipoServicio.getCostoBase().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo base no puede ser negativo");
        }

        // Validar que no exista otro tipo de servicio con el mismo nombre
        if (tipoServicio.getIdTipoServicio() == null) {
            // Es nuevo
            if (tipoServicioRepository.existsByNombreServicio(tipoServicio.getNombreServicio())) {
                throw new IllegalArgumentException("Ya existe un tipo de servicio con el nombre: " + tipoServicio.getNombreServicio());
            }
        } else {
            // Es actualización
            Optional<TipoServicio> existente = tipoServicioRepository.findByNombreServicio(tipoServicio.getNombreServicio());
            if (existente.isPresent() && !existente.get().getIdTipoServicio().equals(tipoServicio.getIdTipoServicio())) {
                throw new IllegalArgumentException("Ya existe otro tipo de servicio con el nombre: " + tipoServicio.getNombreServicio());
            }
        }
    }

    public List<TipoServicio> obtenerTodos() {
        return tipoServicioRepository.findAll();
    }

    public List<TipoServicio> obtenerActivos() {
        return tipoServicioRepository.findByActivoTrue();
    }

    public Optional<TipoServicio> obtenerPorId(Integer id) {
        return tipoServicioRepository.findById(id);
    }

    public Optional<TipoServicio> obtenerPorNombre(String nombreServicio) {
        return tipoServicioRepository.findByNombreServicio(nombreServicio);
    }

    public TipoServicio guardar(TipoServicio tipoServicio) {
        validarTipoServicio(tipoServicio);
        return tipoServicioRepository.save(tipoServicio);
    }

    public TipoServicio actualizar(TipoServicio tipoServicio) {
        if (tipoServicio.getIdTipoServicio() == null) {
            throw new IllegalArgumentException("El ID del tipo de servicio es requerido para actualizar");
        }

        if (!tipoServicioRepository.existsById(tipoServicio.getIdTipoServicio())) {
            throw new IllegalArgumentException("Tipo de servicio no encontrado con id: " + tipoServicio.getIdTipoServicio());
        }

        validarTipoServicio(tipoServicio);
        return tipoServicioRepository.save(tipoServicio);
    }

    public void eliminar(Integer id) {
        if (!tipoServicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de servicio no encontrado con id: " + id);
        }
        tipoServicioRepository.deleteById(id);
    }

    public TipoServicio activar(Integer id) {
        return tipoServicioRepository.findById(id)
                .map(tipoServicio -> {
                    tipoServicio.setActivo(true);
                    return tipoServicioRepository.save(tipoServicio);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tipo de servicio no encontrado con id: " + id));
    }

    public TipoServicio desactivar(Integer id) {
        return tipoServicioRepository.findById(id)
                .map(tipoServicio -> {
                    tipoServicio.setActivo(false);
                    return tipoServicioRepository.save(tipoServicio);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tipo de servicio no encontrado con id: " + id));
    }
}
