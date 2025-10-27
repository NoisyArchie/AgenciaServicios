package com.agenciaservicios.services;

import com.agenciaservicios.models.Modelo;
import com.agenciaservicios.repositories.MarcaRepository;
import com.agenciaservicios.repositories.ModeloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional
    public Modelo crear(Modelo modelo){
        validarModelo(modelo);
        return modeloRepository.save(modelo);
    }

    public Optional<Modelo> obtenerPorId(Integer id){
        return modeloRepository.findById(id);
    }

    public List<Modelo> obtenerTodos(){
        return modeloRepository.findAllWithMarca();
    }

    public List<Modelo> obtenerPorMarca(Integer idMarca){
        return modeloRepository.findByMarca_IdMarcaAndActivoTrue(idMarca);
    }

    @Transactional
    public Modelo actualizar(Modelo modelo){
        validarModelo(modelo);
        return modeloRepository.save(modelo);
    }

    @Transactional
    public void eliminar(Integer idModelo){
        modeloRepository.findById(idModelo).ifPresent(modelo -> {
            modelo.setActivo(false);
            modeloRepository.save(modelo);
        });
    }

    private void validarModelo(Modelo modelo){
        if (modelo.getMarca() == null || modelo.getMarca().getIdMarca() == null) {
            throw new IllegalArgumentException("Debe seleccionar una marca");
        }

        if (modelo.getNombreModelo() == null || modelo.getNombreModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del modelo es obligatorio");
        }

        // Verificar que la marca existe
        if (!marcaRepository.existsById(modelo.getMarca().getIdMarca())) {
            throw new IllegalArgumentException("La marca seleccionada no existe");
        }

    }
}
