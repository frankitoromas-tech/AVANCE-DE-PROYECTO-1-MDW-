package com.clinica.limasalud.config;

import com.clinica.limasalud.security.JwtAuthenticationFilter;
import com.clinica.limasalud.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion de seguridad de la aplicacion.
 *
 * Se definen DOS cadenas de filtros independientes:
 *
 *  1) API REST (/api/**)  -> Autenticacion STATELESS mediante JWT (JSON Web Token).
 *  2) Aplicacion web      -> Autenticacion por formulario con la pagina de login
 *                            personalizada del proyecto y control por roles.
 *
 * Roles: ADMIN (gestion de citas y pacientes), MEDICO y PACIENTE (gestion de citas).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Cadena 1: API REST protegida con JWT. Es "stateless" (no crea sesiones)
     * y valida el token en cada peticion mediante {@link JwtAuthenticationFilter}.
     *
     * El filtro se recibe como parametro del metodo (no por constructor) para evitar
     * un ciclo con el PasswordEncoder que este mismo @Configuration expone.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,
                                                      JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // El login de la API (que entrega el JWT) es publico.
                .requestMatchers("/api/auth/**").permitAll()
                // Gestion de pacientes: solo ADMIN.
                .requestMatchers("/api/pacientes/**").hasRole("ADMIN")
                // Gestion de citas: ADMIN, MEDICO y PACIENTE.
                .requestMatchers("/api/citas/**").hasAnyRole("ADMIN", "MEDICO", "PACIENTE")
                .anyRequest().authenticated()
            )
            // Manejo de errores propio de la API (respuestas JSON, sin redirigir al login web).
            // IMPORTANTE: se escribe el estado directamente (setStatus) en lugar de sendError,
            // para no provocar un ERROR dispatch a /error que caeria en la cadena web.
            .exceptionHandling(ex -> ex
                    // Sin token / token invalido -> 401.
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    // Autenticado pero sin el rol requerido -> 403.
                    .accessDeniedHandler((request, response, denied) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write(
                                "{\"status\":403,\"error\":\"Forbidden\","
                                + "\"message\":\"No tienes permiso para acceder a este recurso\"}");
                    }))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cadena 2: aplicacion web (Thymeleaf) con el formulario de login del proyecto.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
        return http
                .userDetailsService(usuarioService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/usuarios/registrar", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/medico/**", "/pacientes/**", "/servicios/**").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/citas/*/editar").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/citas/*").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/citas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/citas/*/cancelar").authenticated()
                        .requestMatchers("/portal-paciente").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirigir", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
