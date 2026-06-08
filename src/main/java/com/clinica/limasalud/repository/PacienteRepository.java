package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.Paciente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDniIgnoreCase(String dni);

    boolean existsByDniIgnoreCaseAndIdNot(String dni, Long id);
}
