package com.limasalud.model;

public class Servicio {

    private String id;
    private String nombre;
    private String icono;
    private String imagen;

    public Servicio() {
    }

    public Servicio(String id, String nombre, String icono, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.imagen = imagen;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
