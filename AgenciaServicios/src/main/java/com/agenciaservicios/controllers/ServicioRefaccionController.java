package com.agenciaservicios.controllers;

import com.agenciaservicios.models.ServicioRefaccion;
import com.agenciaservicios.services.ServicioRefaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/servicio-refacciones")
@CrossOrigin(origins = "*")
public class ServicioRefaccionController {

    @Autowired
    private ServicioRefaccionService servicioRefaccionService;

    @GetMapping
    public ResponseEntity<List<ServicioRefaccion>> obtenerTodos() {
        return ResponseEntity.ok(servicioRefaccionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioRefaccion> obtenerPorId(@PathVariable Integer id) {
        return servicioRefaccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/servicio/{folioServicio}")
    public ResponseEntity<List<ServicioRefaccion>> obtenerPorServicio(@PathVariable Integer folioServicio) {
        return ResponseEntity.ok(servicioRefaccionService.obtenerPorServicio(folioServicio));
    }

    @GetMapping("/refaccion/{idRefaccion}")
    public ResponseEntity<List<ServicioRefaccion>> obtenerPorRefaccion(@PathVariable Integer idRefaccion) {
        return ResponseEntity.ok(servicioRefaccionService.obtenerPorRefaccion(idRefaccion));
    }

    @GetMapping("/servicio/{folioServicio}/total")
    public ResponseEntity<BigDecimal> calcularTotalRefacciones(@PathVariable Integer folioServicio) {
        return ResponseEntity.ok(servicioRefaccionService.calcularTotalRefacciones(folioServicio));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ServicioRefaccion servicioRefaccion) {
        try {
            ServicioRefaccion nuevaServicioRefaccion = servicioRefaccionService.guardar(servicioRefaccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaServicioRefaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ServicioRefaccion servicioRefaccion) {
        try {
            servicioRefaccion.setIdServicioRefaccion(id);
            ServicioRefaccion servicioRefaccionActualizada = servicioRefaccionService.actualizar(servicioRefaccion);
            return ResponseEntity.ok(servicioRefaccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            servicioRefaccionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/servicio/{folioServicio}")
    public ResponseEntity<?> eliminarPorServicio(@PathVariable Integer folioServicio) {
        servicioRefaccionService.eliminarPorServicio(folioServicio);
        return ResponseEntity.noContent().build();
    }
}
