package com.agenciaservicios.repositories;

import com.agenciaservicios.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    List<Usuario> findAllByActivo(Boolean activo);
    boolean existsByUsername(String username);

}
