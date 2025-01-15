package com.forero.backend.controller;

import com.forero.backend.dto.EmpresaDTO;
import com.forero.backend.dto.EmpresaResponseDTO;
import com.forero.backend.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Empresas", description = "API para la gestión de empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Operation(
            summary = "Obtener todas las empresas",
            description = "Retorna una lista de todas las empresas registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de empresas obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaResponseDTO.class))
    )
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
        List<EmpresaResponseDTO> empresas = empresaService.obtenerTodasLasEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @Operation(
            summary = "Obtener empresa por NIT",
            description = "Retorna una empresa específica basada en su NIT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/{nit}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresa(
            @Parameter(description = "NIT de la empresa", required = true)
            @PathVariable String nit) {
        EmpresaResponseDTO empresa = empresaService.obtenerEmpresaPorNit(nit);
        return ResponseEntity.ok(empresa);
    }

    @Operation(
            summary = "Crear nueva empresa",
            description = "Crea una nueva empresa en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de empresa inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear empresas")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(
            @Parameter(description = "Datos de la empresa", required = true)
            @Valid @RequestBody EmpresaDTO empresaDTO) {
        EmpresaResponseDTO empresaCreada = empresaService.crearEmpresa(empresaDTO);
        return ResponseEntity.ok(empresaCreada);
    }

    @Operation(
            summary = "Actualizar empresa",
            description = "Actualiza los datos de una empresa existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de empresa inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @PutMapping("/{nit}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaResponseDTO> actualizarEmpresa(
            @Parameter(description = "NIT de la empresa", required = true)
            @PathVariable String nit,
            @Parameter(description = "Datos actualizados de la empresa", required = true)
            @Valid @RequestBody EmpresaDTO empresaDTO) {
        EmpresaResponseDTO empresaActualizada = empresaService.actualizarEmpresa(nit, empresaDTO);
        return ResponseEntity.ok(empresaActualizada);
    }

    @Operation(
            summary = "Eliminar empresa",
            description = "Elimina una empresa del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar empresas")
    })
    @DeleteMapping("/{nit}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarEmpresa(
            @Parameter(description = "NIT de la empresa a eliminar", required = true)
            @PathVariable String nit) {
        empresaService.eliminarEmpresa(nit);
        return ResponseEntity.noContent().build();
    }
}