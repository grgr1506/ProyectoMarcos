package com.example.service;

import com.example.model.Carrito;
import com.example.model.ItemCarrito;
import com.example.model.Plato;
import com.example.repository.CarritoRepository;
import com.example.repository.ItemCarritoRepository;
import com.example.repository.PlatoRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final PlatoRepository platoRepository;

    // Obtener o crear carrito
    public Carrito obtenerOCrearCarrito(HttpSession session) {
        String sessionId = (String) session.getAttribute("carritoId");

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("carritoId", sessionId);
        }

        // CORRECCIÓN 1: Creamos una variable final para usarla dentro de la lambda
        String finalSessionId = sessionId;

        return carritoRepository.findByUsuarioId(finalSessionId)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuarioId(finalSessionId); // Usamos la variable final
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    // Agregar producto al carrito
    public void agregarProducto(Long platoId, Integer cantidad, HttpSession session) {
        Carrito carrito = obtenerOCrearCarrito(session);

        Optional<Plato> platoOpt = platoRepository.findById(platoId);
        if (platoOpt.isEmpty()) {
            throw new RuntimeException("Plato no encontrado");
        }

        Plato plato = platoOpt.get();

        // Buscar si ya existe en el carrito
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito.getId(), platoId);

        if (itemExistente.isPresent()) {
            // Si existe, actualizar cantidad
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepository.save(item);
        } else {
            // Si no existe, crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProductoId(plato.getId());
            nuevoItem.setNombre(plato.getNombre());
            
            // CORRECCIÓN 2: Tu entidad Plato NO tiene imagen. 
            // Lo dejamos en null o ponemos una imagen por defecto para que no de error.
            nuevoItem.setImagen(null); 
            
            nuevoItem.setPrecio(plato.getPrecio());
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setTipo(plato.getCategoria());

            itemCarritoRepository.save(nuevoItem);
        }
    }

    // Actualizar cantidad de un item
    public void actualizarCantidad(Long itemId, Integer cantidad) {
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (cantidad <= 0) {
            itemCarritoRepository.delete(item);
        } else {
            item.setCantidad(cantidad);
            itemCarritoRepository.save(item);
        }
    }

    // Eliminar item del carrito
    public void eliminarItem(Long itemId) {
        itemCarritoRepository.deleteById(itemId);
    }

    // Obtener items del carrito
    public List<ItemCarrito> obtenerItems(HttpSession session) {
        Carrito carrito = obtenerOCrearCarrito(session);
        return itemCarritoRepository.findByCarritoId(carrito.getId());
    }

    // Calcular total del carrito
    public BigDecimal calcularTotal(HttpSession session) {
        List<ItemCarrito> items = obtenerItems(session);
        return items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Vaciar carrito
    public void vaciarCarrito(HttpSession session) {
        String sessionId = (String) session.getAttribute("carritoId");
        if (sessionId != null) {
            carritoRepository.deleteByUsuarioId(sessionId);
            session.removeAttribute("carritoId");
        }
    }

    // Obtener cantidad total de items
    public int obtenerCantidadItems(HttpSession session) {
        List<ItemCarrito> items = obtenerItems(session);
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}