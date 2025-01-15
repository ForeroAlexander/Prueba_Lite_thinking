package com.forero.backend.controller;

import com.forero.backend.dto.OrdenDTO;
import com.forero.backend.model.Orden;
import com.forero.backend.service.OrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<List<Orden>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.obtenerTodasLasOrdenes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<Orden> obtenerOrden(@PathVariable UUID id) {
        return ResponseEntity.ok(ordenService.obtenerOrdenPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<List<Orden>> listarOrdenesPorCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<Orden> crearOrden(@Valid @RequestBody OrdenDTO ordenDTO) {
        return ResponseEntity.ok(ordenService.crearOrden(ordenDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarOrden(@PathVariable UUID id) {
        ordenService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}