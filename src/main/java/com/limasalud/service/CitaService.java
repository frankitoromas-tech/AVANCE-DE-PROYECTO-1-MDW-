package com.limasalud.service;

import com.limasalud.model.Cita;
import com.limasalud.model.CitaForm;
import com.limasalud.model.Paciente;
import com.limasalud.model.PacienteForm;
import com.limasalud.model.Servicio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

    private final AtomicLong secuencia = new AtomicLong(0);
    private final List<Cita> citas = new ArrayList<>();

    private final CatalogoService catalogoService;
    private final PacienteService pacienteService;

    public CitaService(CatalogoService catalogoService, PacienteService pacienteService) {
        this.catalogoService = catalogoService;
        this.pacienteService = pacienteService;
    }

    public List<Cita> listarCitas() {
        return citas.stream()
                .sorted(Comparator.comparing(Cita::getId).reversed())
                .toList();
    }

    public Optional<Cita> buscarPorId(Long id) {
        return citas.stream().filter(cita -> cita.getId().equals(id)).findFirst();
    }

    public Cita registrarDesdePortalPaciente(CitaForm form) {
        Servicio servicio = catalogoService.buscarServicioPorId(form.getServicioId()).orElseThrow();
        Cita cita = new Cita(
                secuencia.incrementAndGet(),
                form.getNombre(),
                form.getDni(),
                form.getEdad(),
                form.getParentesco(),
                form.getTelefono(),
                form.getCorreo(),
                form.getServicioId(),
                servicio.getNombre(),
                form.getMedico(),
                form.getDia(),
                form.getHoraSlot(),
                form.getModalidad());
        citas.add(cita);

        pacienteService.buscarPorDni(form.getDni()).orElseGet(() -> pacienteService.registrarPaciente(aPacienteForm(form)));
        return cita;
    }

    public Cita registrarDesdePortalMedico(Long pacienteId, String servicioId, String fechaHora) {
        Paciente paciente = pacienteService.listarPacientes().stream()
                .filter(item -> item.getId().equals(pacienteId))
                .findFirst()
                .orElseThrow();
        Servicio servicio = catalogoService.buscarServicioPorId(servicioId).orElseThrow();

        Cita cita = new Cita(
                secuencia.incrementAndGet(),
                paciente.getNombre(),
                paciente.getDni(),
                paciente.getEdad(),
                paciente.getParentesco(),
                paciente.getTelefono(),
                paciente.getCorreo(),
                servicio.getId(),
                servicio.getNombre(),
                "Asignación médica",
                extraerDia(fechaHora),
                extraerHora(fechaHora),
                "Presencial");
        citas.add(cita);
        return cita;
    }

    public boolean eliminar(Long id) {
        return citas.removeIf(cita -> cita.getId().equals(id));
    }

    private PacienteForm aPacienteForm(CitaForm form) {
        PacienteForm pacienteForm = new PacienteForm();
        pacienteForm.setNombre(form.getNombre());
        pacienteForm.setDni(form.getDni());
        pacienteForm.setEdad(form.getEdad());
        pacienteForm.setParentesco(form.getParentesco());
        pacienteForm.setTelefono(form.getTelefono());
        pacienteForm.setCorreo(form.getCorreo());
        return pacienteForm;
    }

    private String extraerDia(String fechaHora) {
        if (fechaHora == null || fechaHora.isBlank() || !fechaHora.contains("T")) {
            return "Por definir";
        }
        return fechaHora.split("T")[0];
    }

    private String extraerHora(String fechaHora) {
        if (fechaHora == null || fechaHora.isBlank() || !fechaHora.contains("T")) {
            return "Por definir";
        }
        return fechaHora.split("T")[1];
    }
}
