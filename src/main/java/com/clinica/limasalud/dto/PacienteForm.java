package com.clinica.limasalud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PacienteForm {

    @NotBlank(message = "Ingrese el nombre completo")
    private String nombreCompleto;

    @NotBlank(message = "Ingrese el DNI")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 digitos")
    private String dni;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad debe ser valida")
    private int edad;

    @NotBlank(message = "Ingrese el parentesco")
    private String parentesco;

    @NotBlank(message = "Ingrese el telefono")
    private String telefono;

    @Email(message = "Ingrese un correo valido")
    @NotBlank(message = "Ingrese el correo")
    private String correo;

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
