package com.example.service;

import com.example.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    private List<Plato> platos;
    private List<Cliente> clientes;
    private List<Reserva> reservas;
    private Long nextPlatoId = 1L;
    private Long nextClienteId = 1L;
    private Long nextReservaId = 1L;

    public RestauranteService() {
        this.platos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.reservas = new ArrayList<>();
        inicializarDatos();
    }

    private void inicializarDatos() {
        // CORRECCIÓN AQUÍ:
        // Ahora el constructor de Plato pide: id, nombre, descripcion, precio, categoria, disponible, IMAGEN, ES_BESTSELLER
        
        platos.add(new Plato(
            nextPlatoId++, 
            "Ceviche Clásico", 
            "Pescado fresco marinado", 
            new BigDecimal("25.00"),
            "Entradas", 
            true, 
            "ceviche.jpg", // Imagen (puedes cambiar el nombre aquí)
            false          // Es Bestseller
        ));

        platos.add(new Plato(
            nextPlatoId++, 
            "Lomo Saltado", 
            "Carne salteada con papas", 
            new BigDecimal("32.00"),
            "Principales", 
            true, 
            "lomo.jpg",    // Imagen
            true           // Es Bestseller (ejemplo)
        ));

        platos.add(new Plato(
            nextPlatoId++, 
            "Ají de Gallina", 
            "Pollo en crema de ají", 
            new BigDecimal("28.00"),
            "Principales", 
            true, 
            "aji.jpg",     // Imagen
            false          // Es Bestseller
        ));

        // Clientes iniciales
        clientes.add(new Cliente(nextClienteId++, "Juan", "Pérez", "juan@email.com", "999888777", "Lima"));
        clientes.add(new Cliente(nextClienteId++, "María", "García", "maria@email.com", "987654321", "Lima"));
    }

    // ===== OPERACIONES PLATOS =====

    // Listar platos
    public List<Plato> listarPlatos() {
        return new ArrayList<>(platos);
    }

    // Buscar plato por ID
    public Optional<Plato> buscarPlatoPorId(Long id) {
        return platos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    // Buscar platos por nombre
    public List<Plato> buscarPlatosPorNombre(String nombre) {
        return platos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar platos por categoría
    public List<Plato> buscarPlatosPorCategoria(String categoria) {
        return platos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    // Agregar plato
    public Plato agregarPlato(Plato plato) {
        plato.setId(nextPlatoId++);
        platos.add(plato);
        return plato;
    }

    // Eliminar plato
    public boolean eliminarPlato(Long id) {
        return platos.removeIf(p -> p.getId().equals(id));
    }

    // ===== OPERACIONES CLIENTES =====

    // Listar clientes
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    // Buscar cliente por ID
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clientes.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    // Buscar clientes por nombre
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                        c.getApellido().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar cliente por email
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clientes.stream().filter(c -> c.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    // Agregar cliente
    public Cliente agregarCliente(Cliente cliente) {
        cliente.setId(nextClienteId++);
        clientes.add(cliente);
        return cliente;
    }

    public Cliente obtenerOCrearCliente(String nombre, String telefono) {
        // Implementación simplificada para que compile
        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setTelefono(telefono);
        return c;
    }

    // Eliminar cliente
    public boolean eliminarCliente(Long id) {
        return clientes.removeIf(c -> c.getId().equals(id));
    }

    // ===== OPERACIONES RESERVAS =====

    // Listar reservas
    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    // Buscar reserva por ID
    public Optional<Reserva> buscarReservaPorId(Long id) {
        return reservas.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    // Buscar reservas por cliente
    public List<Reserva> buscarReservasPorCliente(Long clienteId) {
        return reservas.stream()
                .filter(r -> r.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    // Buscar reservas por estado
    public List<Reserva> buscarReservasPorEstado(String estado) {
        return reservas.stream()
                .filter(r -> r.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    // Agregar reserva
    public Reserva agregarReserva(Reserva reserva) {
        reserva.setId(nextReservaId++);
        reservas.add(reserva);
        return reserva;
    }

    // Eliminar reserva
    public boolean eliminarReserva(Long id) {
        return reservas.removeIf(r -> r.getId().equals(id));
    }

    // Cambiar estado de reserva
    public boolean cambiarEstadoReserva(Long id, String nuevoEstado) {
        Optional<Reserva> reserva = buscarReservaPorId(id);
        if (reserva.isPresent()) {
            reserva.get().setEstado(nuevoEstado);
            return true;
        }
        return false;
    }
}
