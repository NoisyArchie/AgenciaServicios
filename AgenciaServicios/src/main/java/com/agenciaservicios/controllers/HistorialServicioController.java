package com.agenciaservicios.controllers;

import com.agenciaservicios.models.HistorialServicio;
import com.agenciaservicios.services.HistorialServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/historial-servicios")
@CrossOrigin(origins = "*")
public class HistorialServicioController {

    @Autowired
    private HistorialServicioService historialServicioService;

    @GetMapping
    public ResponseEntity<List<HistorialServicio>> obtenerTodos() {
        return ResponseEntity.ok(historialServicioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialServicio> obtenerPorId(@PathVariable Integer id) {
        return historialServicioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/servicio/{folioServicio}")
    public ResponseEntity<List<HistorialServicio>> obtenerPorServicio(@PathVariable Integer folioServicio) {
        return ResponseEntity.ok(historialServicioService.obtenerPorServicio(folioServicio));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<HistorialServicio>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(historialServicioService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/accion/{accion}")
    public ResponseEntity<List<HistorialServicio>> obtenerPorAccion(@PathVariable String accion) {
        return ResponseEntity.ok(historialServicioService.obtenerPorAccion(accion));
    }

    @GetMapping("/estatus/{estatusNuevo}")
    public ResponseEntity<List<HistorialServicio>> obtenerPorEstatusNuevo(@PathVariable String estatusNuevo) {
        return ResponseEntity.ok(historialServicioService.obtenerPorEstatusNuevo(estatusNuevo));
    }

    @GetMapping("/ultimos")
    public ResponseEntity<List<HistorialServicio>> obtenerUltimos10() {
        return ResponseEntity.ok(historialServicioService.obtenerUltimos10());
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<HistorialServicio>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(historialServicioService.obtenerPorFechas(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody HistorialServicio historialServicio) {
        try {
            HistorialServicio nuevoHistorial = historialServicioService.guardar(historialServicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHistorial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Normalmente el historial NO se debe eliminar, pero incluyo el endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            historialServicioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
