package com.clinica.limasalud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "horario_id", nullable = false)
    private HorarioMedico horario;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String estado;

    public Cita() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public HorarioMedico getHorario() {
        return horario;
    }

    public void setHorario(HorarioMedico horario) {
        this.horario = horario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getPacienteId() {
        return paciente != null ? paciente.getId() : null;
    }

    public String getPacienteNombre() {
        return paciente != null ? paciente.getNombreCompleto() : null;
    }

    public String getDni() {
        return paciente != null ? paciente.getDni() : null;
    }

    public int getEdad() {
        return paciente != null ? paciente.getEdad() : 0;
    }

    public String getParentesco() {
        return paciente != null ? paciente.getParentesco() : null;
    }

    public String getTelefono() {
        return paciente != null ? paciente.getTelefono() : null;
    }

    public String getCorreo() {
        return paciente != null ? paciente.getCorreo() : null;
    }

    public String getServicioId() {
        return horario != null ? horario.getServicioId() : null;
    }

    public String getServicioNombre() {
        return horario != null ? horario.getServicioNombre() : null;
    }

    public String getMedico() {
        return horario != null ? horario.getNombreMedico() : null;
    }

    public String getDia() {
        return horario != null ? horario.getDia() : null;
    }

    public String getHora() {
        return horario != null ? horario.getHora() : null;
    }

    public String getModalidad() {
        return horario != null ? horario.getModalidad() : null;
    }
}
