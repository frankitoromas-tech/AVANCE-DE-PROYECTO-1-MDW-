package com.clinica.limasalud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion de la seguridad JWT de la API REST.
 *
 * Verifican que:
 *  - La API rechaza peticiones sin token (401).
 *  - El login entrega un JWT valido con el rol correcto.
 *  - Los roles se respetan: ADMIN accede a la gestion de pacientes (200);
 *    PACIENTE accede a citas (200) pero no a pacientes (403).
 *  - Las credenciales invalidas son rechazadas (401).
 *
 * Se usa el perfil "test" (base de datos H2 en memoria) para no depender de MySQL.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeguridadJwtTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String login(String username, String password) throws Exception {
        String body = "{\"username\":\"%s\",\"password\":\"%s\"}".formatted(username, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("token").asText();
    }

    @Test
    void apiRechazaPeticionSinToken() throws Exception {
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginAdminEntregaTokenConRol() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"Admin2026!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void adminAccedeAGestionDePacientes() throws Exception {
        String token = login("admin", "Admin2026!");

        mockMvc.perform(get("/api/pacientes").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void pacienteAccedeACitasPeroNoAPacientes() throws Exception {
        String token = login("paciente", "Paciente2026!");

        // El rol PACIENTE puede consultar las citas...
        mockMvc.perform(get("/api/citas").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // ...pero NO la gestion de pacientes (reservada a ADMIN).
        mockMvc.perform(get("/api/pacientes").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void credencialesInvalidasSonRechazadas() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"claveIncorrecta\"}"))
                .andExpect(status().isUnauthorized());
    }
}
