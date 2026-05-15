package com.clinica.limasalud.model;

public class Paciente {

    private Long id;
    private String nombreCompleto;
    private String dni;
    private int edad;
    private String parentesco;
    private String telefono;
    private String correo;

    public Paciente() {
    }

    public Paciente(Long id, String nombreCompleto, String dni, int edad, String parentesco, String telefono, String correo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
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
