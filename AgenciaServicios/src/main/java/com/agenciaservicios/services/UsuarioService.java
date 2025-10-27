package com.agenciaservicios.services;

import com.agenciaservicios.models.Usuario;
import com.agenciaservicios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Login - valida credenciales
    public Optional<Usuario> login(String username, String password) {
        String passwordEncriptada=encriptarPassword(password);
        return usuarioRepository.findByUsernameAndPassword(username, passwordEncriptada).filter(Usuario::getActivo);

    }

    //Obtener todos los usuarios activos
    public List<Usuario> obtenerTodos(){
        return usuarioRepository.findAllByActivo(true);
    }

    //Encriptar password con sha-256
    private String encriptarPassword(String password){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

}
