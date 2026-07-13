package com.clinica.limasalud.api;

import com.clinica.limasalud.api.dto.CitaResponse;
import com.clinica.limasalud.api.dto.PacienteResponse;
import com.clinica.limasalud.service.ClinicaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * API REST protegida con JWT. La autorizacion por rol se define en
 * {@code SecurityConfig#apiSecurityFilterChain}:
 *  - /api/citas     -> ADMIN, MEDICO y PACIENTE (gestion de citas)
 *  - /api/pacientes -> solo ADMIN (gestion de pacientes)
 *  - /api/perfil    -> cualquier usuario autenticado
 */
@RestController
@RequestMapping("/api")
public class RecursosApiController {

    private final ClinicaService clinicaService;

    public RecursosApiController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    /** Datos del usuario autenticado a partir del token JWT. */
    @GetMapping("/perfil")
    public Map<String, Object> perfil(@AuthenticationPrincipal UserDetails usuario) {
        String rol = usuario.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("DESCONOCIDO");
        return Map.of("username", usuario.getUsername(), "rol", rol);
    }

    /** Listado de citas (gestion de citas): ADMIN, MEDICO y PACIENTE. */
    @GetMapping("/citas")
    public List<CitaResponse> listarCitas() {
        return clinicaService.listarCitas().stream()
                .map(CitaResponse::desde)
                .toList();
    }

    /** Listado de pacientes (gestion de pacientes): exclusivo de ADMIN. */
    @GetMapping("/pacientes")
    public List<PacienteResponse> listarPacientes() {
        return clinicaService.listarPacientes().stream()
                .map(PacienteResponse::desde)
                .toList();
    }
}
