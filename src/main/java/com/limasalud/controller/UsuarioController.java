package com.limasalud.controller;

import com.limasalud.model.LoginForm;
import com.limasalud.model.RegistroUsuarioForm;
import com.limasalud.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("registroForm", new RegistroUsuarioForm());
        return "usuario/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginForm loginForm, HttpSession session,
            RedirectAttributes redirectAttributes, Model model) {
        return usuarioService.autenticar(loginForm.getUsername(), loginForm.getPassword())
                .map(usuario -> {
                    session.setAttribute("usuarioMedico", usuario);
                    redirectAttributes.addFlashAttribute("toastMensaje", "¡Bienvenido, acceso concedido!");
                    return "redirect:/medico/agenda";
                })
                .orElseGet(() -> {
                    model.addAttribute("loginForm", loginForm);
                    model.addAttribute("registroForm", new RegistroUsuarioForm());
                    model.addAttribute("errorLogin", "Usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");
                    return "usuario/login";
                });
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute RegistroUsuarioForm registroForm, RedirectAttributes redirectAttributes) {
        usuarioService.registrar(registroForm);
        redirectAttributes.addFlashAttribute("toastMensaje", "Registro completado con éxito.");
        redirectAttributes.addFlashAttribute("mostrarRegistro", true);
        return "redirect:/usuarios/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
