package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Modelo;
import com.agenciaservicios.services.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modelos")
@CrossOrigin(origins = "*")

public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    /**
     * Get /api/modelos
     * Listar todos los modelos con su marca
     */
    @GetMapping
    public ResponseEntity<List<Modelo>> obtenerTodos(){
        return ResponseEntity.ok(modeloService.obtenerTodos());
    }

    /**
     * GET /api/modelos/{id}
     * Obtener un modelo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return modeloService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/modelos/marca/{id}
     */
    @GetMapping("/marca/{idMarca}")
    public ResponseEntity<List<Modelo>> obtenerPorMarca(@PathVariable Integer idMarca){
        return ResponseEntity.ok(modeloService.obtenerPorMarca(idMarca));
    }

    /**
     * POST /api/modelos
     * Crear un nuevo modelo
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Modelo modelo){
        try{
            Modelo nuevo = modeloService.crear(modelo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Modelo modelo){
        try{
            modelo.setIdModelo(id);
            Modelo actualizado = modeloService.actualizar(modelo);
            return ResponseEntity.ok(actualizado);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    /**
     * DELETE /api/modelos/{id}
     * Eliminar un modelo (borrado lógico)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        modeloService.eliminar(id);
        return ResponseEntity.ok(Map.of("message","Modelo eliminado exitosamente"));
    }


}
