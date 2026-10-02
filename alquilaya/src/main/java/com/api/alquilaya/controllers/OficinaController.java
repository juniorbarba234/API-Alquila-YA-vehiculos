package com.api.alquilaya.controllers;

import com.api.alquilaya.models.OficinaModel;
import com.api.alquilaya.services.OficinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaController {

    private final OficinaService oficinaService;

    public OficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public List<OficinaModel> listar() {
        return oficinaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OficinaModel> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(oficinaService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody OficinaModel oficina) {
        try {
            return ResponseEntity.ok(oficinaService.crear(oficina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody OficinaModel oficina) {
        try {
            return ResponseEntity.ok(oficinaService.actualizar(id, oficina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            oficinaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}