package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

// 💡 Nuevo DTO adaptado a la respuesta de Frankfurter API
@Data
public class CurrencyApiResponse {

    private String base; // Ej: "EUR" (La base predeterminada si no se especifica)
    
    // Contiene los tipos de cambio, ej: {"PEN": 4.05, "USD": 1.08}
    private Map<String, Double> rates; 
}