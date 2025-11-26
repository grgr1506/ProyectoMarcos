package com.example.model;

public class UsuarioDTO {
    private Long codigo;
    private String correo;
    private String rol;

    public UsuarioDTO() {}

    public UsuarioDTO(Long codigo, String correo, String rol) {
        this.codigo = codigo;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

