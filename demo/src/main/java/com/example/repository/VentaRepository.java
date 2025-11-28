package com.example.repository;

import com.example.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    // Para el reporte PDF (filtrar por mes y año)
    @Query("SELECT v FROM Venta v WHERE MONTH(v.fechaVenta) = :mes AND YEAR(v.fechaVenta) = :anio")
    List<Venta> findByMesAndAnio(@Param("mes") int mes, @Param("anio") int anio);

    // Para las tarjetas del Dashboard (Suma total del mes actual)
    @Query("SELECT SUM(v.total) FROM Venta v WHERE MONTH(v.fechaVenta) = MONTH(CURRENT_DATE()) AND YEAR(v.fechaVenta) = YEAR(CURRENT_DATE())")
    BigDecimal sumaVentasMesActual();
}