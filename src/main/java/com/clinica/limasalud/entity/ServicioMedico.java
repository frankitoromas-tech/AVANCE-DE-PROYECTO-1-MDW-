package com.clinica.limasalud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
public class ServicioMedico {

    @Id
    @Column(length = 80)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String icono;

    @NotBlank
    @Column(nullable = false)
    private String imagen;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "servicio", fetch = FetchType.LAZY)
    private List<HorarioMedico> horarios = new ArrayList<>();

    public ServicioMedico() {
    }

    public ServicioMedico(String id, String nombre, String descripcion, String icono, String imagen, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.imagen = imagen;
        this.activo = activo;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<HorarioMedico> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioMedico> horarios) {
        this.horarios = horarios;
    }
}
