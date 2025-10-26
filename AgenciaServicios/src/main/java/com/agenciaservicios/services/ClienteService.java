package com.agenciaservicios.services;

import com.agenciaservicios.models.Cliente;
import com.agenciaservicios.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Logica de negocio
@Service
public class ClienteService {

    //Autowiring crea el objeto automaticamente y lo conecta
    @Autowired
    private ClienteRepository clienteRepository;

    //Transactional espera que la operacion se realice completamente o no se realice
    @Transactional
    public Cliente crear(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente>obtenerPorId(Integer idCliente){
        return clienteRepository.findById(idCliente);
    }

    public List<Cliente> obtenerTodos(){
        return clienteRepository.findByActivoTrue();
    }

    @Transactional
    public Cliente actualizar(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }
    @Transactional
    public void eliminar(Integer idCliente) {
        clienteRepository.findById(idCliente).ifPresent(cliente -> {
            cliente.setActivo(false);
            clienteRepository.save(cliente);
        });
    }

    public List<Cliente> buscar(String criterio){
        return clienteRepository.buscarPorCriterio(criterio);
    }

    private void validarCliente(Cliente cliente){
        if(cliente.getNombreCompleto()==null || cliente.getNombreCompleto().isBlank()){
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }
        if(cliente.getNombreCompleto().length()<3){
            throw new IllegalArgumentException("El nombre completo debe tener al menos 3 caracteres");
        }
        if(cliente.getEmail()==null || cliente.getEmail().isBlank()){
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if(cliente.getTelefono()==null || cliente.getTelefono().isBlank()){
            throw new IllegalArgumentException("El telefono es obligatorio");
        }
    }
}
