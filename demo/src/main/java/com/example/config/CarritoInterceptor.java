package com.example.config;

import com.example.service.CarritoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class CarritoInterceptor implements HandlerInterceptor {

    private final CarritoService carritoService;

    @Override
    public void postHandle(HttpServletRequest request, 
                          HttpServletResponse response, 
                          Object handler,
                          ModelAndView modelAndView) throws Exception {
        
        if (modelAndView != null) {
            HttpSession session = request.getSession();
            int cantidadCarrito = carritoService.obtenerCantidadItems(session);
            
            // Agregar cantidad al modelo para que esté disponible en todas las vistas
            modelAndView.addObject("cantidadCarrito", cantidadCarrito);
            
            // También guardarlo en la sesión
            session.setAttribute("cantidadCarrito", cantidadCarrito);
        }
    }
}
