package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Usuarios;
import com.example.service.Usuarios_Servicio;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private Usuarios_Servicio usuariosServicio;

    // =========================
    // UTILIDAD
    // =========================
    private String correoTrim(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }

    // =========================
    // LOGIN
    // =========================

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // login.html
    }

    // Procesar login
    @PostMapping("/login")
    public String login(@RequestParam String correo,
                        @RequestParam String contrasena,
                        Model model,
                        HttpSession session) {

        Usuarios usuario = usuariosServicio.validarUsuario(correoTrim(correo), contrasena);

        if (usuario != null) {
            // Guardar en sesión
            session.setAttribute("usuario", usuario);

            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                return "redirect:/vistasadmi/dashboard";
            } else {
                return "redirect:/index";
            }
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos o cuenta no verificada");
            return "login";
        }
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // =========================
    // PÁGINAS BÁSICAS
    // =========================

    @GetMapping("/index")
    public String index() {
        return "index"; // index.html
    }

    // =========================
    // REGISTRO + VERIFICACIÓN
    // =========================

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String registro(Model modelo) {
        modelo.addAttribute("usuarios", new Usuarios());
        return "registro"; // registro.html
    }

    // Procesar registro + envío de código (sin redirect)
    @PostMapping("/registrarUsuario")
    public String registrarUsuario(Usuarios usuario, Model model) {

        try {
            // Normalizar correo
            usuario.setCorreo(correoTrim(usuario.getCorreo()));

            // Registrar: encripta, genera código, guarda, envía correo
            usuariosServicio.registrarConVerificacion(usuario);

            // Pasamos el correo a la vista de verificación
            model.addAttribute("correo", usuario.getCorreo());
            model.addAttribute("mensaje",
                    "Te enviamos un código a tu correo. Ingrésalo para activar tu cuenta.");

            return "verificar-cuenta"; // mostrar directamente la vista
        } catch (RuntimeException e) {
            // Puede ser "El correo ya está registrado" o nuestro mensaje de "ya registrado pero sin verificar"
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuarios", usuario);
            return "registro";
        }
    }

    // Mostrar formulario de verificación (por si entran manualmente)
    @GetMapping("/verificar-cuenta")
    public String mostrarVerificarCuenta(
            @RequestParam(name = "correo", required = false) String correo,
            Model model) {

        if (correo != null && !correo.isBlank()) {
            model.addAttribute("correo", correoTrim(correo));
        }
        return "verificar-cuenta"; // verificar-cuenta.html
    }

    // Procesar verificación
    @PostMapping("/verificar-cuenta")
    public String procesarVerificarCuenta(@RequestParam String correo,
                                          @RequestParam String code,
                                          Model model) {

        boolean ok = usuariosServicio.verificarCuenta(correoTrim(correo), code);

        if (ok) {
            model.addAttribute("mensaje", "Cuenta verificada correctamente. Ya puedes iniciar sesión.");
            return "login"; // mostramos directamente el login
        } else {
            model.addAttribute("error", "Código inválido o expirado.");
            model.addAttribute("correo", correoTrim(correo));
            return "verificar-cuenta";
        }
    }

    // =========================
    // OLVIDÉ MI CONTRASEÑA
    // =========================

    // 1) Mostrar formulario para ingresar correo
    @GetMapping("/olvido-password")
    public String mostrarOlvidoPassword() {
        return "olvido-password"; // olvido-password.html
    }

    // 2) Procesar envío del código de reseteo
    @PostMapping("/olvido-password")
    public String procesarOlvidoPassword(@RequestParam String correo,
                                         Model model) {

        String correoNormalizado = correoTrim(correo);

        // Envía el código si el usuario existe y está enabled (el servicio ya controla esto)
        usuariosServicio.enviarCodigoReset(correoNormalizado);

        // Independientemente de si existe o no, mostramos la vista para ingresar código y nueva contraseña
        model.addAttribute("correo", correoNormalizado);
        model.addAttribute("mensaje", "Si el correo existe y está activo, te enviamos un código de verificación.");

        // MUY IMPORTANTE: aquí ya NO volvemos a "olvido-password", sino a "cambiar-password"
        return "cambiar-password"; // cambiar-password.html
    }

    // 3) Mostrar formulario de cambio de contraseña (por si llegan con un link o algo)
    @GetMapping("/cambiar-password")
    public String mostrarCambiarPassword(@RequestParam(name = "correo", required = false) String correo,
                                         Model model) {
        if (correo != null && !correo.isBlank()) {
            model.addAttribute("correo", correoTrim(correo));
        }
        return "cambiar-password";
    }

    // 4) Procesar cambio de contraseña con código
    @PostMapping("/cambiar-password")
    public String procesarCambiarPassword(@RequestParam String correo,
                                          @RequestParam String code,
                                          @RequestParam String nuevaContrasena,
                                          Model model) {

        boolean ok = usuariosServicio.resetearContrasena(
                correoTrim(correo),
                code,
                nuevaContrasena
        );

        if (ok) {
            model.addAttribute("mensaje", "Contraseña actualizada correctamente. Ya puedes iniciar sesión.");
            return "login";
        } else {
            model.addAttribute("error", "Código inválido o expirado.");
            model.addAttribute("correo", correoTrim(correo));
            return "cambiar-password";
        }
    }
}

