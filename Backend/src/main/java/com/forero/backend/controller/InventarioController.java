package com.forero.backend.controller;

import com.forero.backend.dto.InventarioDTO;
import com.forero.backend.model.Producto;
import com.forero.backend.service.InventarioService;
import com.forero.backend.service.PdfService;
import com.forero.backend.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoRepository productoRepository;
    private final PdfService pdfService;


    @GetMapping("/download")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadReporte() {
        try {
            // Generar el PDF
            List<Producto> productos = productoRepository.findAll();
            File pdfFile = pdfService.generarPdfInventario(productos);

            // Leer el contenido del archivo
            byte[] contents = Files.readAllBytes(pdfFile.toPath());

            // Eliminar el archivo temporal
            pdfFile.delete();

            // Configurar los headers de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "inventario.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(contents.length)
                    .body(contents);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el reporte: " + e.getMessage());
        }
    }
}