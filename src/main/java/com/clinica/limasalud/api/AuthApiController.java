package com.clinica.limasalud.api;

import com.clinica.limasalud.api.dto.AuthResponse;
import com.clinica.limasalud.api.dto.LoginRequest;
import com.clinica.limasalud.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Autenticacion de la API REST. Valida las credenciales con Spring Security y,
 * si son correctas, entrega un JWT firmado que el cliente debe enviar luego en la
 * cabecera {@code Authorization: Bearer <token>}.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthApiController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String rol = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(a -> a.replace("ROLE_", ""))
                .orElse("DESCONOCIDO");

        String token = jwtService.generarToken(authentication.getName(), rol);

        return ResponseEntity.ok(new AuthResponse(
                token, authentication.getName(), rol, jwtService.getExpirationMillis()));
    }
}
