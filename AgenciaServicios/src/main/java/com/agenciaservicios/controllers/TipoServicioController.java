package com.agenciaservicios.controllers;

import com.agenciaservicios.models.TipoServicio;
import com.agenciaservicios.services.TipoServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-servicio")
@CrossOrigin(origins = "*")
public class TipoServicioController {

    @Autowired
    private TipoServicioService tipoServicioService;

    @GetMapping
    public ResponseEntity<List<TipoServicio>> obtenerTodos() {
        return ResponseEntity.ok(tipoServicioService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoServicio>> obtenerActivos() {
        return ResponseEntity.ok(tipoServicioService.obtenerActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> obtenerPorId(@PathVariable Integer id) {
        return tipoServicioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombreServicio}")
    public ResponseEntity<TipoServicio> obtenerPorNombre(@PathVariable String nombreServicio) {
        return tipoServicioService.obtenerPorNombre(nombreServicio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoServicio tipoServicio) {
        try {
            TipoServicio nuevoTipoServicio = tipoServicioService.guardar(tipoServicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TipoServicio tipoServicio) {
        try {
            tipoServicio.setIdTipoServicio(id);
            TipoServicio tipoServicioActualizado = tipoServicioService.actualizar(tipoServicio);
            return ResponseEntity.ok(tipoServicioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            tipoServicioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Integer id) {
        try {
            TipoServicio tipoServicio = tipoServicioService.activar(id);
            return ResponseEntity.ok(tipoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Integer id) {
        try {
            TipoServicio tipoServicio = tipoServicioService.desactivar(id);
            return ResponseEntity.ok(tipoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
