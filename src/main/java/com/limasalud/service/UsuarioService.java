package com.limasalud.service;

import com.limasalud.model.RegistroUsuarioForm;
import com.limasalud.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>(List.of(
            new Usuario("admin", "1234", "MEDICO", "Administrador Médico"),
            new Usuario("JGUERRA", "1234", "MEDICO", "Dr. José Guerra")));

    public Optional<Usuario> autenticar(String username, String password) {
        return usuarios.stream()
                .filter(usuario -> usuario.getUsername().equals(username) && usuario.getPassword().equals(password))
                .findFirst();
    }

    public Usuario registrar(RegistroUsuarioForm form) {
        String usernameGenerado = form.getCorreo().split("@")[0];
        Usuario usuario = new Usuario(usernameGenerado, form.getPassword(), "MEDICO", form.getNombreCompleto());
        usuarios.add(usuario);
        return usuario;
    }
}
