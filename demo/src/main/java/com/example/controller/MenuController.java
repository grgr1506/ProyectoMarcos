package com.example.controller;

import com.example.repository.PlatoRepository;
import com.example.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final PlatoRepository platoRepository;
    private final CarritoService carritoService;

    @GetMapping
    public String mostrarMenu(Model model, HttpSession session) {
        model.addAttribute("platos", platoRepository.findAll());
        
        // Guardar cantidad en sesión para que esté disponible en la navbar
        int cantidadCarrito = carritoService.obtenerCantidadItems(session);
        session.setAttribute("cantidadCarrito", cantidadCarrito);
        model.addAttribute("cantidadCarrito", cantidadCarrito);
        
        return "menu";
    }
}
