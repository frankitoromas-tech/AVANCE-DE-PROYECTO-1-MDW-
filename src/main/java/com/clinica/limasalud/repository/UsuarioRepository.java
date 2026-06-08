package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);

    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);

    boolean existsByPaciente_Id(Long pacienteId);
}
