package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    List<Servicio> findByVehiculoIdVehiculo(Integer idVehiculo);

    List<Servicio> findByUsuarioIdUsuario(Integer idUsuario);

    List<Servicio> findByEstatus(String estatus);

    List<Servicio> findByFechaIngresoBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Servicio> findByFechaEstimadaEntregaBefore(LocalDate fecha);

    List<Servicio> findByEstatusAndFechaEstimadaEntregaBefore(String estatus, LocalDate fecha);

    List<Servicio> findByVehiculoClienteIdCliente(Integer idCliente);
}
