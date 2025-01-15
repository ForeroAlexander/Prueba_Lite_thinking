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
    private final AwsService awsService;

    public String generarYEnviarReporteInventario(InventarioDTO dto) throws Exception {
        List<Producto> productos = productoRepository.findAll();
        File pdfFile = pdfService.generarPdfInventario(productos);
        String nombreArchivo = pdfFile.getName();
        String s3Url = awsService.subirArchivoS3(pdfFile, nombreArchivo);

        awsService.enviarEmail(
                dto.getEmailDestino(),
                "Reporte de Inventario",
                "Adjunto encontrará el reporte de inventario. También puede descargarlo desde el siguiente enlace:",
                s3Url
        );
        pdfFile.delete();
        return s3Url;
    }

    public File generarReporteDescargable() throws Exception {
        List<Producto> productos = productoRepository.findAll();
        return pdfService.generarPdfInventario(productos);
    }
}
