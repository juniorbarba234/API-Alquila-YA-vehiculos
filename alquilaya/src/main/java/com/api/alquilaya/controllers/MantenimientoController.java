package com.api.alquilaya.controllers;

import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.services.MantenimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(
            MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    public List<MantenimientoModel> listar() {
        return mantenimientoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoModel> buscar(
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    mantenimientoService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody MantenimientoModel mantenimiento) {
        try {
            return ResponseEntity.ok(
                    mantenimientoService.crear(mantenimiento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody MantenimientoModel mantenimiento) {
        try {
            return ResponseEntity.ok(
                    mantenimientoService.actualizar(id, mantenimiento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            mantenimientoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}