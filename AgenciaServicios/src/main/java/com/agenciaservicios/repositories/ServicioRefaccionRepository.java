package com.agenciaservicios.repositories;

import com.agenciaservicios.models.ServicioRefaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServicioRefaccionRepository extends JpaRepository<ServicioRefaccion, Integer> {

    List<ServicioRefaccion> findByServicioFolioServicio(Integer folioServicio);

    List<ServicioRefaccion> findByRefaccionIdRefaccion(Integer idRefaccion);

    @Query("SELECT SUM(sr.cantidad * sr.precioAplicado) FROM ServicioRefaccion sr WHERE sr.servicio.folioServicio = ?1")
    BigDecimal calcularTotalRefaccionesPorServicio(Integer folioServicio);

    void deleteByServicioFolioServicio(Integer folioServicio);
}
