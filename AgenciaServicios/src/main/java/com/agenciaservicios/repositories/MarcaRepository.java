package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    List<Marca> findByActivoTrue();
    Optional<Marca> findByNombreMarca(String nombre);
    boolean existsByNombreMarca(String nombre);

}
