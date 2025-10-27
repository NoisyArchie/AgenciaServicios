package com.agenciaservicios.services;

import com.agenciaservicios.models.Marca;
import com.agenciaservicios.repositories.MarcaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional
    public Marca crear(Marca marca){
        if(marcaRepository.existsByNombreMarca(marca.getNombreMarca())){
            throw new IllegalArgumentException("La marca ya existe");
        }
        return marcaRepository.save(marca);
    }

    public Optional<Marca> obtenerPorId(Integer idMarca){
        return marcaRepository.findById(idMarca);
    }
    public List<Marca> obtenerTodos(){
        return marcaRepository.findByActivoTrue();
    }

    @Transactional
    public Marca actualizar(Marca marca){
        return marcaRepository.save(marca);
    }

    @Transactional
    public void eliminar(Integer idMarca){
        marcaRepository.findById(idMarca).ifPresent(marca -> {
            marca.setActivo(false);
            marcaRepository.save(marca);
        });
    }
}
