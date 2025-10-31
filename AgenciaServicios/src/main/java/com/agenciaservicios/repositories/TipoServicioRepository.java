package com.agenciaservicios.repositories;

import com.agenciaservicios.models.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Integer> {

    Optional<TipoServicio> findByNombreServicio(String nombreServicio);

    List<TipoServicio> findByActivoTrue();

    boolean existsByNombreServicio(String nombreServicio);
}
