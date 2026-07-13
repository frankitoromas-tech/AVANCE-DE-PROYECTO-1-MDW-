package com.clinica.limasalud.api.dto;

import com.clinica.limasalud.entity.Paciente;

/** Vista de un paciente para la API REST. */
public record PacienteResponse(
        Long id,
        String nombreCompleto,
        String dni,
        int edad,
        String telefono,
        String correo) {

    public static PacienteResponse desde(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getNombreCompleto(),
                paciente.getDni(),
                paciente.getEdad(),
                paciente.getTelefono(),
                paciente.getCorreo());
    }
}
