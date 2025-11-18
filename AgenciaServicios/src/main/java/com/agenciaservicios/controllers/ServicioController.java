package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Servicio;
import com.agenciaservicios.services.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerTodos() {
        return ResponseEntity.ok(servicioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Integer id) {
        return servicioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehiculo/{idVehiculo}")
    public ResponseEntity<List<Servicio>> obtenerPorVehiculo(@PathVariable Integer idVehiculo) {
        return ResponseEntity.ok(servicioService.obtenerPorVehiculo(idVehiculo));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Servicio>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(servicioService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Servicio>> obtenerPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(servicioService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Servicio>> obtenerPorEstatus(@PathVariable String estatus) {
        return ResponseEntity.ok(servicioService.obtenerPorEstatus(estatus));
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<Servicio>> obtenerVencidos() {
        return ResponseEntity.ok(servicioService.obtenerVencidos());
    }

    @GetMapping("/vencidos-pendientes")
    public ResponseEntity<List<Servicio>> obtenerVencidosPendientes() {
        return ResponseEntity.ok(servicioService.obtenerServiciosVencidosPendientes());
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Servicio>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(servicioService.obtenerPorFechas(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Servicio servicio) {
        try {
            Servicio nuevoServicio = servicioService.guardar(servicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Servicio servicio) {
        try {
            servicio.setFolioServicio(id);
            Servicio servicioActualizado = servicioService.actualizar(servicio);
            return ResponseEntity.ok(servicioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            servicioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estatus")
    public ResponseEntity<?> cambiarEstatus(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String nuevoEstatus = request.get("estatus");
            if (nuevoEstatus == null || nuevoEstatus.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El estatus es requerido");
            }
            Servicio servicio = servicioService.cambiarEstatus(id, nuevoEstatus);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciarServicio(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.iniciarServicio(id);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<?> completarServicio(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.completarServicio(id);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<?> entregarServicio(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.entregarServicio(id);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarServicio(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.cancelarServicio(id);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}