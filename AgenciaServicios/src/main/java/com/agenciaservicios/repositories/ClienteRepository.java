package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByActivoTrue();
    List<Cliente> findByNombreCompletoContainingIgnoreCaseAndActivoTrue(String nombre);

    Optional<Cliente>  findByTelefono(String telefono);
    Optional<Cliente> findByEmail(String email);

    @Query("SELECT c FROM Cliente c WHERE " +
            "(LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :criterio, '%')) " +
            "OR c.telefono LIKE CONCAT('%', :criterio, '%') " +
            "OR c.email LIKE CONCAT('%', :criterio, '%')) " +
            "AND c.activo = true")
    List<Cliente> buscarPorCriterio(@Param("criterio") String criterio);

    List<Cliente> findByNombreCompletoContainingIgnoreCaseOrTelefonoContainingOrEmailContainingIgnoreCase(
            String nombreCompleto, String telefono, String email);

    // Para vincular clientes con usuarios
    @Query("SELECT c FROM Cliente c WHERE c.usuario.idUsuario = :usuarioId")
    Optional<Cliente> findByUsuarioIdUsuario(@Param("usuarioId") Integer usuarioId);
    //Optional<Cliente> findByUsuarioIdUsuario(Integer idUsuario);


}
