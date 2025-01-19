package com.forero.backend.service;

import com.forero.backend.dto.InventarioDTO;
import com.forero.backend.model.Producto;
import com.forero.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final PdfService pdfService;
}
