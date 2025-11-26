package com.example.repository;

import com.example.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    Optional<Carrito> findByUsuarioId(String usuarioId);
    
    void deleteByUsuarioId(String usuarioId);
}
