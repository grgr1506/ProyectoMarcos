package com.example.controller;

import com.example.model.Consulta;
import com.example.service.ConsultaService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class ConsultaApiController {

    private final ConsultaService service;

    public ConsultaApiController(ConsultaService service) {
        this.service = service;
    }

    // Lista en JSON para la tabla
    @GetMapping("/consultas/api/listar")
    public List<Map<String, Object>> listar() {
        var out = new ArrayList<Map<String, Object>>();
        for (Consulta c : service.listar()) {
            var row = new HashMap<String, Object>();
            row.put("id", c.getId());
            row.put("nombre", c.getNombre());
            row.put("apellido", c.getApellido());
            row.put("telefono", c.getTelefono());
            row.put("email", c.getEmail());
            row.put("tipoConsulta", c.getTipoConsulta());
            row.put("mensaje", c.getMensaje());
            row.put("estado", c.getEstado());
            out.add(row);
        }
        return out;
    }

    // Guardar desde el formulario (AJAX)
    @PostMapping("/consultas/api/guardar")
    public Map<String, Object> guardar(@RequestBody Map<String, Object> body) {
        var res = new HashMap<String, Object>();
        try {
            Consulta c = new Consulta();
            c.setNombre(Objects.toString(body.get("nombre"), "").trim());
            c.setApellido(Objects.toString(body.get("apellido"), "").trim());
            c.setTelefono(Objects.toString(body.get("telefono"), "").trim());
            c.setEmail(Objects.toString(body.get("email"), "").trim());
            c.setTipoConsulta(Objects.toString(body.get("tipoConsulta"), "").trim());
            c.setMensaje(Objects.toString(body.get("mensaje"), "").trim());
            c.setEstado("PENDIENTE");

            if (c.getNombre().isEmpty() || c.getApellido().isEmpty() ||
                    c.getTelefono().isEmpty() || c.getEmail().isEmpty() ||
                    c.getTipoConsulta().isEmpty() || c.getMensaje().isEmpty()) {
                res.put("success", false);
                res.put("message", "Datos incompletos");
                return res;
            }

            Consulta saved = service.guardar(c);
            res.put("success", true);
            res.put("id", saved.getId());
            return res;
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return res;
        }
    }

    // Cambiar estado: PENDIENTE | EN_PROCESO | RESUELTA
    @PatchMapping("/consultas/api/estado/{id}")
    public Map<String, Object> estado(@PathVariable Long id, @RequestParam String estado) {
        var res = new HashMap<String, Object>();
        boolean ok = service.cambiarEstado(id, estado);
        res.put("success", ok);
        if (!ok)
            res.put("message", "Consulta no encontrada");
        return res;
    }

    // Eliminar
    @DeleteMapping("/consultas/api/eliminar/{id}")
    public Map<String, Object> eliminar(@PathVariable Long id) {
        var res = new HashMap<String, Object>();
        boolean ok = service.eliminar(id);
        res.put("success", ok);
        if (!ok)
            res.put("message", "Consulta no encontrada");
        return res;
    }
}
