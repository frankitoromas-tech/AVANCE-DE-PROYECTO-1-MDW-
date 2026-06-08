package com.clinica.limasalud.controller;

import com.clinica.limasalud.entity.Rol;
import com.clinica.limasalud.entity.Usuario;
import com.clinica.limasalud.dto.UsuarioForm;
import com.clinica.limasalud.service.ClinicaService;
import com.clinica.limasalud.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;

    public AdminUsuarioController(UsuarioService usuarioService, ClinicaService clinicaService) {
        this.usuarioService = usuarioService;
        this.clinicaService = clinicaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        prepararFormulario(model, new UsuarioForm());
        return "admin/usuario-form";
    }

    @PostMapping
    public String crear(
            @Valid @ModelAttribute("usuarioForm") UsuarioForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, form);
            return "admin/usuario-form";
        }

        try {
            usuarioService.crearUsuario(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado correctamente.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            prepararFormulario(model, form);
            return "admin/usuario-form";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }

        UsuarioForm form = new UsuarioForm();
        form.setId(usuario.getId());
        form.setUsername(usuario.getUsername());
        form.setNombreCompleto(usuario.getNombreCompleto());
        form.setCorreo(usuario.getCorreo());
        form.setRol(usuario.getRol());
        form.setPacienteId(usuario.getPacienteId());

        prepararFormulario(model, form);
        model.addAttribute("editando", true);
        return "admin/usuario-form";
    }

    @PostMapping("/{id}")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuarioForm") UsuarioForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        form.setId(id);

        if (bindingResult.hasErrors()) {
            prepararFormulario(model, form);
            model.addAttribute("editando", true);
            return "admin/usuario-form";
        }

        try {
            usuarioService.actualizarUsuario(id, form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            prepararFormulario(model, form);
            model.addAttribute("editando", true);
            return "admin/usuario-form";
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    private void prepararFormulario(Model model, UsuarioForm form) {
        model.addAttribute("usuarioForm", form);
        model.addAttribute("roles", Rol.values());
        model.addAttribute("pacientes", clinicaService.listarPacientes());
    }
}
