package com.example.controller;

import com.example.model.Reserva;
import com.example.service.ReservaApiService;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservaApiController {

    private final ReservaApiService service;

    public ReservaApiController(ReservaApiService service) {
        this.service = service;
    }

    // GET usado por cargarReservas() en tu JS
    @GetMapping("/reservas/del-dia")
    public List<Map<String, Object>> reservasDelDia() {
        List<Reserva> reservas = service.reservasDelDia();
        List<Map<String, Object>> out = new ArrayList<>();
        for (Reserva r : reservas) {
            Map<String, Object> cli = new HashMap<>();
            if (r.getCliente() != null) {
                cli.put("nombre",   Optional.ofNullable(r.getCliente().getNombre()).orElse("—"));
                cli.put("telefono", Optional.ofNullable(r.getCliente().getTelefono()).orElse("—"));
            } else {
                cli.put("nombre", "—");
                cli.put("telefono", "—");
            }
            Map<String, Object> row = new HashMap<>();
            row.put("id", r.getId());
            row.put("fechaHora", r.getFechaHora().toString()); // tu JS hace substring(11,16) -> HH:mm
            row.put("numeroPersonas", r.getNumeroPersonas());
            row.put("cliente", cli);
            out.add(row);
        }
        return out;
    }

    // POST usado por el form (fetch) de tu HTML
    @PostMapping("/reservas/api/guardar")
    public Map<String, Object> guardar(@RequestBody Map<String, Object> body) {
        Map<String, Object> res = new HashMap<>();
        try {
            String nombre   = Objects.toString(body.get("nombre"), "").trim();
            String telefono = Objects.toString(body.get("telefono"), "").trim();
            String hora     = Objects.toString(body.get("hora"), "").trim();
            Integer personas = (body.get("personas") instanceof Number)
                    ? ((Number) body.get("personas")).intValue()
                    : Integer.parseInt(Objects.toString(body.get("personas")));

            if (nombre.isEmpty() || telefono.isEmpty() || hora.isEmpty() || personas == null || personas < 1) {
                res.put("success", false);
                res.put("message", "Datos incompletos o inválidos");
                return res;
            }

            Reserva saved = service.guardarReserva(nombre, telefono, personas, hora);
            res.put("success", true);
            res.put("id", saved.getId());
            return res;

        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return res;
        }
    }

    // DELETE usado por tu botón eliminar
    @DeleteMapping("/reservas/api/eliminar/{id}")
    public Map<String, Object> eliminar(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        boolean ok = service.eliminar(id);
        res.put("success", ok);
        if (!ok) res.put("message", "Reserva no encontrada");
        return res;
    }
}
