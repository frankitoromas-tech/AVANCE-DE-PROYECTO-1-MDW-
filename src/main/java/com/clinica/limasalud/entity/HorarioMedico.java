package com.clinica.limasalud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "horarios")
public class HorarioMedico {

    @Id
    @Column(length = 120)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private ServicioMedico servicio;

    @NotBlank
    @Column(name = "nombre_medico", nullable = false)
    private String nombreMedico;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String dia;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String hora;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String modalidad;

    public HorarioMedico() {
    }

    public HorarioMedico(String id, ServicioMedico servicio, String nombreMedico, String dia, String hora, String modalidad) {
        this.id = id;
        this.servicio = servicio;
        this.nombreMedico = nombreMedico;
        this.dia = dia;
        this.hora = hora;
        this.modalidad = modalidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServicioMedico getServicio() {
        return servicio;
    }

    public void setServicio(ServicioMedico servicio) {
        this.servicio = servicio;
    }

    public String getServicioId() {
        return servicio != null ? servicio.getId() : null;
    }

    public String getServicioNombre() {
        return servicio != null ? servicio.getNombre() : null;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
