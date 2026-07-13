package com.clinica.limasalud.api.dto;

import com.clinica.limasalud.entity.Cita;

/** Vista de una cita para la API REST (evita exponer la entidad JPA directamente). */
public record CitaResponse(
        Long id,
        String paciente,
        String servicio,
        String medico,
        String dia,
        String hora,
        String modalidad,
        String estado) {

    public static CitaResponse desde(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getPacienteNombre(),
                cita.getServicioNombre(),
                cita.getMedico(),
                cita.getDia(),
                cita.getHora(),
                cita.getModalidad(),
                cita.getEstado());
    }
}
