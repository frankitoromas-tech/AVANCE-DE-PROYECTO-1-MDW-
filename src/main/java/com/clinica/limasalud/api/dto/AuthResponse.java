package com.clinica.limasalud.api.dto;

/** Respuesta del login de la API: token JWT y datos del usuario autenticado. */
public record AuthResponse(
        String token,
        String tipo,
        String username,
        String rol,
        long expiraEnMs) {

    public AuthResponse(String token, String username, String rol, long expiraEnMs) {
        this(token, "Bearer", username, rol, expiraEnMs);
    }
}
