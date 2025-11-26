
package com.example.service;

import com.example.model.UsuarioDTO;
import com.example.model.Usuarios;
import com.example.repository.UsuariosRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Usuarios_Servicio {

    @Autowired
    private UsuariosRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ==============================
    // LOGIN (usa BCrypt + enabled)
    // ==============================
    public Usuarios validarUsuario(String correo, String contrasena) {
        Usuarios usuario = repo.findByCorreo(correo.trim().toLowerCase());

        if (usuario == null) {
            return null;
        }

        // Debe estar verificado
        if (!usuario.isEnabled()) {
            return null;
        }

        // Compara la contraseña ingresada con el hash almacenado
        if (passwordEncoder.matches(contrasena.trim(), usuario.getContrasena())) {
            return usuario;
        }
        return null;
    }

    // ==============================
    // REGISTRO + ENVÍO DE CÓDIGO
    // (maneja usuario nuevo, ya registrado sin verificar, y ya verificado)
    // ==============================
    public Usuarios registrarConVerificacion(Usuarios datosFormulario) {

        String correoNormalizado = datosFormulario.getCorreo().trim().toLowerCase();
        Usuarios existente = repo.findByCorreo(correoNormalizado);

        // CASO 1: ya existe y está VERIFICADO
        if (existente != null && existente.isEnabled()) {
            throw new RuntimeException("El correo ya está registrado y la cuenta ya está verificada. Inicia sesión.");
        }

        // CASO 2: ya existe pero NO está verificado -> reenvío código
        if (existente != null && !existente.isEnabled()) {
            String code = generarCodigo(6);
            String codeHash = passwordEncoder.encode(code);

            existente.setVerificationCodeHash(codeHash);
            existente.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));

            repo.save(existente);
            emailService.sendVerificationEmail(existente.getCorreo(), code);

            return existente;
        }

        // CASO 3: usuario NUEVO
        Usuarios nuevo = new Usuarios();
        nuevo.setCorreo(correoNormalizado);
        nuevo.setContrasena(passwordEncoder.encode(datosFormulario.getContrasena().trim()));

        // si ya tienes estos campos en la entidad:
        nuevo.setNombre(datosFormulario.getNombre());
        nuevo.setApellido(datosFormulario.getApellido());
        nuevo.setDni(datosFormulario.getDni());
        nuevo.setTelefono(datosFormulario.getTelefono());
        nuevo.setRol("CLIENTE"); // o lo que uses por defecto
        nuevo.setEnabled(false);

        // Generar código de verificación
        String code = generarCodigo(6);
        String codeHash = passwordEncoder.encode(code);

        nuevo.setVerificationCodeHash(codeHash);
        nuevo.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));

        repo.save(nuevo);

        emailService.sendVerificationEmail(nuevo.getCorreo(), code);

        return nuevo;
    }

    // ==============================
    // VERIFICAR CUENTA POR CÓDIGO
    // ==============================
    public boolean verificarCuenta(String correo, String codeIngresado) {
        Usuarios usuario = repo.findByCorreo(correo.trim().toLowerCase());
        if (usuario == null) return false;

        if (usuario.getVerificationCodeExpiry() == null ||
            usuario.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        boolean ok = passwordEncoder.matches(codeIngresado.trim(), usuario.getVerificationCodeHash());
        if (!ok) return false;

        usuario.setEnabled(true);
        usuario.setVerificationCodeHash(null);
        usuario.setVerificationCodeExpiry(null);
        repo.save(usuario);
        return true;
    }

    // ==============================
    // OLVIDÉ MI CONTRASEÑA: ENVIAR CÓDIGO
    // ==============================
    public void enviarCodigoReset(String correo) {
        Usuarios usuario = repo.findByCorreo(correo.trim().toLowerCase());
        if (usuario == null || !usuario.isEnabled()) {
            // Por seguridad, no reveles si existe o no
            return;
        }

        String code = generarCodigo(6);
        String codeHash = passwordEncoder.encode(code);

        usuario.setResetCodeHash(codeHash);
        usuario.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
        repo.save(usuario);

        emailService.sendResetPasswordEmail(usuario.getCorreo(), code);
    }

    // ==============================
    // RESETEAR CONTRASEÑA CON CÓDIGO
    // ==============================
    public boolean resetearContrasena(String correo, String codeIngresado, String nuevaContrasena) {
        Usuarios usuario = repo.findByCorreo(correo.trim().toLowerCase());
        if (usuario == null) return false;

        if (usuario.getResetCodeExpiry() == null ||
            usuario.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        boolean ok = passwordEncoder.matches(codeIngresado.trim(), usuario.getResetCodeHash());
        if (!ok) return false;

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena.trim()));
        usuario.setResetCodeHash(null);
        usuario.setResetCodeExpiry(null);
        repo.save(usuario);
        return true;
    }

    // ==============================
    // UTILIDAD: GENERAR CÓDIGO
    // ==============================
    private String generarCodigo(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0-9
        }
        return sb.toString();
    }

    // ==============================
    // LO QUE YA TENÍAS
    // ==============================
    // Listar todos los usuarios (para admin / tabla)
    public List<Usuarios> listarUsuarios() {
        return repo.findAll();
    }

    // Listar usuarios como DTO (sin contraseña)
    public List<UsuarioDTO> listarUsuariosDTO() {
        return repo.findAll().stream()
                .map(u -> new UsuarioDTO(u.getCodigo(), u.getCorreo(), u.getRol()))
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> buscarPorCodigoDTO(Long codigo) {
        return repo.findById(codigo)
               .map(u -> new UsuarioDTO(u.getCodigo(), u.getCorreo(), u.getRol()));
    }

    // Si ya no usas el método guardar simple, puedes eliminarlo
    public void guardar(Usuarios usuario) {
        repo.save(usuario);
    }
}
