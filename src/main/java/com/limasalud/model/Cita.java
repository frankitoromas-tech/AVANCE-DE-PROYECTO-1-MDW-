package com.limasalud.model;

public class Cita {

    private Long id;
    private String nombrePaciente;
    private String dniPaciente;
    private Integer edadPaciente;
    private String parentesco;
    private String telefonoPaciente;
    private String correoPaciente;
    private String servicioId;
    private String servicio;
    private String medico;
    private String dia;
    private String horaSlot;
    private String modalidad;

    public Cita() {
    }

    public Cita(Long id, String nombrePaciente, String dniPaciente, Integer edadPaciente, String parentesco,
            String telefonoPaciente, String correoPaciente, String servicioId, String servicio, String medico,
            String dia, String horaSlot, String modalidad) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.dniPaciente = dniPaciente;
        this.edadPaciente = edadPaciente;
        this.parentesco = parentesco;
        this.telefonoPaciente = telefonoPaciente;
        this.correoPaciente = correoPaciente;
        this.servicioId = servicioId;
        this.servicio = servicio;
        this.medico = medico;
        this.dia = dia;
        this.horaSlot = horaSlot;
        this.modalidad = modalidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public Integer getEdadPaciente() {
        return edadPaciente;
    }

    public void setEdadPaciente(Integer edadPaciente) {
        this.edadPaciente = edadPaciente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTelefonoPaciente() {
        return telefonoPaciente;
    }

    public void setTelefonoPaciente(String telefonoPaciente) {
        this.telefonoPaciente = telefonoPaciente;
    }

    public String getCorreoPaciente() {
        return correoPaciente;
    }

    public void setCorreoPaciente(String correoPaciente) {
        this.correoPaciente = correoPaciente;
    }

    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraSlot() {
        return horaSlot;
    }

    public void setHoraSlot(String horaSlot) {
        this.horaSlot = horaSlot;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
