package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.model.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    @Query("SELECT u FROM Usuarios u WHERE LOWER(u.correo) = LOWER(:correo)")
    Usuarios findByCorreo(@Param("correo") String correo);
}
