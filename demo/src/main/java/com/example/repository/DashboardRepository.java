package com.example.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.model.DashboardData;
import java.util.List;

@Repository
public interface DashboardRepository extends CrudRepository<DashboardData, Long> {

    @Query(value = "CALL obtenerOpinionesPorEstrellas()", nativeQuery = true)
    List<Object[]> obtenerOpinionesPorEstrellas();

    @Query(value = "CALL obtenerVentasPorMes()", nativeQuery = true)
    List<Object[]> obtenerVentasPorMes();


    @Query(value = "CALL obtenerPlatosMasVendidos()", nativeQuery = true)
    List<Object[]> obtenerPlatosMasVendidos();
}
