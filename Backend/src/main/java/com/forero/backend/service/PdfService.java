package com.forero.backend.service;

import com.forero.backend.model.Producto;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    public File generarPdfInventario(List<Producto> productos) throws Exception {
        String fileName = "inventario_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        File tempFile = File.createTempFile("temp_", fileName);

        PdfWriter writer = new PdfWriter(tempFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        // Título
        Paragraph titulo = new Paragraph("Reporte de Inventario")
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        document.add(titulo);

        // Fecha del reporte
        Paragraph fecha = new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setItalic();
        document.add(fecha);

        document.add(new Paragraph("\n")); // Espacio

        // Crear tabla
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 2, 2}))
                .useAllAvailableWidth();

        // Encabezados
        String[] headers = {"Código", "Nombre", "Precio USD", "Precio EUR", "Precio COP"};
        for (String header : headers) {
            table.addHeaderCell(
                    new Cell()
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph(header).setBold())
            );
        }

        // Datos
        for (Producto producto : productos) {
            table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(producto.getCodigo())));
            table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph(producto.getNombre())));
            table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph(String.format("$%.2f", producto.getPrecio_usd()))));
            table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph(String.format("€%.2f", producto.getPrecio_eur()))));
            table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph(String.format("$%.2f", producto.getPrecio_cop()))));
        }

        document.add(table);

        // Pie de página
        document.add(new Paragraph("\n"));
        document.add(
                new Paragraph("Total de productos: " + productos.size())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(12)
                        .setBold()
        );

        document.close();
        return tempFile;
    }
}