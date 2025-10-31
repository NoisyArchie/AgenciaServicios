package com.agenciaservicios.services;

import com.agenciaservicios.models.HistorialServicio;
import com.agenciaservicios.models.Servicio;
import com.agenciaservicios.models.Usuario;
import com.agenciaservicios.repositories.HistorialServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialServicioService {

    @Autowired
    private HistorialServicioRepository historialServicioRepository;

    private void validarHistorialServicio(HistorialServicio historialServicio) {
        if (historialServicio == null) {
            throw new IllegalArgumentException("El historial no puede ser nulo");
        }

        if (historialServicio.getServicio() == null || historialServicio.getServicio().getFolioServicio() == null) {
            throw new IllegalArgumentException("El servicio es requerido");
        }

        if (historialServicio.getUsuario() == null || historialServicio.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (historialServicio.getAccion() == null || historialServicio.getAccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La acción es requerida");
        }
    }

    public List<HistorialServicio> obtenerTodos() {
        return historialServicioRepository.findAll();
    }

    public Optional<HistorialServicio> obtenerPorId(Integer id) {
        return historialServicioRepository.findById(id);
    }

    public List<HistorialServicio> obtenerPorServicio(Integer folioServicio) {
        return historialServicioRepository.findByServicioFolioServicioOrderByFechaAccionDesc(folioServicio);
    }

    public List<HistorialServicio> obtenerPorUsuario(Integer idUsuario) {
        return historialServicioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<HistorialServicio> obtenerPorAccion(String accion) {
        return historialServicioRepository.findByAccion(accion);
    }

    public List<HistorialServicio> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return historialServicioRepository.findByFechaAccionBetween(inicio, fin);
    }

    public List<HistorialServicio> obtenerPorEstatusNuevo(String estatusNuevo) {
        return historialServicioRepository.findByEstatusNuevo(estatusNuevo);
    }

    public List<HistorialServicio> obtenerUltimos10() {
        return historialServicioRepository.findTop10ByOrderByFechaAccionDesc();
    }

    public HistorialServicio guardar(HistorialServicio historialServicio) {
        validarHistorialServicio(historialServicio);
        return historialServicioRepository.save(historialServicio);
    }

    // Métodos auxiliares para registrar acciones comunes
    public HistorialServicio registrarCreacion(Servicio servicio, Usuario usuario) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion("CREACION")
                .estatusNuevo(servicio.getEstatus())
                .detalles("Servicio creado")
                .build());
    }

    public HistorialServicio registrarCambioEstatus(Servicio servicio, Usuario usuario,
                                                    String estatusAnterior, String estatusNuevo) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion("CAMBIO_ESTATUS")
                .estatusAnterior(estatusAnterior)
                .estatusNuevo(estatusNuevo)
                .detalles("Cambio de estatus de " + estatusAnterior + " a " + estatusNuevo)
                .build());
    }

    public HistorialServicio registrarModificacion(Servicio servicio, Usuario usuario, String detalles) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion("MODIFICACION")
                .estatusNuevo(servicio.getEstatus())
                .detalles(detalles)
                .build());
    }

    public HistorialServicio registrarCancelacion(Servicio servicio, Usuario usuario, String motivo) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion("CANCELACION")
                .estatusAnterior(servicio.getEstatus())
                .estatusNuevo("CANCELADO")
                .detalles("Servicio cancelado. Motivo: " + motivo)
                .build());
    }

    public HistorialServicio registrarEntrega(Servicio servicio, Usuario usuario) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion("ENTREGA")
                .estatusAnterior(servicio.getEstatus())
                .estatusNuevo("ENTREGADO")
                .detalles("Vehículo entregado a: " + servicio.getQuienEntrego())
                .build());
    }

    public HistorialServicio registrarAccion(Servicio servicio, Usuario usuario, String accion, String detalles) {
        return guardar(HistorialServicio.builder()
                .servicio(servicio)
                .usuario(usuario)
                .accion(accion)
                .estatusNuevo(servicio.getEstatus())
                .detalles(detalles)
                .build());
    }

    // El historial no se debe eliminar (auditoría), pero incluyo el método por completitud
    public void eliminar(Integer id) {
        if (!historialServicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Historial no encontrado con id: " + id);
        }
        historialServicioRepository.deleteById(id);
    }
}
