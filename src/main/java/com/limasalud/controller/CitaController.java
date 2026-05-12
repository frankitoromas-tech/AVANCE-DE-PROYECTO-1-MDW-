package com.limasalud.controller;

import com.limasalud.model.CitaForm;
import com.limasalud.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping("/registrar")
    public String registrarDesdePaciente(@ModelAttribute CitaForm citaForm, RedirectAttributes redirectAttributes) {
        citaService.registrarDesdePortalPaciente(citaForm);
        redirectAttributes.addFlashAttribute("toastMensaje", "Cita registrada con éxito");
        redirectAttributes.addFlashAttribute("vistaActiva", "misCitas");
        return "redirect:/paciente/portal";
    }
}
