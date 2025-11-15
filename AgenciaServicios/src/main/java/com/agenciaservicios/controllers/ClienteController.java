package com.agenciaservicios.controllers;

import com.agenciaservicios.models.Cliente;
import com.agenciaservicios.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")

public class ClienteController {

    //autowired crea el objeto automaticamente y lo conecta
    @Autowired
    private ClienteService clienteService;

    // GetMapping para obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> ObtenerTodos(){
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    // GetMapping para obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?>obtenerPorID(@PathVariable Integer id){
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PostMapping para crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cliente cliente){
        try{
            Cliente nuevo = clienteService.crear(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);

        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    // PutMapping para actualizar un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Cliente cliente){
        try{
            cliente.setIdCliente(id);
            Cliente actualizado=clienteService.actualizar(cliente);
            return ResponseEntity.ok(actualizado);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    // DeleteMapping para eliminar un cliente (borrado lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        clienteService.eliminar(id);
        return ResponseEntity.ok(Map.of("message","Cliente eliminado exitosamente"));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscar(@RequestParam String termino) {
        List<Cliente> resultados = clienteService.buscar(termino);
        return ResponseEntity.ok(resultados);
    }
}
