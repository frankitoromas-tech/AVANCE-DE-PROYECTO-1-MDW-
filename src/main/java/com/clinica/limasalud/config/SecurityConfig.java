package com.clinica.limasalud.config;

import com.clinica.limasalud.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
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
}
