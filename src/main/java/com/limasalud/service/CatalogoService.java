package com.limasalud.service;

import com.limasalud.model.Horario;
import com.limasalud.model.Medico;
import com.limasalud.model.Servicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService {

    private final List<Servicio> servicios = List.of(
            new Servicio("cardiologia", "Cardiología", "bi-heart-pulse", "/Imagenes/cardiologia.jpg"),
            new Servicio("pediatria", "Pediatría", "bi-person-hearts", "/Imagenes/pediatria.jpg"),
            new Servicio("traumatologia", "Traumatología", "bi-bandaid", "/Imagenes/traumatologia.jpg"),
            new Servicio("neurologia", "Neurología", "bi-brain", "/Imagenes/neurologia.jpg"));

    private final List<Medico> medicos = List.of(
            new Medico("med-001", "Dr. Pérez Rodríguez", "cardiologia", List.of(
                    new Horario("Lunes", "08:00", "Presencial"),
                    new Horario("Miércoles", "10:00", "Presencial"),
                    new Horario("Viernes", "14:00", "Presencial"))),
            new Medico("med-002", "Dra. López Vargas", "cardiologia", List.of(
                    new Horario("Martes", "09:00", "Presencial"),
                    new Horario("Jueves", "15:00", "Presencial"))),
            new Medico("med-003", "Dra. Torres Quispe", "pediatria", List.of(
                    new Horario("Lunes", "07:00", "Presencial"),
                    new Horario("Miércoles", "13:00", "Presencial"))),
            new Medico("med-004", "Dr. Mamani Flores", "pediatria", List.of(
                    new Horario("Martes", "08:00", "Telemedicina"),
                    new Horario("Viernes", "10:00", "Presencial"))),
            new Medico("med-005", "Dr. Cáceres Huanca", "traumatologia", List.of(
                    new Horario("Lunes", "09:00", "Presencial"),
                    new Horario("Jueves", "14:00", "Presencial"))),
            new Medico("med-006", "Dra. Salas Mendoza", "neurologia", List.of(
                    new Horario("Martes", "10:00", "Presencial"),
                    new Horario("Viernes", "08:00", "Telemedicina"))));

    public List<Servicio> listarServicios() {
        return servicios;
    }

    public Optional<Servicio> buscarServicioPorId(String servicioId) {
        return servicios.stream().filter(servicio -> servicio.getId().equalsIgnoreCase(servicioId)).findFirst();
    }

    public List<Medico> listarMedicosPorServicio(String servicioId) {
        return medicos.stream()
                .filter(medico -> medico.getServicioId().equalsIgnoreCase(servicioId))
                .toList();
    }

    public List<Medico> listarTodosLosMedicos() {
        return medicos;
    }

    public List<String> listarEspecialidades() {
        List<String> especialidades = new ArrayList<>();
        for (Servicio servicio : servicios) {
            especialidades.add(servicio.getNombre());
        }
        return especialidades;
    }
}
