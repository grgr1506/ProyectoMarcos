package com.example.repository;


import com.example.model.Reserva1;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Reserva1Repository extends CrudRepository<Reserva1, Integer> {

    @Query(value = "SELECT * FROM reservas1 WHERE DATE(fecha) = STR_TO_DATE(:fecha, '%Y-%m-%d') AND categoria = :categoria", nativeQuery = true)
    List<Reserva1> findByFechaAndCategoria(@Param("fecha") String fecha, @Param("categoria") String categoria);

    @Query(value = "SELECT * FROM reservas1 WHERE DATE(fecha) = STR_TO_DATE(:fecha, '%Y-%m-%d')", nativeQuery = true)
    List<Reserva1> findByFecha(@Param("fecha") String fecha);

    @Query(value = "SELECT * FROM reservas1 WHERE categoria = :categoria", nativeQuery = true)
    List<Reserva1> findByCategoria(@Param("categoria") String categoria);
}
