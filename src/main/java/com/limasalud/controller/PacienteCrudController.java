package com.limasalud.controller;

import com.limasalud.model.PacienteForm;
import com.limasalud.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
public class PacienteCrudController {

    private final PacienteService pacienteService;

    public PacienteCrudController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute PacienteForm pacienteForm, RedirectAttributes redirectAttributes) {
        pacienteService.registrarPaciente(pacienteForm);
        redirectAttributes.addFlashAttribute("toastMensaje", "Paciente registrado con éxito.");
        return "redirect:/medico/agenda";
    }
}
