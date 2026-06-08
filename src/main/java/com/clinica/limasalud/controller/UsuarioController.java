package com.clinica.limasalud.controller;

import com.clinica.limasalud.dto.RegistroUsuarioForm;
import com.clinica.limasalud.service.ClinicaService;
import com.clinica.limasalud.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;

    public UsuarioController(UsuarioService usuarioService, ClinicaService clinicaService) {
        this.usuarioService = usuarioService;
        this.clinicaService = clinicaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("servicios", clinicaService.listarServicios());
        model.addAttribute("totalPacientes", clinicaService.totalPacientes());
        model.addAttribute("totalCitas", clinicaService.totalCitas());
        model.addAttribute("totalServicios", clinicaService.listarServicios().size());
        return "public/index";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model
    ) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroUsuarioForm());
        }
        model.addAttribute("mostrarRegistro", model.containsAttribute("mostrarRegistro"));
        model.addAttribute("errorLogin", error != null);
        model.addAttribute("logoutOk", logout != null);
        return "auth/login";
    }

    @PostMapping("/usuarios/registrar")
    public String registrarPaciente(
            @Valid @ModelAttribute("registroForm") RegistroUsuarioForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registroForm", bindingResult);
            redirectAttributes.addFlashAttribute("registroForm", form);
            redirectAttributes.addFlashAttribute("mostrarRegistro", true);
            return "redirect:/login";
        }

        try {
            usuarioService.registrarPaciente(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Registro completado. Ya puedes iniciar sesion.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            redirectAttributes.addFlashAttribute("registroForm", form);
            redirectAttributes.addFlashAttribute("mostrarRegistro", true);
        }
        return "redirect:/login";
    }

    @GetMapping("/redirigir")
    public String redirigirPorRol(Authentication authentication) {
        boolean esMedicoOAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(rol -> rol.equals("ROLE_MEDICO") || rol.equals("ROLE_ADMIN"));
        return esMedicoOAdmin ? "redirect:/medico/agenda" : "redirect:/portal-paciente";
    }
}
