package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.HorarioMedico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, String> {

    @Query("SELECT h FROM HorarioMedico h JOIN FETCH h.servicio ORDER BY h.servicio.nombre, h.dia, h.hora")
    List<HorarioMedico> findAllWithServicio();
}
