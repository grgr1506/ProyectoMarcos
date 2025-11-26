package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vistasadmi")
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "vistasadmi/dashboard";
    }
    
    @GetMapping("/nosotros")
    public String nosotros() {
        return "vistasadmi/nosotros";
    }
    
    @GetMapping("/estadisticas")
    public String estadisticas() {
        return "vistasadmi/estadisticas";
    }

    // 👈 ESTA ES LA LÍNEA CORREGIDA
    // Ahora, al ir a /vistasadmi/reservas se muestra la plantilla reservas1.html
    @GetMapping("/reservas")
    public String reservas() {
        return "vistasadmi/reservas1"; 
    }
}