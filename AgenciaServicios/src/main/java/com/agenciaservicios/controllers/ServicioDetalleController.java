package com.agenciaservicios.controllers;

import com.agenciaservicios.models.ServicioDetalle;
import com.agenciaservicios.services.ServicioDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/servicio-detalle")
@CrossOrigin(origins = "*")
public class ServicioDetalleController {

    @Autowired
    private ServicioDetalleService servicioDetalleService;

    @GetMapping
    public ResponseEntity<List<ServicioDetalle>> obtenerTodos() {
        return ResponseEntity.ok(servicioDetalleService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDetalle> obtenerPorId(@PathVariable Integer id) {
        return servicioDetalleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/servicio/{folioServicio}")
    public ResponseEntity<List<ServicioDetalle>> obtenerPorServicio(@PathVariable Integer folioServicio) {
        return ResponseEntity.ok(servicioDetalleService.obtenerPorServicio(folioServicio));
    }

    @GetMapping("/tipo-servicio/{idTipoServicio}")
    public ResponseEntity<List<ServicioDetalle>> obtenerPorTipoServicio(@PathVariable Integer idTipoServicio) {
        return ResponseEntity.ok(servicioDetalleService.obtenerPorTipoServicio(idTipoServicio));
    }

    @GetMapping("/servicio/{folioServicio}/total")
    public ResponseEntity<BigDecimal> calcularTotalServicio(@PathVariable Integer folioServicio) {
        return ResponseEntity.ok(servicioDetalleService.calcularTotalServicio(folioServicio));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ServicioDetalle servicioDetalle) {
        try {
            ServicioDetalle nuevoDetalle = servicioDetalleService.guardar(servicioDetalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ServicioDetalle servicioDetalle) {
        try {
            servicioDetalle.setIdServicioDetalle(id);
            ServicioDetalle detalleActualizado = servicioDetalleService.actualizar(servicioDetalle);
            return ResponseEntity.ok(detalleActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            servicioDetalleService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/servicio/{folioServicio}")
    public ResponseEntity<?> eliminarPorServicio(@PathVariable Integer folioServicio) {
        servicioDetalleService.eliminarPorServicio(folioServicio);
        return ResponseEntity.noContent().build();
    }
}