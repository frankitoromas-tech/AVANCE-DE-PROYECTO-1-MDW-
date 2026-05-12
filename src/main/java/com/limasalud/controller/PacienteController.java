package com.limasalud.controller;

import com.limasalud.model.CitaForm;
import com.limasalud.service.CatalogoService;
import com.limasalud.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private final CatalogoService catalogoService;
    private final CitaService citaService;

    public PacienteController(CatalogoService catalogoService, CitaService citaService) {
        this.catalogoService = catalogoService;
        this.citaService = citaService;
    }

    @GetMapping("/portal")
    public String portalPaciente(Model model) {
        model.addAttribute("servicios", catalogoService.listarServicios());
        model.addAttribute("citas", citaService.listarCitas());
        model.addAttribute("citaForm", new CitaForm());
        return "paciente/portal";
    }

    @GetMapping("/citas/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("servicios", catalogoService.listarServicios());
        model.addAttribute("citas", citaService.listarCitas());
        model.addAttribute("citaForm", new CitaForm());
        model.addAttribute("detalleCita", citaService.buscarPorId(id).orElse(null));
        model.addAttribute("abrirDetalle", true);
        model.addAttribute("vistaActiva", "misCitas");
        return "paciente/portal";
    }

    @PostMapping("/citas/{id}/eliminar")
    public String eliminarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        citaService.eliminar(id);
        redirectAttributes.addFlashAttribute("toastMensaje", "Cita cancelada correctamente");
        redirectAttributes.addFlashAttribute("vistaActiva", "misCitas");
        return "redirect:/paciente/portal";
    }
}
