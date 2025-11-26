package com.example.controller;

import com.example.model.Reserva1;
import com.example.repository.Reserva1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/reservas1")
public class ReservasRestController {

    @Autowired
    private Reserva1Repository reservaRepo;

    @GetMapping
    public List<Reserva1> listarReservas(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String categoria) {

        if (fecha != null && categoria != null) {
            return reservaRepo.findByFechaAndCategoria(fecha, categoria);
        } else if (fecha != null) {
            return reservaRepo.findByFecha(fecha);
        } else if (categoria != null) {
            return reservaRepo.findByCategoria(categoria);
        } else {
            return (List<Reserva1>) reservaRepo.findAll();
        }
    }
}

