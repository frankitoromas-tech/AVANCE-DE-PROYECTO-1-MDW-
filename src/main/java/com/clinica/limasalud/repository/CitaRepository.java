package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.Cita;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.horario h JOIN FETCH h.servicio WHERE c.paciente.id = :pacienteId ORDER BY c.id DESC")
    List<Cita> findByPacienteIdWithDetalle(Long pacienteId);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.horario h JOIN FETCH h.servicio ORDER BY h.dia, h.hora")
    List<Cita> findAllWithDetalle();

    long countByPaciente_Id(Long pacienteId);
}
