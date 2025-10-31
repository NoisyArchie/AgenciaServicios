package com.agenciaservicios.repositories;

import com.agenciaservicios.models.HistorialServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialServicioRepository extends JpaRepository<HistorialServicio, Integer> {

    List<HistorialServicio> findByServicioFolioServicio(Integer folioServicio);

    List<HistorialServicio> findByServicioFolioServicioOrderByFechaAccionDesc(Integer folioServicio);

    List<HistorialServicio> findByUsuarioIdUsuario(Integer idUsuario);

    List<HistorialServicio> findByAccion(String accion);

    List<HistorialServicio> findByFechaAccionBetween(LocalDateTime inicio, LocalDateTime fin);

    List<HistorialServicio> findByEstatusNuevo(String estatusNuevo);

    List<HistorialServicio> findTop10ByOrderByFechaAccionDesc();
}
