package com.limasalud.model;

import java.util.ArrayList;
import java.util.List;

public class Medico {

    private String id;
    private String nombre;
    private String servicioId;
    private List<Horario> horarios = new ArrayList<>();

    public Medico() {
    }

    public Medico(String id, String nombre, String servicioId, List<Horario> horarios) {
        this.id = id;
        this.nombre = nombre;
        this.servicioId = servicioId;
        this.horarios = new ArrayList<>(horarios);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = new ArrayList<>(horarios);
    }
}
