package com.agenciaservicios.services;


import com.agenciaservicios.models.Vehiculo;
import com.agenciaservicios.repositories.ClienteRepository;
import com.agenciaservicios.repositories.ModeloRepository;
import com.agenciaservicios.repositories.UsuarioRepository;
import com.agenciaservicios.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    public Vehiculo crear(Vehiculo vehiculo){

        validarVehiculo(vehiculo);
        return vehiculoRepository.save(vehiculo);
    }

    public Optional<Vehiculo> buscarPorId(Integer id){
        return vehiculoRepository.findById(id);
    }

    public List<Vehiculo> buscarTodos(){
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> buscarPorSerie(String numeroSerie){
        return vehiculoRepository.findByNumeroSerie(numeroSerie);
    }

    public Optional<Vehiculo> buscarPorPlacas(String placas){
        return vehiculoRepository.findByPlacas(placas);
    }

    public List<Vehiculo> buscarPorCliente(Integer idCliente){
        return vehiculoRepository.findByClienteIdCliente(idCliente);
    }

    public List<Vehiculo> buscarPorModelo(Integer idModelo){
        return vehiculoRepository.findByModeloIdModelo(idModelo);
    }

    public Vehiculo actualizar(Vehiculo vehiculo){
        validarVehiculo(vehiculo);
        return vehiculoRepository.save(vehiculo);
    }

    public void eliminar(Integer idVehiculo){
        if (!vehiculoRepository.existsById(idVehiculo)) {
            throw new IllegalArgumentException("Vehículo no encontrado con id: " + idVehiculo);
        }
        vehiculoRepository.deleteById(idVehiculo);
    }




    private void validarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }

        if (vehiculo.getCliente() == null || vehiculo.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("El cliente es requerido");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().getIdModelo() == null) {
            throw new IllegalArgumentException("El modelo es requerido");
        }

        if (vehiculo.getAnio() == null) {
            throw new IllegalArgumentException("El año es requerido");
        }

        if (vehiculo.getAnio() < 1900 || vehiculo.getAnio() > 2030) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2030");
        }

        if (vehiculo.getNumeroSerie() == null || vehiculo.getNumeroSerie().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de serie es requerido");
        }

        if (vehiculo.getColor() == null || vehiculo.getColor().trim().isEmpty()) {
            throw new IllegalArgumentException("El color es requerido");
        }

        if (vehiculo.getKilometraje() == null) {
            throw new IllegalArgumentException("El kilometraje es requerido");
        }

        if (vehiculo.getKilometraje() < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }

        // Validar que no exista otro vehículo con el mismo número de serie
        if (vehiculo.getIdVehiculo() == null) {
            // Es un nuevo vehículo
            if (vehiculoRepository.existsByNumeroSerie(vehiculo.getNumeroSerie())) {
                throw new IllegalArgumentException("Ya existe un vehículo con el número de serie: " + vehiculo.getNumeroSerie());
            }

            if (vehiculo.getPlacas() != null && !vehiculo.getPlacas().trim().isEmpty()
                    && vehiculoRepository.existsByPlacas(vehiculo.getPlacas())) {
                throw new IllegalArgumentException("Ya existe un vehículo con las placas: " + vehiculo.getPlacas());
            }
        } else {
            // Es una actualización
            Optional<Vehiculo> vehiculoExistente = vehiculoRepository.findByNumeroSerie(vehiculo.getNumeroSerie());
            if (vehiculoExistente.isPresent() && !vehiculoExistente.get().getIdVehiculo().equals(vehiculo.getIdVehiculo())) {
                throw new IllegalArgumentException("Ya existe otro vehículo con el número de serie: " + vehiculo.getNumeroSerie());
            }

            if (vehiculo.getPlacas() != null && !vehiculo.getPlacas().trim().isEmpty()) {
                Optional<Vehiculo> vehiculoConPlacas = vehiculoRepository.findByPlacas(vehiculo.getPlacas());
                if (vehiculoConPlacas.isPresent() && !vehiculoConPlacas.get().getIdVehiculo().equals(vehiculo.getIdVehiculo())) {
                    throw new IllegalArgumentException("Ya existe otro vehículo con las placas: " + vehiculo.getPlacas());
                }
            }
        }
    }
}
