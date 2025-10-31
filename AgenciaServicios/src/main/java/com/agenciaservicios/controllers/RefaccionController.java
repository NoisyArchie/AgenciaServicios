package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Refaccion;
import com.agenciaservicios.services.RefaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refacciones")
@CrossOrigin(origins = "*")
public class RefaccionController {

    @Autowired
    private RefaccionService refaccionService;

    @GetMapping
    public ResponseEntity<List<Refaccion>> obtenerTodas() {
        return ResponseEntity.ok(refaccionService.obtenerTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Refaccion>> obtenerActivas() {
        return ResponseEntity.ok(refaccionService.obtenerActivas());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Refaccion>> obtenerDisponibles() {
        return ResponseEntity.ok(refaccionService.obtenerDisponibles());
    }

    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<Refaccion>> obtenerConStockBajo(@PathVariable Integer cantidad) {
        return ResponseEntity.ok(refaccionService.obtenerConStockBajo(cantidad));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refaccion> obtenerPorId(@PathVariable Integer id) {
        return refaccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Refaccion>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(refaccionService.obtenerPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Refaccion refaccion) {
        try {
            Refaccion nuevaRefaccion = refaccionService.guardar(refaccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRefaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Refaccion refaccion) {
        try {
            refaccion.setIdRefaccion(id);
            Refaccion refaccionActualizada = refaccionService.actualizar(refaccion);
            return ResponseEntity.ok(refaccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            refaccionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Integer id) {
        try {
            Refaccion refaccion = refaccionService.activar(id);
            return ResponseEntity.ok(refaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Integer id) {
        try {
            Refaccion refaccion = refaccionService.desactivar(id);
            return ResponseEntity.ok(refaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/agregar-stock")
    public ResponseEntity<?> agregarStock(@PathVariable Integer id, @RequestBody Map<String, Integer> request) {
        try {
            Integer cantidad = request.get("cantidad");
            if (cantidad == null) {
                return ResponseEntity.badRequest().body("La cantidad es requerida");
            }
            Refaccion refaccion = refaccionService.agregarStock(id, cantidad);
            return ResponseEntity.ok(refaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/reducir-stock")
    public ResponseEntity<?> reducirStock(@PathVariable Integer id, @RequestBody Map<String, Integer> request) {
        try {
            Integer cantidad = request.get("cantidad");
            if (cantidad == null) {
                return ResponseEntity.badRequest().body("La cantidad es requerida");
            }
            Refaccion refaccion = refaccionService.reducirStock(id, cantidad);
            return ResponseEntity.ok(refaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
