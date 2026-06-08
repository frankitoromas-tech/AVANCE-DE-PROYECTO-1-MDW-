package com.clinica.limasalud.controller;

import com.clinica.limasalud.dto.CitaForm;
import com.clinica.limasalud.entity.Paciente;
import com.clinica.limasalud.dto.PacienteForm;
import com.clinica.limasalud.dto.ServicioForm;
import com.clinica.limasalud.entity.Usuario;
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
        return "paciente/portal";
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

        try {
            clinicaService.cancelarCita(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cita cancelada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
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
        if (!model.containsAttribute("citaForm")) {
            model.addAttribute("citaForm", new CitaForm());
        }
        model.addAttribute("pacientes", clinicaService.listarPacientes());
        model.addAttribute("servicios", clinicaService.listarServicios());
        model.addAttribute("horarios", clinicaService.listarHorarios());
        model.addAttribute("citas", clinicaService.listarCitas());
        model.addAttribute("totalPacientes", clinicaService.totalPacientes());
        model.addAttribute("totalCitas", clinicaService.totalCitas());
        return "medico/agenda";
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

        try {
            clinicaService.registrarPaciente(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Paciente registrado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/medico/agenda";
    }

    @GetMapping("/pacientes/{id}/editar")
    public String editarPaciente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Paciente paciente = clinicaService.buscarPacientePorId(id).orElse(null);
        if (paciente == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Paciente no encontrado.");
            return "redirect:/medico/agenda";
        }

        PacienteForm form = new PacienteForm();
        form.setNombreCompleto(paciente.getNombreCompleto());
        form.setDni(paciente.getDni());
        form.setEdad(paciente.getEdad());
        form.setParentesco(paciente.getParentesco());
        form.setTelefono(paciente.getTelefono());
        form.setCorreo(paciente.getCorreo());

        model.addAttribute("pacienteId", id);
        model.addAttribute("pacienteForm", form);
        return "medico/paciente-form";
    }

    @PostMapping("/pacientes/{id}")
    public String actualizarPaciente(
            @PathVariable Long id,
            @Valid @ModelAttribute("pacienteForm") PacienteForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pacienteId", id);
            return "medico/paciente-form";
        }

        try {
            clinicaService.actualizarPaciente(id, form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Paciente actualizado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/pacientes/" + id + "/editar";
        }
        return "redirect:/medico/agenda";
    }

    @PostMapping("/pacientes/{id}/eliminar")
    public String eliminarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clinicaService.eliminarPaciente(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Paciente eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/medico/agenda";
    }

    @PostMapping("/medico/citas")
    public String registrarCitaMedico(
            @Valid @ModelAttribute("citaForm") CitaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.citaForm", bindingResult);
            redirectAttributes.addFlashAttribute("citaForm", form);
            redirectAttributes.addFlashAttribute("mensajeError", "Revisa los datos de la cita.");
            return "redirect:/medico/agenda";
        }

        try {
            clinicaService.guardarCita(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cita medica registrada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/medico/agenda";
    }

    @GetMapping("/citas/{id}/editar")
    public String editarCita(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var cita = clinicaService.buscarCitaPorId(id).orElse(null);
        if (cita == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Cita no encontrada.");
            return "redirect:/medico/agenda";
        }

        CitaForm form = new CitaForm();
        form.setId(cita.getId());
        form.setPacienteId(cita.getPacienteId());
        form.setHorarioId(cita.getHorario().getId());
        form.setEstado(cita.getEstado());

        model.addAttribute("citaForm", form);
        model.addAttribute("pacientes", clinicaService.listarPacientes());
        model.addAttribute("horarios", clinicaService.listarHorarios());
        return "medico/cita-form";
    }

    @PostMapping("/citas/{id}")
    public String actualizarCita(
            @PathVariable Long id,
            @Valid @ModelAttribute("citaForm") CitaForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        form.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pacientes", clinicaService.listarPacientes());
            model.addAttribute("horarios", clinicaService.listarHorarios());
            return "medico/cita-form";
        }

        try {
            clinicaService.guardarCita(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cita actualizada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/citas/" + id + "/editar";
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
