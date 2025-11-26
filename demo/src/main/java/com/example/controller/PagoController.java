package com.example.controller;

import com.example.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class PagoController {

    private final CarritoService carritoService;

    @GetMapping("/checkout")
    public String mostrarCheckout(Model model, HttpSession session) {
        // 1. Verificar si hay items, si no, redirigir al menú
        int cantidadItems = carritoService.obtenerCantidadItems(session);
        if (cantidadItems == 0) {
            return "redirect:/menu";
        }

        // 2. Calcular totales
        BigDecimal total = carritoService.calcularTotal(session);
        // Sumamos el delivery fijo de 5.00
        BigDecimal totalConDelivery = total.add(new BigDecimal("5.00"));

        // 3. Pasar datos a la vista (Checkout.html)
        // --- ESTA ES LA LÍNEA QUE FALTABA PARA EL RESUMEN DETALLADO ---
        model.addAttribute("items", carritoService.obtenerItems(session)); 
        // --------------------------------------------------------------

        model.addAttribute("total", total);
        model.addAttribute("totalPagar", totalConDelivery);
        model.addAttribute("cantidadItems", cantidadItems);
        
        // Datos dummy para el usuario (si no tienes sistema de login aún)
        model.addAttribute("nombreUsuario", "Cliente"); 

        return "checkout";
    }

    @PostMapping("/procesar-pago")
    public String procesarPago(@RequestParam String metodoPago, HttpSession session) {
        // AQUÍ OCURRE LA MAGIA DEL BACKEND (Guardar orden en BD, enviar correo, etc.)
        
        // Simulación: Vaciamos el carrito después del pago exitoso
        carritoService.vaciarCarrito(session);
        
        // Redirigir a la página de éxito
        return "redirect:/carrito/pago-exitoso";
    }

    @GetMapping("/pago-exitoso")
    public String pagoExitoso(HttpSession session) {
        // Generar un número de orden random para simular visualmente
        session.setAttribute("nroOrden", "ORD-" + System.currentTimeMillis());
        return "pago-exitoso";
    }
}