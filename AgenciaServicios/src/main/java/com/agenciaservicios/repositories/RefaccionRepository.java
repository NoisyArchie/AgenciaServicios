package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Refaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RefaccionRepository extends JpaRepository<Refaccion, Integer> {

    List<Refaccion> findByActivoTrue();

    List<Refaccion> findByNombreRefaccionContainingIgnoreCase(String nombre);

    List<Refaccion> findByStockLessThan(Integer cantidad);

    List<Refaccion> findByActivoTrueAndStockGreaterThan(Integer cantidad);
}
