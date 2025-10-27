package com.agenciaservicios.repositories;


import com.agenciaservicios.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    Optional<Vehiculo> findByPlacas(String placas);

    List<Vehiculo> findByClienteIdCliente(Integer idCliente);

    List<Vehiculo> findByModeloIdModelo(Integer idModelo);

    Optional<Vehiculo> findByNumeroSerie(String numeroSerie);


    boolean existsByPlacas(String placas);
    boolean existsByNumeroSerie(String numeroSerie);
}
