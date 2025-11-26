
package com.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "tipo_consulta", nullable = false, length = 50)
    private String tipoConsulta; // pedido | reserva | delivery | evento | reclamo | otro

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(nullable = false, length = 20)
    private String estado; // PENDIENTE | EN_PROCESO | RESUELTA

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    void prePersist() {
        if (estado == null)
            estado = "PENDIENTE";
        if (creadoEn == null)
            creadoEn = LocalDateTime.now();
    }
}
