package com.agenciaservicios.services;

import com.agenciaservicios.models.ServicioDetalle;
import com.agenciaservicios.repositories.ServicioDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioDetalleService {

    @Autowired
    private ServicioDetalleRepository servicioDetalleRepository;

    private void validarServicioDetalle(ServicioDetalle servicioDetalle) {
        if (servicioDetalle == null) {
            throw new IllegalArgumentException("El detalle del servicio no puede ser nulo");
        }

        if (servicioDetalle.getServicio() == null || servicioDetalle.getServicio().getFolioServicio() == null) {
            throw new IllegalArgumentException("El servicio es requerido");
        }

        if (servicioDetalle.getTipoServicio() == null || servicioDetalle.getTipoServicio().getIdTipoServicio() == null) {
            throw new IllegalArgumentException("El tipo de servicio es requerido");
        }

        if (servicioDetalle.getCosto() == null) {
            throw new IllegalArgumentException("El costo es requerido");
        }

        if (servicioDetalle.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
    }

    public List<ServicioDetalle> obtenerTodos() {
        return servicioDetalleRepository.findAll();
    }

    public Optional<ServicioDetalle> obtenerPorId(Integer id) {
        return servicioDetalleRepository.findById(id);
    }

    public List<ServicioDetalle> obtenerPorServicio(Integer folioServicio) {
        return servicioDetalleRepository.findByServicioFolioServicio(folioServicio);
    }

    public List<ServicioDetalle> obtenerPorTipoServicio(Integer idTipoServicio) {
        return servicioDetalleRepository.findByTipoServicioIdTipoServicio(idTipoServicio);
    }

    public BigDecimal calcularTotalServicio(Integer folioServicio) {
        BigDecimal total = servicioDetalleRepository.calcularTotalPorServicio(folioServicio);
        return total != null ? total : BigDecimal.ZERO;
    }

    public ServicioDetalle guardar(ServicioDetalle servicioDetalle) {
        validarServicioDetalle(servicioDetalle);
        return servicioDetalleRepository.save(servicioDetalle);
    }

    public ServicioDetalle actualizar(ServicioDetalle servicioDetalle) {
        if (servicioDetalle.getIdServicioDetalle() == null) {
            throw new IllegalArgumentException("El ID del detalle es requerido para actualizar");
        }

        if (!servicioDetalleRepository.existsById(servicioDetalle.getIdServicioDetalle())) {
            throw new IllegalArgumentException("Detalle no encontrado con id: " + servicioDetalle.getIdServicioDetalle());
        }

        validarServicioDetalle(servicioDetalle);
        return servicioDetalleRepository.save(servicioDetalle);
    }

    public void eliminar(Integer id) {
        if (!servicioDetalleRepository.existsById(id)) {
            throw new IllegalArgumentException("Detalle no encontrado con id: " + id);
        }
        servicioDetalleRepository.deleteById(id);
    }

    @Transactional
    public void eliminarPorServicio(Integer folioServicio) {
        servicioDetalleRepository.deleteByServicioFolioServicio(folioServicio);
    }
}
