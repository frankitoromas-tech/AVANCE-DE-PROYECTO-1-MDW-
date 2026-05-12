package com.limasalud.model;

public class Horario {

    private String dia;
    private String hora;
    private String modalidad;

    public Horario() {
    }

    public Horario(String dia, String hora, String modalidad) {
        this.dia = dia;
        this.hora = hora;
        this.modalidad = modalidad;
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
