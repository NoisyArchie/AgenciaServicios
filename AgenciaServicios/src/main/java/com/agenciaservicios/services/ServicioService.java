package com.agenciaservicios.services;

import com.agenciaservicios.models.Servicio;
import com.agenciaservicios.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    private void validarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }

        if (servicio.getVehiculo() == null || servicio.getVehiculo().getIdVehiculo() == null) {
            throw new IllegalArgumentException("El vehículo es requerido");
        }

        if (servicio.getUsuario() == null || servicio.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (servicio.getKilometrajeActual() != null && servicio.getKilometrajeActual() < 0) {
            throw new IllegalArgumentException("El kilometraje actual no puede ser negativo");
        }

        if (servicio.getProximoServicioKm() != null && servicio.getProximoServicioKm() < 0) {
            throw new IllegalArgumentException("El próximo servicio en km no puede ser negativo");
        }

        if (servicio.getCostoTotal() != null && servicio.getCostoTotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo total no puede ser negativo");
        }

        // Validar estatus
        if (servicio.getEstatus() != null) {
            String estatus = servicio.getEstatus().toUpperCase();
            if (!estatus.equals("PENDIENTE") && !estatus.equals("EN_PROCESO") &&
                    !estatus.equals("COMPLETADO") && !estatus.equals("ENTREGADO") &&
                    !estatus.equals("CANCELADO")) {
                throw new IllegalArgumentException("Estatus inválido. Debe ser: PENDIENTE, EN_PROCESO, COMPLETADO, ENTREGADO o CANCELADO");
            }
            servicio.setEstatus(estatus);
        }
    }

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> obtenerPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> obtenerPorVehiculo(Integer idVehiculo) {
        return servicioRepository.findByVehiculoIdVehiculo(idVehiculo);
    }

    public List<Servicio> obtenerPorUsuario(Integer idUsuario) {
        return servicioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Servicio> obtenerPorCliente(Integer idCliente) {
        return servicioRepository.findByVehiculoClienteIdCliente(idCliente);
    }

    public List<Servicio> obtenerPorEstatus(String estatus) {
        return servicioRepository.findByEstatus(estatus.toUpperCase());
    }

    public List<Servicio> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return servicioRepository.findByFechaIngresoBetween(inicio, fin);
    }

    public List<Servicio> obtenerVencidos() {
        return servicioRepository.findByFechaEstimadaEntregaBefore(LocalDate.now());
    }

    public List<Servicio> obtenerServiciosVencidosPendientes() {
        return servicioRepository.findByEstatusAndFechaEstimadaEntregaBefore("PENDIENTE", LocalDate.now());
    }

    public Servicio guardar(Servicio servicio) {
        validarServicio(servicio);
        return servicioRepository.save(servicio);
    }

    public Servicio actualizar(Servicio servicio) {
        if (servicio.getFolioServicio() == null) {
            throw new IllegalArgumentException("El folio del servicio es requerido para actualizar");
        }

        if (!servicioRepository.existsById(servicio.getFolioServicio())) {
            throw new IllegalArgumentException("Servicio no encontrado con folio: " + servicio.getFolioServicio());
        }

        validarServicio(servicio);
        return servicioRepository.save(servicio);
    }

    public void eliminar(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Servicio no encontrado con folio: " + id);
        }
        servicioRepository.deleteById(id);
    }

    public Servicio cambiarEstatus(Integer id, String nuevoEstatus) {
        return servicioRepository.findById(id)
                .map(servicio -> {
                    servicio.setEstatus(nuevoEstatus.toUpperCase());

                    // Si se marca como ENTREGADO, registrar fecha de entrega
                    if (nuevoEstatus.equalsIgnoreCase("ENTREGADO") && servicio.getFechaEntregaReal() == null) {
                        servicio.setFechaEntregaReal(LocalDateTime.now());
                    }

                    return servicioRepository.save(servicio);
                })
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado con folio: " + id));
    }

    public Servicio iniciarServicio(Integer id) {
        return cambiarEstatus(id, "EN_PROCESO");
    }

    public Servicio completarServicio(Integer id) {
        return cambiarEstatus(id, "COMPLETADO");
    }

    public Servicio entregarServicio(Integer id) {
        return cambiarEstatus(id, "ENTREGADO");
    }

    public Servicio cancelarServicio(Integer id) {
        return cambiarEstatus(id, "CANCELADO");
    }
}
