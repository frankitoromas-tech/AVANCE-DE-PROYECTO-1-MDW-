package com.clinica.limasalud.service;

import com.clinica.limasalud.entity.Cita;
import com.clinica.limasalud.dto.CitaForm;
import com.clinica.limasalud.entity.HorarioMedico;
import com.clinica.limasalud.entity.Paciente;
import com.clinica.limasalud.dto.PacienteForm;
import com.clinica.limasalud.dto.ServicioForm;
import com.clinica.limasalud.entity.ServicioMedico;
import com.clinica.limasalud.repository.CitaRepository;
import com.clinica.limasalud.repository.HorarioMedicoRepository;
import com.clinica.limasalud.repository.PacienteRepository;
import com.clinica.limasalud.repository.ServicioMedicoRepository;
import com.clinica.limasalud.repository.UsuarioRepository;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClinicaService {

    private final PacienteRepository pacienteRepository;
    private final ServicioMedicoRepository servicioRepository;
    private final HorarioMedicoRepository horarioRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    public ClinicaService(
            PacienteRepository pacienteRepository,
            ServicioMedicoRepository servicioRepository,
            HorarioMedicoRepository horarioRepository,
            CitaRepository citaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.pacienteRepository = pacienteRepository;
        this.servicioRepository = servicioRepository;
        this.horarioRepository = horarioRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll().stream()
                .sorted((a, b) -> a.getNombreCompleto().compareToIgnoreCase(b.getNombreCompleto()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServicioMedico> listarServicios() {
        return servicioRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public List<HorarioMedico> listarHorarios() {
        return horarioRepository.findAllWithServicio();
    }

    @Transactional(readOnly = true)
    public List<Cita> listarCitas() {
        return citaRepository.findAllWithDetalle();
    }

    @Transactional(readOnly = true)
    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteIdWithDetalle(pacienteId);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPacientePorDni(String dni) {
        return pacienteRepository.findByDniIgnoreCase(dni);
    }

    @Transactional(readOnly = true)
    public Optional<Cita> buscarCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ServicioMedico> buscarServicioPorId(String id) {
        return servicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<HorarioMedico> buscarHorarioPorId(String id) {
        return horarioRepository.findById(id);
    }

    public Paciente registrarPaciente(PacienteForm form) {
        Optional<Paciente> existente = buscarPacientePorDni(form.getDni());
        if (existente.isPresent()) {
            Paciente paciente = existente.get();
            paciente.setNombreCompleto(form.getNombreCompleto());
            paciente.setEdad(form.getEdad());
            paciente.setParentesco(form.getParentesco());
            paciente.setTelefono(form.getTelefono());
            paciente.setCorreo(form.getCorreo());
            return pacienteRepository.save(paciente);
        }

        Paciente paciente = new Paciente();
        paciente.setNombreCompleto(form.getNombreCompleto());
        paciente.setDni(form.getDni());
        paciente.setEdad(form.getEdad());
        paciente.setParentesco(form.getParentesco());
        paciente.setTelefono(form.getTelefono());
        paciente.setCorreo(form.getCorreo());
        return pacienteRepository.save(paciente);
    }

    public Paciente actualizarPaciente(Long id, PacienteForm form) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (pacienteRepository.existsByDniIgnoreCaseAndIdNot(form.getDni(), id)) {
            throw new IllegalArgumentException("Ya existe otro paciente con ese DNI");
        }

        paciente.setNombreCompleto(form.getNombreCompleto());
        paciente.setDni(form.getDni());
        paciente.setEdad(form.getEdad());
        paciente.setParentesco(form.getParentesco());
        paciente.setTelefono(form.getTelefono());
        paciente.setCorreo(form.getCorreo());
        return pacienteRepository.save(paciente);
    }

    public void eliminarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        if (citaRepository.countByPaciente_Id(id) > 0) {
            throw new IllegalArgumentException("No se puede eliminar: el paciente tiene citas registradas.");
        }
        if (usuarioRepository.existsByPaciente_Id(id)) {
            throw new IllegalArgumentException("No se puede eliminar: el paciente tiene un usuario asociado.");
        }
        pacienteRepository.deleteById(id);
    }

    public Cita registrarCita(Long pacienteId, String horarioId) {
        CitaForm form = new CitaForm();
        form.setPacienteId(pacienteId);
        form.setHorarioId(horarioId);
        form.setEstado("Programada");
        return guardarCita(form);
    }

    public Cita guardarCita(CitaForm form) {
        Paciente paciente = pacienteRepository.findById(form.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        HorarioMedico horario = horarioRepository.findById(form.getHorarioId())
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        Cita cita = form.getId() == null
                ? new Cita()
                : citaRepository.findById(form.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        cita.setPaciente(paciente);
        cita.setHorario(horario);
        cita.setEstado(form.getEstado());
        return citaRepository.save(cita);
    }

    public void cancelarCita(Long citaId) {
        if (!citaRepository.existsById(citaId)) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        citaRepository.deleteById(citaId);
    }

    public ServicioMedico registrarServicio(ServicioForm form) {
        String idBase = crearSlug(form.getNombre());
        String id = idBase;
        int repetido = 2;
        while (buscarServicioPorId(id).isPresent()) {
            id = idBase + "-" + repetido;
            repetido++;
        }

        String imagen = normalizarRutaImagen(form.getImagen());
        String icono = form.getIcono() == null || form.getIcono().isBlank()
                ? "bi-hospital"
                : form.getIcono().trim();

        ServicioMedico servicio = new ServicioMedico(
                id,
                form.getNombre().trim(),
                form.getDescripcion().trim(),
                icono,
                imagen,
                true
        );
        servicioRepository.save(servicio);

        HorarioMedico horario = new HorarioMedico(
                id + "-general-lunes-0800",
                servicio,
                "Dr. Especialista " + servicio.getNombre(),
                "Lunes",
                "08:00",
                "Presencial"
        );
        horarioRepository.save(horario);
        return servicio;
    }

    @Transactional(readOnly = true)
    public long totalPacientes() {
        return pacienteRepository.count();
    }

    @Transactional(readOnly = true)
    public long totalCitas() {
        return citaRepository.count();
    }

    private String crearSlug(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return normalizado.isBlank() ? "servicio" : normalizado;
    }

    private String normalizarRutaImagen(String imagen) {
        if (imagen == null || imagen.isBlank()) {
            return "/images/sesiones-clinicas.jpg";
        }
        String ruta = imagen.trim();
        if (ruta.startsWith("http://") || ruta.startsWith("https://") || ruta.startsWith("/")) {
            return ruta;
        }
        return "/" + ruta;
    }
}
