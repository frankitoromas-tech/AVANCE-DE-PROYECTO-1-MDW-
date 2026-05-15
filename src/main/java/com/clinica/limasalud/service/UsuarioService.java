package com.clinica.limasalud.service;

import com.clinica.limasalud.model.Paciente;
import com.clinica.limasalud.model.PacienteForm;
import com.clinica.limasalud.model.RegistroUsuarioForm;
import com.clinica.limasalud.model.Rol;
import com.clinica.limasalud.model.Usuario;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;
    private final ClinicaService clinicaService;

    public UsuarioService(PasswordEncoder passwordEncoder, ClinicaService clinicaService) {
        this.passwordEncoder = passwordEncoder;
        this.clinicaService = clinicaService;
        inicializarUsuarios();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = buscarPorUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .authorities("ROLE_" + usuario.getRol().name())
                .build();
    }

    public Usuario registrarPaciente(RegistroUsuarioForm form) {
        String username = form.getUsername().trim().toLowerCase();
        if (usuarios.containsKey(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        PacienteForm pacienteForm = new PacienteForm();
        pacienteForm.setNombreCompleto(form.getNombreCompleto());
        pacienteForm.setDni(form.getDni());
        pacienteForm.setEdad(form.getEdad());
        pacienteForm.setParentesco(form.getParentesco() == null || form.getParentesco().isBlank() ? "Titular" : form.getParentesco());
        pacienteForm.setTelefono(form.getTelefono());
        pacienteForm.setCorreo(form.getCorreo());

        Paciente paciente = clinicaService.registrarPaciente(pacienteForm);
        Usuario usuario = new Usuario(
                username,
                passwordEncoder.encode(form.getPassword()),
                form.getNombreCompleto(),
                form.getCorreo(),
                Rol.PACIENTE,
                paciente.getId()
        );
        usuarios.put(username, usuario);
        return usuario;
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usuarios.get(username.toLowerCase()));
    }

    private void inicializarUsuarios() {
        usuarios.put("medico", new Usuario("medico", passwordEncoder.encode("Medico2026!"), "Medico Demo", "medico@limasalud.pe", Rol.MEDICO, null));
        usuarios.put("admin", new Usuario("admin", passwordEncoder.encode("Admin2026!"), "Administrador Demo", "admin@limasalud.pe", Rol.ADMIN, null));
        usuarios.put("paciente", new Usuario("paciente", passwordEncoder.encode("Paciente2026!"), "Juan Perez", "juan@mail.com", Rol.PACIENTE, 1L));
    }
}
