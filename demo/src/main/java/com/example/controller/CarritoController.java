package com.example.controller;

import com.example.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Mostrar página del carrito (Resumen de items)
    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        int cantidadItems = carritoService.obtenerCantidadItems(session);
        
        model.addAttribute("items", carritoService.obtenerItems(session));
        model.addAttribute("total", carritoService.calcularTotal(session));
        model.addAttribute("cantidadItems", cantidadItems);
        
        // Actualizar sesión para que el numerito del navbar se actualice
        session.setAttribute("cantidadCarrito", cantidadItems);
        
        return "carrito";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam Long platoId, 
                                  @RequestParam(defaultValue = "1") Integer cantidad,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            carritoService.agregarProducto(platoId, cantidad, session);
            
            // Actualizar cantidad en sesión
            session.setAttribute("cantidadCarrito", carritoService.obtenerCantidadItems(session));
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al agregar producto");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        // Redirige de vuelta al menú para seguir comprando
        return "redirect:/menu";
    }

    // Actualizar cantidad de un item (Para los botones + y -)
    @PostMapping("/actualizar/{itemId}")
    @ResponseBody
    public String actualizarCantidad(@PathVariable Long itemId,
                                     @RequestParam Integer cantidad) {
        try {
            carritoService.actualizarCantidad(itemId, cantidad);
            return "OK";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    // Eliminar item del carrito
    @PostMapping("/eliminar/{itemId}")
    public String eliminarItem(@PathVariable Long itemId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            carritoService.eliminarItem(itemId);
            
            // Actualizar sesión
            session.setAttribute("cantidadCarrito", carritoService.obtenerCantidadItems(session));
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar producto");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/carrito";
    }

    // Vaciar todo el carrito
    @PostMapping("/vaciar")
    public String vaciarCarrito(HttpSession session,
                                RedirectAttributes redirectAttributes) {
        carritoService.vaciarCarrito(session);
        
        // Actualizar sesión a 0
        session.setAttribute("cantidadCarrito", 0);
        
        redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/carrito";
    }

    // ==========================================================================
    // 🔴 HE ELIMINADO EL MÉTODO "checkout" DE AQUÍ.
    // AHORA LO MANEJA "PagoController.java" PARA EVITAR EL CONFLICTO.
    // ==========================================================================
}
