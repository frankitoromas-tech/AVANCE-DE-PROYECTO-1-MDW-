package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.ServicioMedico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioMedicoRepository extends JpaRepository<ServicioMedico, String> {

    List<ServicioMedico> findByActivoTrueOrderByNombreAsc();
}
