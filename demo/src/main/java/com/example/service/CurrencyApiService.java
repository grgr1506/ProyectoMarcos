package com.example.service;

import com.example.model.CurrencyApiResponse;
import lombok.RequiredArgsConstructor;
// Eliminamos la inyección @Value ya que Frankfurter no requiere API Key
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyApiService {

    private final RestTemplate restTemplate;

    // 💡 URL de Frankfurter para obtener tasas de USD y PEN. Es GRATUITA y no requiere token.
    private static final String API_BASE_URL = 
        "https://api.frankfurter.app/latest?from=USD&to=PEN"; 

    /**
     * Obtiene la tasa de PEN (Soles) a USD (Dólares).
     * El API nos da USD -> PEN. Calculamos la inversa (PEN -> USD).
     */
    public BigDecimal getPenToUsdRate() {
        // Tasa de contingencia (fallback) si la API falla
        BigDecimal fallbackRate = new BigDecimal("0.2700"); 

        try {
            // Llamada a la API
            CurrencyApiResponse response = restTemplate.getForObject(API_BASE_URL, CurrencyApiResponse.class);
            
            if (response != null && response.getRates() != null) {
                // Obtenemos la tasa USD -> PEN (ej: 1 USD = 4.05 PEN)
                Double penRate = response.getRates().get("PEN"); 
                
                if (penRate != null && penRate > 0) {
                    // Calculamos la inversa: PEN -> USD (ej: 1 / 4.05 = 0.2469 USD)
                    BigDecimal penToUsd = BigDecimal.ONE.divide(
                        BigDecimal.valueOf(penRate), 4, RoundingMode.HALF_UP
                    );
                    return penToUsd;
                }
            }
            
            return fallbackRate; 

        // Captura cualquier excepción de REST, JSON, o error de conexión
        } catch (Exception e) { 
            // Esto imprimirá el error en la consola de Spring para depurar, pero la web no caerá.
            System.err.println("Error FATAL al obtener la tasa de cambio con Frankfurter: " + e.getMessage());
            return fallbackRate; 
        }
    }

    /**
     * Convierte un monto de Soles a Dólares usando la tasa actual.
     */
    public BigDecimal convertPenToUsd(BigDecimal amountPen) {
        BigDecimal rate = getPenToUsdRate();
        return amountPen.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}