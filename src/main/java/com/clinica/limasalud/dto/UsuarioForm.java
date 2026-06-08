package com.clinica.limasalud.dto;

import com.clinica.limasalud.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioForm {

    private Long id;

    @NotBlank(message = "Ingrese un usuario")
    @Size(max = 50, message = "El usuario no puede superar 50 caracteres")
    private String username;

    @NotBlank(message = "Ingrese el nombre completo")
    private String nombreCompleto;

    @Email(message = "Ingrese un correo valido")
    @NotBlank(message = "Ingrese el correo")
    private String correo;

    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "Seleccione un rol")
    private Rol rol;

    private Long pacienteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
}
