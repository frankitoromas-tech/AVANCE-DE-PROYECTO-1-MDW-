package com.clinica.limasalud.api.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciales enviadas al endpoint de login de la API para obtener un JWT. */
public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio") String username,
        @NotBlank(message = "La contrasena es obligatoria") String password) {
}
