package com.clinica.limasalud.model;

public class HorarioMedico {

    private String id;
    private String servicioId;
    private String servicioNombre;
    private String nombreMedico;
    private String dia;
    private String hora;
    private String modalidad;

    public HorarioMedico() {
    }

    public HorarioMedico(String id, String servicioId, String servicioNombre, String nombreMedico, String dia, String hora, String modalidad) {
        this.id = id;
        this.servicioId = servicioId;
        this.servicioNombre = servicioNombre;
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

    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public String getServicioNombre() {
        return servicioNombre;
    }

    public void setServicioNombre(String servicioNombre) {
        this.servicioNombre = servicioNombre;
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
