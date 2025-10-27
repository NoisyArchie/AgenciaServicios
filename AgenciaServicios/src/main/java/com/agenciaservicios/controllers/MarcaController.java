package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Marca;
import com.agenciaservicios.services.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")

public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> obtenerTodos(){
        return ResponseEntity.ok(marcaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorID(@PathVariable Integer id){
        return marcaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Marca marca){
        try{
            Marca nueva = marcaService.crear(marca);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Marca marca){
        try{
            marca.setIdMarca(id);
            Marca actualizado = marcaService.actualizar(marca);
            return ResponseEntity.ok(actualizado);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        marcaService.eliminar(id);
        return ResponseEntity.ok(Map.of("message", "Marca eliminada exitosamente"));
    }
}
