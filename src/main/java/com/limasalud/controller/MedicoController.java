package com.limasalud.controller;

import com.limasalud.model.PacienteForm;
import com.limasalud.service.CatalogoService;
import com.limasalud.service.CitaService;
import com.limasalud.service.PacienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final PacienteService pacienteService;
    private final CatalogoService catalogoService;
    private final CitaService citaService;

    public MedicoController(PacienteService pacienteService, CatalogoService catalogoService, CitaService citaService) {
        this.pacienteService = pacienteService;
        this.catalogoService = catalogoService;
        this.citaService = citaService;
    }

    @GetMapping("/agenda")
    public String agenda(Model model, HttpSession session) {
        if (session.getAttribute("usuarioMedico") == null) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("pacientes", pacienteService.listarPacientes());
        model.addAttribute("servicios", catalogoService.listarServicios());
        model.addAttribute("citas", citaService.listarCitas());
        model.addAttribute("pacienteForm", new PacienteForm());
        return "medico/agenda";
    }

    @PostMapping("/citas")
    public String registrarCita(@RequestParam Long pacienteId, @RequestParam String servicioId,
            @RequestParam String fechaHora, RedirectAttributes redirectAttributes) {
        citaService.registrarDesdePortalMedico(pacienteId, servicioId, fechaHora);
        redirectAttributes.addFlashAttribute("toastMensaje", "Cita registrada con éxito.");
        return "redirect:/medico/agenda";
    }
}
