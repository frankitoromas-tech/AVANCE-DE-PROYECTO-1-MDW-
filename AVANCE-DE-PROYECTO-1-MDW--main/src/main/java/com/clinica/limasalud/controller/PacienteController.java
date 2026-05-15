package com.clinica.limasalud.controller;

import com.clinica.limasalud.model.Paciente;
import com.clinica.limasalud.model.PacienteForm;
import com.clinica.limasalud.model.ServicioForm;
import com.clinica.limasalud.model.Usuario;
import com.clinica.limasalud.service.ClinicaService;
import com.clinica.limasalud.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PacienteController {

    private final ClinicaService clinicaService;
    private final UsuarioService usuarioService;

    public PacienteController(ClinicaService clinicaService, UsuarioService usuarioService) {
        this.clinicaService = clinicaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/portal-paciente")
    public String portalPaciente(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.buscarPorUsername(authentication.getName());
        if (usuario.isEmpty() || usuario.get().getPacienteId() == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Tu usuario no tiene un perfil de paciente asociado.");
            return "redirect:/login";
        }

        Paciente paciente = clinicaService.buscarPacientePorId(usuario.get().getPacienteId())
                .orElseThrow(() -> new IllegalStateException("Paciente asociado no encontrado"));
        model.addAttribute("paciente", paciente);
        model.addAttribute("servicios", clinicaService.listarServicios());
        model.addAttribute("horarios", clinicaService.listarHorarios());
        model.addAttribute("citas", clinicaService.listarCitasPorPaciente(paciente.getId()));
        return "portal-paciente";
    }

    @PostMapping("/citas")
    public String registrarCitaPaciente(
            @RequestParam("horarioId") String horarioId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = usuarioService.buscarPorUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        if (usuario.getPacienteId() == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Solo un paciente puede registrar citas desde este portal.");
            return "redirect:/portal-paciente";
        }

        try {
            clinicaService.registrarCita(usuario.getPacienteId(), horarioId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cita registrada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/portal-paciente";
    }

    @PostMapping("/citas/{id}/cancelar")
    public String cancelarCita(
            @PathVariable("id") Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (!puedeGestionarCita(id, authentication)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para cancelar esta cita.");
            return "redirect:/portal-paciente";
        }

        clinicaService.cancelarCita(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cita cancelada correctamente.");
        return esPersonalClinico(authentication) ? "redirect:/medico/agenda" : "redirect:/portal-paciente";
    }

    @GetMapping("/medico/agenda")
    public String agendaMedico(Model model) {
        if (!model.containsAttribute("pacienteForm")) {
            model.addAttribute("pacienteForm", new PacienteForm());
        }
        if (!model.containsAttribute("servicioForm")) {
            model.addAttribute("servicioForm", new ServicioForm());
        }
        model.addAttribute("pacientes", clinicaService.listarPacientes());
        model.addAttribute("servicios", clinicaService.listarServicios());
        model.addAttribute("horarios", clinicaService.listarHorarios());
        model.addAttribute("citas", clinicaService.listarCitas());
        model.addAttribute("totalPacientes", clinicaService.totalPacientes());
        model.addAttribute("totalCitas", clinicaService.totalCitas());
        return "agenda-medico";
    }

    @PostMapping("/pacientes")
    public String registrarPaciente(
            @Valid @ModelAttribute("pacienteForm") PacienteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pacienteForm", bindingResult);
            redirectAttributes.addFlashAttribute("pacienteForm", form);
            redirectAttributes.addFlashAttribute("mensajeError", "Revisa los datos del paciente.");
            return "redirect:/medico/agenda";
        }

        clinicaService.registrarPaciente(form);
        redirectAttributes.addFlashAttribute("mensajeExito", "Paciente registrado correctamente.");
        return "redirect:/medico/agenda";
    }

    @PostMapping("/medico/citas")
    public String registrarCitaMedico(
            @RequestParam("pacienteId") Long pacienteId,
            @RequestParam("horarioId") String horarioId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            clinicaService.registrarCita(pacienteId, horarioId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cita medica registrada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/medico/agenda";
    }

    @PostMapping("/servicios")
    public String registrarServicio(
            @Valid @ModelAttribute("servicioForm") ServicioForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.servicioForm", bindingResult);
            redirectAttributes.addFlashAttribute("servicioForm", form);
            redirectAttributes.addFlashAttribute("mensajeError", "Revisa los datos del servicio.");
            return "redirect:/medico/agenda";
        }

        clinicaService.registrarServicio(form);
        redirectAttributes.addFlashAttribute("mensajeExito", "Servicio agregado correctamente.");
        return "redirect:/medico/agenda";
    }

    private boolean puedeGestionarCita(Long citaId, Authentication authentication) {
        if (esPersonalClinico(authentication)) {
            return true;
        }
        Optional<Usuario> usuario = usuarioService.buscarPorUsername(authentication.getName());
        return usuario.flatMap(u -> clinicaService.buscarCitaPorId(citaId)
                        .filter(cita -> u.getPacienteId() != null && cita.getPacienteId().equals(u.getPacienteId())))
                .isPresent();
    }

    private boolean esPersonalClinico(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(rol -> rol.equals("ROLE_MEDICO") || rol.equals("ROLE_ADMIN"));
    }
}
