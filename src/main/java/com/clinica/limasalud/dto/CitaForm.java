package com.clinica.limasalud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CitaForm {

    private Long id;

    @NotNull(message = "Seleccione un paciente")
    private Long pacienteId;

    @NotBlank(message = "Seleccione un horario")
    private String horarioId;

    @NotBlank(message = "Ingrese el estado de la cita")
    private String estado = "Programada";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(String horarioId) {
        this.horarioId = horarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
