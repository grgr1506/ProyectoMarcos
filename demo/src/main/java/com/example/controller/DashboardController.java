package com.example.controller;

import com.example.model.Venta;
import com.example.repository.ClienteRepository;
import com.example.repository.Reserva1Repository;
import com.example.repository.VentaRepository;
import com.example.service.ReportePdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vistasadmi")
public class DashboardController {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private Reserva1Repository reservaRepository; // Usamos Reserva1 ya que es la que usa tu vista admin
    @Autowired private ReportePdfService reportePdfService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. Obtener datos reales de la BD
        long totalClientes = clienteRepository.count();
        long totalReservasHoy = reservaRepository.findByFecha(LocalDate.now().toString()).size(); // Tu repo usa String fecha
        BigDecimal ventasMes = ventaRepository.sumaVentasMesActual();
        
        // Si es null (no hay ventas), poner 0
        if (ventasMes == null) ventasMes = BigDecimal.ZERO;

        // 2. Pasar datos al HTML
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalReservasHoy", totalReservasHoy);
        model.addAttribute("ventasMes", ventasMes);
        model.addAttribute("mesActual", LocalDate.now().getMonth().toString());
        
        // 3. Datos por defecto para el formulario de reporte
        model.addAttribute("anioActual", LocalDate.now().getYear());
        
        return "vistasadmi/dashboard";
    }

    @GetMapping("/reporte/pdf")
    public void descargarReportePdf(
            @RequestParam(name = "mes") int mes,
            @RequestParam(name = "anio") int anio,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        String cabecera = "attachment; filename=Reporte_Ventas_" + mes + "_" + anio + ".pdf";
        response.setHeader("Content-Disposition", cabecera);

        List<Venta> ventas = ventaRepository.findByMesAndAnio(mes, anio);
        
        // Obtener nombre del mes
        String nombreMes = new DateFormatSymbols(new java.util.Locale("es", "ES")).getMonths()[mes-1];
        
        reportePdfService.exportarVentasMensuales(response, ventas, nombreMes.toUpperCase(), anio);
    }

    @GetMapping("/nosotros")
    public String nosotros() { return "vistasadmi/nosotros"; }
    
    @GetMapping("/estadisticas")
    public String estadisticas() { return "vistasadmi/estadisticas"; }

    @GetMapping("/reservas")
    public String reservas() { return "vistasadmi/reservas1"; }
}