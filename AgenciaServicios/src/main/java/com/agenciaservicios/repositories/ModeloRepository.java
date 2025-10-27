package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Marca;
import com.agenciaservicios.models.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {

    //Obtener solo modelos activos
    List<Modelo> findByActivoTrue();

    //Obtener modelos por marca(usando objeto Marca)
    List<Modelo> findByMarcaAndActivoTrue(Marca marca);

    //Obtener modelos por ID de marca
    List<Modelo> findByMarca_IdMarcaAndActivoTrue(Integer idMarca);

    //Obtener todos los modelos con su marca asociada (JOIN FETCH)
    @Query("SELECT m FROM Modelo m JOIN FETCH m.marca WHERE m.activo = true")
    List<Modelo> findAllWithMarca();
}
