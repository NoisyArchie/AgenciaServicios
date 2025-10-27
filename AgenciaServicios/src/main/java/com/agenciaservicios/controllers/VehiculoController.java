package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Vehiculo;
import com.agenciaservicios.repositories.VehiculoRepository;
import com.agenciaservicios.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")

public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    /**
     * GET /api/vehiculos
     * Listar todos los vehiculos
     */
    @GetMapping
    public ResponseEntity<List<Vehiculo>> obtenerTodos(){
        return ResponseEntity.ok(vehiculoService.buscarTodos());
    }

    /**
     * GET /api/vehiculos/{id}
     * Obtener vehiculo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return vehiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/vehiculos/serie/{numeroSerie}
     * Obtener vehiculo por numero de serie
     */
    @GetMapping("/serie/{numeroSerie}")
    public ResponseEntity<?> obtenerPorSerie(@PathVariable String numeroSerie) {
        return vehiculoService.buscarPorSerie(numeroSerie)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/vehiculos/placas/{placas}
     * Obtener vehiculo por placas
     */
    @GetMapping("/placas/{placas}")
    public ResponseEntity<Vehiculo> obtenerPorPlacas(@PathVariable String placas) {
        return vehiculoService.buscarPorPlacas(placas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/vehiculos/cliente/{idCliente}
     * Obtener vehiculos por cliente
     */
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Vehiculo>> obtenerPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(vehiculoService.buscarPorCliente(idCliente));
    }

    /**
     * GET /api/vehiculos/modelo/{idModelo}
     * Obtener vehiculos por modelo
     */
    @GetMapping("/modelo/{idModelo}")
    public ResponseEntity<List<Vehiculo>> obtenerPorModelo(@PathVariable Integer idModelo) {
        return ResponseEntity.ok(vehiculoService.buscarPorModelo(idModelo));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Vehiculo vehiculo){
        try{
            Vehiculo nuevo = vehiculoService.crear(vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Vehiculo vehiculo){
        try {
            vehiculo.setIdVehiculo(id);
            Vehiculo actualizado = vehiculoService.actualizar(vehiculo);
            return ResponseEntity.ok(actualizado);
        }catch(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.ok().body("Vehículo eliminado exitosamente");
    }
}
