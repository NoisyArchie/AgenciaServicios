package com.agenciaservicios.services;

import com.agenciaservicios.models.Servicio;
import com.agenciaservicios.models.Vehiculo;
import com.agenciaservicios.repositories.ServicioRepository;
import com.agenciaservicios.repositories.VehiculoRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private VehiculoRepository vehiculoRepository;


    @Transactional
    public Servicio crearServicio(Servicio servicio) {
        // 1. Guardar el servicio
        Servicio servicioGuardado = servicioRepository.save(servicio);

        // 2. ⚠️ ACTUALIZAR EL KILOMETRAJE DEL VEHÍCULO
        if (servicio.getVehiculo() != null && servicio.getKilometrajeActual() != null) {
            // Obtener el vehículo actual de la BD
            Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(servicio.getVehiculo().getIdVehiculo());

            if (vehiculoOpt.isPresent()) {
                Vehiculo vehiculo = vehiculoOpt.get();

                // Actualizar solo si el nuevo kilometraje es mayor (para evitar sobreescribir con valores viejos)
                if (vehiculo.getKilometraje() == null ||
                        servicio.getKilometrajeActual() > vehiculo.getKilometraje()) {
                    vehiculo.setKilometraje(servicio.getKilometrajeActual());
                    vehiculoRepository.save(vehiculo);
                }
            }
        }

        return servicioGuardado;
    }

    @Transactional
    public Servicio actualizarServicio(int folioServicio, Servicio servicioActualizado) {
        return servicioRepository.findById(folioServicio).map(servicio -> {
            servicio.setEstatus(servicioActualizado.getEstatus());
            servicio.setFechaEstimadaEntrega(servicioActualizado.getFechaEstimadaEntrega());
            servicio.setFechaEntregaReal(servicioActualizado.getFechaEntregaReal());
            servicio.setObservaciones(servicioActualizado.getObservaciones());
            servicio.setCostoTotal(servicioActualizado.getCostoTotal());

            // ✅ Si se actualiza el kilometraje, también actualizar el vehículo
            if (servicioActualizado.getKilometrajeActual() != null) {
                servicio.setKilometrajeActual(servicioActualizado.getKilometrajeActual());

                // Actualizar el vehículo
                Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(servicio.getVehiculo().getIdVehiculo());
                if (vehiculoOpt.isPresent()) {
                    Vehiculo vehiculo = vehiculoOpt.get();
                    if (vehiculo.getKilometraje() == null ||
                            servicioActualizado.getKilometrajeActual() > vehiculo.getKilometraje()) {
                        vehiculo.setKilometraje(servicioActualizado.getKilometrajeActual());
                        vehiculoRepository.save(vehiculo);
                    }
                }
            }

            return servicioRepository.save(servicio);
        }).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

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

    // ✅ MÉTODO CORREGIDO - Ahora también actualiza el kilometraje del vehículo
    @Transactional
    public Servicio guardar(Servicio servicio) {
        validarServicio(servicio);

        // 1. Guardar el servicio
        Servicio servicioGuardado = servicioRepository.save(servicio);

        // 2. ⚠️ ACTUALIZAR EL KILOMETRAJE DEL VEHÍCULO
        if (servicio.getVehiculo() != null && servicio.getKilometrajeActual() != null) {
            Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(servicio.getVehiculo().getIdVehiculo());

            if (vehiculoOpt.isPresent()) {
                Vehiculo vehiculo = vehiculoOpt.get();

                // Actualizar solo si el nuevo kilometraje es mayor
                if (vehiculo.getKilometraje() == null ||
                        servicio.getKilometrajeActual() > vehiculo.getKilometraje()) {
                    vehiculo.setKilometraje(servicio.getKilometrajeActual());
                    vehiculoRepository.save(vehiculo);
                }
            }
        }

        return servicioGuardado;
    }

    // ✅ MÉTODO CORREGIDO - Ahora también actualiza el kilometraje del vehículo
    @Transactional
    public Servicio actualizar(Servicio servicio) {
        if (servicio.getFolioServicio() == null) {
            throw new IllegalArgumentException("El folio del servicio es requerido para actualizar");
        }

        if (!servicioRepository.existsById(servicio.getFolioServicio())) {
            throw new IllegalArgumentException("Servicio no encontrado con folio: " + servicio.getFolioServicio());
        }

        validarServicio(servicio);

        // 1. Guardar el servicio actualizado
        Servicio servicioGuardado = servicioRepository.save(servicio);

        // 2. ⚠️ ACTUALIZAR EL KILOMETRAJE DEL VEHÍCULO si cambió
        if (servicio.getVehiculo() != null && servicio.getKilometrajeActual() != null) {
            Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(servicio.getVehiculo().getIdVehiculo());

            if (vehiculoOpt.isPresent()) {
                Vehiculo vehiculo = vehiculoOpt.get();

                // Actualizar solo si el nuevo kilometraje es mayor
                if (vehiculo.getKilometraje() == null ||
                        servicio.getKilometrajeActual() > vehiculo.getKilometraje()) {
                    vehiculo.setKilometraje(servicio.getKilometrajeActual());
                    vehiculoRepository.save(vehiculo);
                }
            }
        }

        return servicioGuardado;
    }

    public void eliminar(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Servicio no encontrado con folio: " + id);
        }
        servicioRepository.deleteById(id);
    }

    @Transactional
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