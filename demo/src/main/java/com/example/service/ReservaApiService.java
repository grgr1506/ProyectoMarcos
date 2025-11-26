package com.example.service;

import com.example.model.Cliente;
import com.example.model.Reserva;
import com.example.repository.ClienteRepository;
import com.example.repository.ReservaRepository;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaApiService {

    private final ReservaRepository reservaRepo;
    private final ClienteRepository clienteRepo;
    private static final ZoneId LIMA = ZoneId.of("America/Lima");

    public ReservaApiService(ReservaRepository reservaRepo, ClienteRepository clienteRepo) {
        this.reservaRepo = reservaRepo;
        this.clienteRepo = clienteRepo;
    }

    public List<Reserva> reservasDelDia() {
        LocalDate hoy = LocalDate.now(LIMA);
        LocalDateTime start = hoy.atStartOfDay();
        LocalDateTime end = hoy.plusDays(1).atStartOfDay();
        return reservaRepo.findAllByFechaHoraBetweenOrderByFechaHora(start, end);
    }

    @Transactional
    public Reserva guardarReserva(String nombre, String telefono, int personas, String horaHHmm) {
        // buscar o crear cliente
        Cliente cliente = clienteRepo.findByNombreAndTelefono(nombre, telefono)
                .orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setNombre(nombre);
                    c.setApellido("");
                    c.setTelefono(telefono);
                    // Si tu Cliente.email es NOT NULL + UNIQUE, generamos uno sintético:
                    String unico = System.currentTimeMillis() + "-" + telefono;
                    c.setEmail(("reserva+" + unico + "@lareal.local").replace(" ", "").toLowerCase());
                    c.setDireccion("");
                    return clienteRepo.save(c);
                });

        LocalTime time = LocalTime.parse(horaHHmm); // "HH:mm"
        LocalDate fecha = LocalDate.now(LIMA); // hoy Lima
        LocalDateTime fechaHora = LocalDateTime.of(fecha, time);

        Reserva r = new Reserva();
        r.setCliente(cliente);
        r.setFechaHora(fechaHora);
        r.setNumeroPersonas(personas);
        r.setMesa(null);
        r.setEstado("CONFIRMADA");
        r.setObservaciones(null);
        return reservaRepo.save(r);
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (!reservaRepo.existsById(id))
            return false;
        reservaRepo.deleteById(id);
        return true;
    }
}
