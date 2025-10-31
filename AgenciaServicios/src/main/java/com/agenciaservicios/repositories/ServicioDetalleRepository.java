package com.agenciaservicios.repositories;

import com.agenciaservicios.models.ServicioDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServicioDetalleRepository extends JpaRepository<ServicioDetalle, Integer> {

    List<ServicioDetalle> findByServicioFolioServicio(Integer folioServicio);

    List<ServicioDetalle> findByTipoServicioIdTipoServicio(Integer idTipoServicio);

    @Query("SELECT SUM(sd.costo) FROM ServicioDetalle sd WHERE sd.servicio.folioServicio = ?1")
    BigDecimal calcularTotalPorServicio(Integer folioServicio);

    void deleteByServicioFolioServicio(Integer folioServicio);
}
