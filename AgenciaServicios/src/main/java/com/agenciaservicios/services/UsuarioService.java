package com.agenciaservicios.services;
// UsuarioService.java

import com.agenciaservicios.models.Usuario;
import com.agenciaservicios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        usuario.setPassword(null);
        return usuario;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAllByActivo(true);
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarUsuario(usuario);

        // Verificar si el username ya existe (excepto para el usuario actual)
        if (!usuarioExistente.getUsername().equals(usuario.getUsername())
                && usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> buscarUsuarios(String query) {
        return usuarioRepository.findByNombreCompletoContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new RuntimeException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new RuntimeException("El rol es obligatorio");
        }
        // Validar que el rol sea válido
        if (!usuario.getRol().equals("admin") && !usuario.getRol().equals("trabajador") && !usuario.getRol().equals("cliente")) {
            throw new RuntimeException("El rol debe ser admin, trabajador o cliente");
        }
    }
}
