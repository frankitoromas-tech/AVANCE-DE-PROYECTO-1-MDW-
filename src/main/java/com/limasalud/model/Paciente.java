package com.limasalud.model;

public class Paciente {

    private Long id;
    private String nombre;
    private String dni;
    private Integer edad;
    private String parentesco;
    private String telefono;
    private String correo;

    public Paciente() {
    }

    public Paciente(Long id, String nombre, String dni, Integer edad, String parentesco, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.edad = edad;
        this.parentesco = parentesco;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
