package com.clinica.limasalud.repository;

import com.clinica.limasalud.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    /** Carga los usuarios junto con su paciente asociado (evita LazyInitializationException en la vista). */
    @Query("select u from Usuario u left join fetch u.paciente")
    List<Usuario> findAllWithPaciente();

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);

    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);

    boolean existsByPaciente_Id(Long pacienteId);
}
