package com.example.controller;

import com.example.model.*;
import com.example.service.RestauranteService;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    // ===== PÁGINAS PRINCIPALES =====

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("titulo", "LA REAL 394");
        model.addAttribute("active", "inicio");
        model.addAttribute("platos", restauranteService.listarPlatos());
        return "index";
    }

    /* 
    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("titulo", "Menú - LA REAL 394");
        model.addAttribute("active", "menu");
        model.addAttribute("platos", restauranteService.listarPlatos());
        return "menu";
    }
    */

    // @GetMapping("/registro")
    // public String registro(Model modelo) {
    //     modelo.addAttribute("usuarios", new Usuarios());
    //     return "registro";
    // }

    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("titulo", "Nosotros - LA REAL 394");
        model.addAttribute("active", "nosotros");
        return "nosotros";
    }

    @GetMapping("/contactanos")
    public String mostrarFormulario(Model model) {
        // Inicializa un objeto ContactoMensaje vacío para enlazar los datos
        // (Aunque para un formulario simple POST sin error en GET no es estrictamente
        // necesario, es buena práctica)
        model.addAttribute("contactoMensaje", new ContactoMensaje());
        List<Reserva> reservas = restauranteService.listarReservas();
        model.addAttribute("reservasDelDia", reservas);
        // Retorna el nombre del template HTML (contactanos.html)
        return "contactanos";
    }

    
//    @GetMapping("/login")
//     public String login(Model modelo) {
//         modelo.addAttribute("usuarios", new Usuarios());
//         return "login";
//     }

    @GetMapping("/opiniones")
    public String opiniones(Model model) {

        // Datos para los gráficos con listas
        List<String> estrellasLabels = Arrays.asList("1 estrella", "2 estrellas", "3 estrellas", "4 estrellas",
                "5 estrellas");
        List<Integer> estrellasData = Arrays.asList(10, 20, 30, 40, 50); // Ejemplo de cantidad de opiniones por cada
                                                                         // calificación

        List<String> tortaLabels = Arrays.asList("Combo La Real", "Combo para 1", "Real Bacon", "Americana",
                "BBQ Master");
        List<Integer> tortaData = Arrays.asList(50, 30, 40, 60, 80); // Ejemplo de ventas de hamburguesas más populares

        List<String> lineasLabels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio");
        List<Integer> lineasData = Arrays.asList(100, 200, 150, 250, 300, 220);

        // Pasar los datos al modelo de Thymeleaf
        model.addAttribute("estrellasLabels", estrellasLabels);
        model.addAttribute("estrellasData", estrellasData);

        model.addAttribute("tortaLabels", tortaLabels);
        model.addAttribute("tortaData", tortaData);

        model.addAttribute("lineasLabels", lineasLabels);
        model.addAttribute("lineasData", lineasData);

        return "opiniones";
    }

    // ===== GESTIÓN DE PLATOS =====

    @GetMapping("/platos")
    public String listarPlatos(Model model) {
        model.addAttribute("platos", restauranteService.listarPlatos());
        return "platos/lista";
    }

    @GetMapping("/platos/nuevo")
    public String nuevoPlatoForm(Model model) {
        model.addAttribute("plato", new Plato());
        return "platos/form";
    }

    @PostMapping("/platos/guardar")
    public String guardarPlato(@ModelAttribute Plato plato, RedirectAttributes redirectAttributes) {
        restauranteService.agregarPlato(plato);
        redirectAttributes.addFlashAttribute("mensaje", "Plato guardado exitosamente");
        return "redirect:/platos";
    }

    @GetMapping("/platos/editar/{id}")
    public String editarPlatoForm(@PathVariable Long id, Model model) {
        model.addAttribute("plato", restauranteService.buscarPlatoPorId(id).orElse(new Plato()));
        return "platos/form";
    }

    @PostMapping("/platos/eliminar/{id}")
    public String eliminarPlato(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (restauranteService.eliminarPlato(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Plato eliminado");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar");
        }
        return "redirect:/platos";
    }

    @GetMapping("/platos/buscar")
    public String buscarPlatos(@RequestParam String nombre, Model model) {
        model.addAttribute("platos", restauranteService.buscarPlatosPorNombre(nombre));
        return "platos/lista";
    }

    // ===== GESTIÓN DE CLIENTES =====

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", restauranteService.listarClientes());
        return "clientes/lista";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        restauranteService.agregarCliente(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editarClienteForm(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", restauranteService.buscarClientePorId(id).orElse(new Cliente()));
        return "clientes/form";
    }

    @PostMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (restauranteService.eliminarCliente(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/buscar")
    public String buscarClientes(@RequestParam String nombre, Model model) {
        model.addAttribute("clientes", restauranteService.buscarClientesPorNombre(nombre));
        return "clientes/lista";
    }

    // ===== GESTIÓN DE RESERVAS =====

    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        model.addAttribute("reservas", restauranteService.listarReservas());
        return "reservas/lista";
    }

    @GetMapping("/reservas/nueva")
    public String nuevaReservaForm(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clientes", restauranteService.listarClientes());
        return "reservas/form";
    }

    @PostMapping("/reservas/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        restauranteService.agregarReserva(reserva);
        redirectAttributes.addFlashAttribute("mensaje", "Reserva guardada exitosamente");
        return "redirect:/reservas";
    }

    @GetMapping("/reservas/editar/{id}")
    public String editarReservaForm(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", restauranteService.buscarReservaPorId(id).orElse(new Reserva()));
        model.addAttribute("clientes", restauranteService.listarClientes());
        return "reservas/form";
    }

    @PostMapping("/reservas/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (restauranteService.eliminarReserva(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Reserva eliminada");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/cambiar-estado/{id}")
    public String cambiarEstadoReserva(@PathVariable Long id, @RequestParam String estado,
            RedirectAttributes redirectAttributes) {
        if (restauranteService.cambiarEstadoReserva(id, estado)) {
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/contactanos")
    public String procesarFormulario(
            @ModelAttribute ContactoMensaje contactoMensaje,
            RedirectAttributes redirectAttributes) {

        // --- LÓGICA DEL BACKEND ---

        // *****************************************************************
        // 1. Aquí puedes guardar el objeto 'contactoMensaje' en una base de datos.
        // 2. Aquí puedes usar un servicio para enviar un correo electrónico.
        // 3. Aquí puedes realizar cualquier validación avanzada.
        // *****************************************************************

        System.out.println("Mensaje recibido: " + contactoMensaje.toString());

        // Añade un mensaje flash que se mostrará en la página de éxito
        redirectAttributes.addFlashAttribute("mensajeExito",
                "¡Mensaje enviado con éxito! Pronto nos pondremos en contacto contigo.");

        // Redirige al usuario a una página de éxito (o a la misma página)
        // Redirección es importante para evitar el doble envío al recargar.
        return "redirect:/contactanos?enviado=true";
    }

}
