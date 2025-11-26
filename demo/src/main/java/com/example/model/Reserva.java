
package com.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // 👈 para bind con <input type="datetime-local">
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private int numeroPersonas;

    @Column(length = 20)
    private String mesa;

    @Column(length = 20)
    private String estado; // PENDIENTE | CONFIRMADA | CANCELADA

    @Column(length = 500)
    private String observaciones;
}
