package com.clinica.limasalud.dto;

import jakarta.validation.constraints.NotBlank;

public class ServicioForm {

    @NotBlank(message = "Ingrese el nombre del servicio")
    private String nombre;

    @NotBlank(message = "Ingrese una descripcion")
    private String descripcion;

    private String icono = "bi-hospital";
    private String imagen = "/images/sesiones-clinicas.jpg";

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
