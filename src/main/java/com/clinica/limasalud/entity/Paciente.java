package com.clinica.limasalud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @NotBlank
    @Size(min = 8, max = 8)
    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @NotNull
    @Min(0)
    @Max(120)
    @Column(nullable = false)
    private Integer edad;

    @NotBlank
    @Column(nullable = false)
    private String parentesco;

    @NotBlank
    @Column(nullable = false, length = 15)
    private String telefono;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String correo;

    public Paciente() {
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
        return edad == null ? 0 : edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Integer getEdadValue() {
        return edad;
    }

    public void setEdadValue(Integer edad) {
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
