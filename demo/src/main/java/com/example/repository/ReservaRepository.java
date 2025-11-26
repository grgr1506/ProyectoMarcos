package com.example.repository;

import com.example.model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findAllByFechaHoraBetweenOrderByFechaHora(LocalDateTime start, LocalDateTime end);
}
