package com.clinica.limasalud.service;

import com.clinica.limasalud.model.Cita;
import com.clinica.limasalud.model.HorarioMedico;
import com.clinica.limasalud.model.Paciente;
import com.clinica.limasalud.model.PacienteForm;
import com.clinica.limasalud.model.ServicioForm;
import com.clinica.limasalud.model.ServicioMedico;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ClinicaService {

    private final AtomicLong pacienteSecuencia = new AtomicLong(1);
    private final AtomicLong citaSecuencia = new AtomicLong(1);
    private final List<Paciente> pacientes = new CopyOnWriteArrayList<>();
    private final List<ServicioMedico> servicios = new CopyOnWriteArrayList<>();
    private final List<HorarioMedico> horarios = new CopyOnWriteArrayList<>();
    private final List<Cita> citas = new CopyOnWriteArrayList<>();

    public ClinicaService() {
        inicializarServicios();
        inicializarPacientesYCitas();
    }

    public List<Paciente> listarPacientes() {
        return pacientes.stream()
                .sorted(Comparator.comparing(Paciente::getNombreCompleto))
                .toList();
    }

    public List<ServicioMedico> listarServicios() {
        return servicios.stream()
                .filter(ServicioMedico::isActivo)
                .sorted(Comparator.comparing(ServicioMedico::getNombre))
                .toList();
    }

    public List<HorarioMedico> listarHorarios() {
        return horarios.stream()
                .sorted(Comparator.comparing(HorarioMedico::getServicioNombre)
                        .thenComparing(HorarioMedico::getDia)
                        .thenComparing(HorarioMedico::getHora))
                .toList();
    }

    public List<Cita> listarCitas() {
        return citas.stream()
                .sorted(Comparator.comparing(Cita::getDia).thenComparing(Cita::getHora))
                .toList();
    }

    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return listarCitas().stream()
                .filter(cita -> cita.getPacienteId().equals(pacienteId))
                .toList();
    }

    public Optional<Paciente> buscarPacientePorId(Long id) {
        return pacientes.stream()
                .filter(paciente -> paciente.getId().equals(id))
                .findFirst();
    }

    public Optional<Paciente> buscarPacientePorDni(String dni) {
        return pacientes.stream()
                .filter(paciente -> paciente.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    public Optional<Cita> buscarCitaPorId(Long id) {
        return citas.stream()
                .filter(cita -> cita.getId().equals(id))
                .findFirst();
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
            return paciente;
        }

        Paciente paciente = new Paciente(
                pacienteSecuencia.getAndIncrement(),
                form.getNombreCompleto(),
                form.getDni(),
                form.getEdad(),
                form.getParentesco(),
                form.getTelefono(),
                form.getCorreo()
        );
        pacientes.add(paciente);
        return paciente;
    }

    public Cita registrarCita(Long pacienteId, String horarioId) {
        Paciente paciente = buscarPacientePorId(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        HorarioMedico horario = buscarHorarioPorId(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        Cita cita = new Cita();
        cita.setId(citaSecuencia.getAndIncrement());
        cita.setPacienteId(paciente.getId());
        cita.setPacienteNombre(paciente.getNombreCompleto());
        cita.setDni(paciente.getDni());
        cita.setEdad(paciente.getEdad());
        cita.setParentesco(paciente.getParentesco());
        cita.setTelefono(paciente.getTelefono());
        cita.setCorreo(paciente.getCorreo());
        cita.setServicioId(horario.getServicioId());
        cita.setServicioNombre(horario.getServicioNombre());
        cita.setMedico(horario.getNombreMedico());
        cita.setDia(horario.getDia());
        cita.setHora(horario.getHora());
        cita.setModalidad(horario.getModalidad());
        cita.setEstado("Programada");
        citas.add(cita);
        return cita;
    }

    public void cancelarCita(Long citaId) {
        citas.removeIf(cita -> cita.getId().equals(citaId));
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
        servicios.add(servicio);
        horarios.add(new HorarioMedico(
                id + "-general-lunes-0800",
                id,
                servicio.getNombre(),
                "Dr. Especialista " + servicio.getNombre(),
                "Lunes",
                "08:00",
                "Presencial"
        ));
        return servicio;
    }

    public long totalPacientes() {
        return pacientes.size();
    }

    public long totalCitas() {
        return citas.size();
    }

    public Optional<ServicioMedico> buscarServicioPorId(String id) {
        return servicios.stream()
                .filter(servicio -> servicio.getId().equals(id))
                .findFirst();
    }

    public Optional<HorarioMedico> buscarHorarioPorId(String id) {
        return horarios.stream()
                .filter(horario -> horario.getId().equals(id))
                .findFirst();
    }

    private void inicializarServicios() {
        servicios.add(new ServicioMedico("cardiologia", "Cardiologia", "Prevencion, diagnostico y control de enfermedades cardiovasculares.", "bi-heart-pulse", "/Imagenes/cardiologia.jpg", true));
        servicios.add(new ServicioMedico("pediatria", "Pediatria", "Atencion integral para ninos y adolescentes.", "bi-person-hearts", "/Imagenes/pediatria.jpg", true));
        servicios.add(new ServicioMedico("traumatologia", "Traumatologia", "Evaluacion y recuperacion de lesiones oseas, musculares y articulares.", "bi-bandaid", "/Imagenes/traumatologia.jpg", true));
        servicios.add(new ServicioMedico("neurologia", "Neurologia", "Atencion especializada del sistema nervioso y dolores neurologicos.", "bi-activity", "/Imagenes/neurologia.jpg", true));

        horarios.addAll(List.of(
                new HorarioMedico("cardio-perez-lunes-0800", "cardiologia", "Cardiologia", "Dr. Perez Rodriguez", "Lunes", "08:00", "Presencial"),
                new HorarioMedico("cardio-perez-miercoles-1000", "cardiologia", "Cardiologia", "Dr. Perez Rodriguez", "Miercoles", "10:00", "Presencial"),
                new HorarioMedico("cardio-lopez-jueves-1500", "cardiologia", "Cardiologia", "Dra. Lopez Vargas", "Jueves", "15:00", "Presencial"),
                new HorarioMedico("pedia-torres-lunes-0700", "pediatria", "Pediatria", "Dra. Torres Quispe", "Lunes", "07:00", "Presencial"),
                new HorarioMedico("pedia-mamani-viernes-1000", "pediatria", "Pediatria", "Dr. Mamani Flores", "Viernes", "10:00", "Telemedicina"),
                new HorarioMedico("trauma-caceres-lunes-0900", "traumatologia", "Traumatologia", "Dr. Caceres Huanca", "Lunes", "09:00", "Presencial"),
                new HorarioMedico("neuro-salas-martes-1000", "neurologia", "Neurologia", "Dra. Salas Mendoza", "Martes", "10:00", "Presencial"),
                new HorarioMedico("neuro-salas-viernes-0800", "neurologia", "Neurologia", "Dra. Salas Mendoza", "Viernes", "08:00", "Telemedicina")
        ));
    }

    private void inicializarPacientesYCitas() {
        List<PacienteForm> registros = new ArrayList<>();
        registros.add(crearPacienteForm("Juan Perez", "72635441", 35, "Titular", "987654321", "juan@mail.com"));
        registros.add(crearPacienteForm("Maria Lopez", "10293847", 29, "Titular", "912345678", "maria@mail.com"));
        registros.add(crearPacienteForm("Ricardo Soto", "44556677", 48, "Titular", "999888777", "soto@mail.com"));
        registros.add(crearPacienteForm("Ana Torres", "88776655", 41, "Titular", "954123987", "ana@mail.com"));
        registros.forEach(this::registrarPaciente);

        registrarCita(1L, "cardio-perez-lunes-0800");
        registrarCita(2L, "pedia-torres-lunes-0700");
        registrarCita(3L, "trauma-caceres-lunes-0900");
        registrarCita(4L, "neuro-salas-martes-1000");
    }

    private PacienteForm crearPacienteForm(String nombre, String dni, int edad, String parentesco, String telefono, String correo) {
        PacienteForm form = new PacienteForm();
        form.setNombreCompleto(nombre);
        form.setDni(dni);
        form.setEdad(edad);
        form.setParentesco(parentesco);
        form.setTelefono(telefono);
        form.setCorreo(correo);
        return form;
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
            return "/Imagenes/sesiones-clinicas.jpg";
        }
        String ruta = imagen.trim();
        if (ruta.startsWith("http://") || ruta.startsWith("https://") || ruta.startsWith("/")) {
            return ruta;
        }
        return "/" + ruta;
    }
}
