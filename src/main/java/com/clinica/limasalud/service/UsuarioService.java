package com.clinica.limasalud.service;

import com.clinica.limasalud.entity.Paciente;
import com.clinica.limasalud.dto.PacienteForm;
import com.clinica.limasalud.dto.RegistroUsuarioForm;
import com.clinica.limasalud.entity.Rol;
import com.clinica.limasalud.entity.Usuario;
import com.clinica.limasalud.dto.UsuarioForm;
import com.clinica.limasalud.repository.PacienteRepository;
import com.clinica.limasalud.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClinicaService clinicaService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PacienteRepository pacienteRepository,
            PasswordEncoder passwordEncoder,
            ClinicaService clinicaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.clinicaService = clinicaService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = buscarPorUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .authorities("ROLE_" + usuario.getRol().name())
                .build();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAllWithPaciente();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario registrarPaciente(RegistroUsuarioForm form) {
        String username = form.getUsername().trim().toLowerCase();
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(form.getCorreo())) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        PacienteForm pacienteForm = new PacienteForm();
        pacienteForm.setNombreCompleto(form.getNombreCompleto());
        pacienteForm.setDni(form.getDni());
        pacienteForm.setEdad(form.getEdad());
        pacienteForm.setParentesco(form.getParentesco() == null || form.getParentesco().isBlank() ? "Titular" : form.getParentesco());
        pacienteForm.setTelefono(form.getTelefono());
        pacienteForm.setCorreo(form.getCorreo());

        Paciente paciente = clinicaService.registrarPaciente(pacienteForm);

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        usuario.setNombreCompleto(form.getNombreCompleto());
        usuario.setCorreo(form.getCorreo());
        usuario.setRol(Rol.PACIENTE);
        usuario.setPaciente(paciente);
        return usuarioRepository.save(usuario);
    }

    public Usuario crearUsuario(UsuarioForm form) {
        validarUsuarioNuevo(form);

        Usuario usuario = new Usuario();
        aplicarFormulario(usuario, form, true);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioForm form) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        validarUsuarioExistente(form, id);
        aplicarFormulario(usuario, form, false);
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return usuarioRepository.findByUsernameIgnoreCase(username.trim());
    }

    private void validarUsuarioNuevo(UsuarioForm form) {
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }
        if (usuarioRepository.existsByUsernameIgnoreCase(form.getUsername().trim().toLowerCase())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(form.getCorreo())) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }
    }

    private void validarUsuarioExistente(UsuarioForm form, Long id) {
        if (usuarioRepository.existsByUsernameIgnoreCaseAndIdNot(form.getUsername().trim().toLowerCase(), id)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(form.getCorreo(), id)) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }
    }

    private void aplicarFormulario(Usuario usuario, UsuarioForm form, boolean esNuevo) {
        usuario.setUsername(form.getUsername().trim().toLowerCase());
        usuario.setNombreCompleto(form.getNombreCompleto().trim());
        usuario.setCorreo(form.getCorreo().trim());
        usuario.setRol(form.getRol());

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        } else if (esNuevo) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }

        if (form.getRol() == Rol.PACIENTE && form.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(form.getPacienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
            usuario.setPaciente(paciente);
        } else {
            usuario.setPaciente(null);
        }
    }
}
