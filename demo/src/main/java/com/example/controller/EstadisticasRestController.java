package com.example.controller;

import com.example.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasRestController {

    @Autowired
    private DashboardRepository dashboardRepo;

    @GetMapping("/opiniones")
    public Map<String, Object> getOpiniones() {
        List<Object[]> resultados = dashboardRepo.obtenerOpinionesPorEstrellas();

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] fila : resultados) {
            labels.add("⭐ " + fila[0].toString());
            data.add(((Number) fila[1]).intValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        return response;
    }

    @GetMapping("/ventas")
    public Map<String, Object> getVentas() {
        List<Object[]> resultados = dashboardRepo.obtenerVentasPorMes();

        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Object[] fila : resultados) {
            labels.add("Mes " + fila[0].toString());
            data.add(((Number) fila[1]).doubleValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        return response;
    }
    

//     @GetMapping("/ventas")
// public Map<String, Object> getVentasMock() {
//     Map<String, Object> response = new HashMap<>();
//     response.put("labels", List.of("Enero", "Febrero", "Marzo"));
//     response.put("data", List.of(250.0, 400.0, 300.0));
//     return response;
// }



// @GetMapping("/ventas")
// public Map<String, Object> getVentas(@RequestParam(required = false) Integer mes) {
//     List<Object[]> resultados;

//     if (mes != null) {
//         resultados = dashboardRepo.obtenerVentasPorMesFiltrado(mes);
//     } else {
//         resultados = dashboardRepo.obtenerVentasPorMes();
//     }

//     List<String> labels = new ArrayList<>();
//     List<Double> data = new ArrayList<>();

//     for (Object[] fila : resultados) {
//         labels.add("Mes " + fila[0].toString());
//         data.add(((Number) fila[1]).doubleValue());
//     }

//     Map<String, Object> response = new HashMap<>();
//     response.put("labels", labels);
//     response.put("data", data);
//     return response;
// }
// @GetMapping("/ventas")
// public Map<String, Object> getVentas(@RequestParam(required = false) Integer mes) {
//     List<Object[]> resultados;
//     List<String> labels = new ArrayList<>();
//     List<Double> data = new ArrayList<>();

//     if (mes != null) {
//         resultados = dashboardRepo.obtenerVentasPorMesFiltrado(mes);
//         for (Object[] fila : resultados) {
//             labels.add("Día " + fila[0].toString());         // día
//             data.add(((Number) fila[1]).doubleValue());      // total
//         }
//     } else {
//         resultados = dashboardRepo.obtenerVentasPorMes();
//         for (Object[] fila : resultados) {
//             labels.add("Mes " + fila[0].toString());         // mes
//             data.add(((Number) fila[1]).doubleValue());      // total
//         }
//     }

//     Map<String, Object> response = new HashMap<>();
//     response.put("labels", labels);
//     response.put("data", data);
//     return response;
// }


    @GetMapping("/bestsellers")
    public Map<String, Object> getBestSellers() {
        List<Object[]> resultados = dashboardRepo.obtenerPlatosMasVendidos();

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] fila : resultados) {
            labels.add(fila[0].toString());
            data.add(((Number) fila[1]).intValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        return response;
    }
}

