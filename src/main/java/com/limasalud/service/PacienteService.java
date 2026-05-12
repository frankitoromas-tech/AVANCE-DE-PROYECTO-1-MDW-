package com.limasalud.service;

import com.limasalud.model.Paciente;
import com.limasalud.model.PacienteForm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final AtomicLong secuencia = new AtomicLong(4);
    private final List<Paciente> pacientes = new ArrayList<>(List.of(
            new Paciente(1L, "Juan Pérez", "72635441", 35, "Titular", "987 654 321", "juan@mail.com"),
            new Paciente(2L, "María López", "10293847", 29, "Titular", "912 345 678", "maria@mail.com"),
            new Paciente(3L, "Ricardo Soto", "44556677", 41, "Titular", "999 888 777", "soto@mail.com"),
            new Paciente(4L, "Ana Torres", "88776655", 33, "Titular", "954 123 987", "ana@mail.com")));

    public List<Paciente> listarPacientes() {
        return pacientes.stream()
                .sorted(Comparator.comparing(Paciente::getNombre))
                .toList();
    }

    public Paciente registrarPaciente(PacienteForm form) {
        Paciente paciente = new Paciente(
                secuencia.incrementAndGet(),
                form.getNombre(),
                form.getDni(),
                form.getEdad(),
                form.getParentesco(),
                form.getTelefono(),
                form.getCorreo());
        pacientes.add(paciente);
        return paciente;
    }

    public Optional<Paciente> buscarPorDni(String dni) {
        return pacientes.stream().filter(p -> p.getDni().equalsIgnoreCase(dni)).findFirst();
    }
}
