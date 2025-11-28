package com.example.service;

import com.example.model.Venta;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class ReportePdfService {

    public void exportarVentasMensuales(HttpServletResponse response, List<Venta> ventas, String mes, int anio) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Fuentes
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(255, 192, 0)); // Color1
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font fontDatos = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Título
        Paragraph titulo = new Paragraph("Reporte Mensual de Ventas - La Real 394", fontTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titulo);
        
        document.add(new Paragraph("Período: " + mes + " " + anio));
        document.add(new Paragraph("Generado el: " + java.time.LocalDate.now()));
        document.add(new Paragraph(" ")); // Espacio

        // Tabla
        PdfPTable tabla = new PdfPTable(4); // 4 columnas
        tabla.setWidthPercentage(100f);
        tabla.setSpacingBefore(10);
        tabla.setWidths(new float[]{1.5f, 3.0f, 3.0f, 2.5f});

        // Cabeceras
        crearCeldaCabecera(tabla, "ID Venta", fontCabecera);
        crearCeldaCabecera(tabla, "Fecha", fontCabecera);
        crearCeldaCabecera(tabla, "Método Pago", fontCabecera);
        crearCeldaCabecera(tabla, "Monto (S/)", fontCabecera);

        // Datos
        double totalMes = 0;
        for (Venta venta : ventas) {
            tabla.addCell(new Phrase(String.valueOf(venta.getIdVenta()), fontDatos));
            tabla.addCell(new Phrase(venta.getFechaVenta().toString(), fontDatos));
            tabla.addCell(new Phrase(venta.getMetodoPago(), fontDatos));
            tabla.addCell(new Phrase("S/ " + venta.getTotal(), fontDatos));
            totalMes += venta.getTotal().doubleValue();
        }

        document.add(tabla);

        // Total Final
        Paragraph totalP = new Paragraph("TOTAL INGRESOS: S/ " + String.format("%.2f", totalMes), 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        totalP.setAlignment(Paragraph.ALIGN_RIGHT);
        totalP.setSpacingBefore(15);
        document.add(totalP);

        document.close();
    }

    private void crearCeldaCabecera(PdfPTable tabla, String titulo, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(titulo, fuente));
        celda.setBackgroundColor(new Color(33, 37, 41)); // Color4 (Dark)
        celda.setPadding(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}