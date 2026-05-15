# Guion de diapositivas - Entregable 2

## Diapositiva 1: Titulo

**Sistema Web para optimizar la gestion de citas en pacientes de una clinica en Lima, 2026**  
Entregable 2: Logica de negocio con Spring

## Diapositiva 2: Problema

Las citas medicas requieren organizacion, control de usuarios y disponibilidad de servicios. El sistema busca reducir procesos manuales mediante una plataforma web con acceso para pacientes y personal clinico.

## Diapositiva 3: Objetivo

Implementar una aplicacion Spring Boot funcional, sin base de datos persistente, que permita registrar pacientes, gestionar citas, administrar servicios y controlar accesos por roles.

## Diapositiva 4: Arquitectura usada

- Spring Boot como framework principal.
- Spring MVC para controladores.
- Thymeleaf para vistas dinamicas.
- Spring Security para autenticacion y autorizacion.
- Datos en memoria para simular la base de datos.

## Diapositiva 5: Configuracion Spring Boot

Mostrar captura de:

- `pom.xml`
- `application.properties`
- `LimaSaludApplication.java`

Mensaje clave: el proyecto ya no es solo front-end; ahora tiene backend ejecutable.

## Diapositiva 6: UsuarioController

Explicar:

- Muestra la pagina principal.
- Renderiza login.
- Registra usuarios pacientes.
- Redirige segun rol.

Captura sugerida: `UsuarioController.java`.

## Diapositiva 7: PacienteController

Explicar:

- Renderiza el portal del paciente.
- Registra y cancela citas.
- Renderiza la agenda del medico.
- Registra pacientes desde el portal medico.
- Administra servicios medicos.

Captura sugerida: `PacienteController.java`.

## Diapositiva 8: Thymeleaf dinamico

Mostrar:

- Lista de servicios con `th:each`.
- Citas del paciente con datos reales del backend.
- Formularios con `th:action`.

Captura sugerida: portal del paciente y agenda medica.

## Diapositiva 9: Seguridad

Explicar:

- Login personalizado.
- Contraseñas con BCrypt.
- Roles `PACIENTE`, `MEDICO`, `ADMIN`.
- Proteccion CSRF en formularios.

Captura sugerida: login y `SecurityConfig.java`.

## Diapositiva 10: Demostracion

Flujo recomendado:

1. Abrir `http://localhost:8080/`.
2. Iniciar sesion como paciente.
3. Registrar una cita.
4. Cerrar sesion.
5. Iniciar sesion como medico.
6. Mostrar agenda y administrar servicios.

## Diapositiva 11: Conclusiones

Se implemento un sistema funcional con Spring Boot, controladores MVC, Thymeleaf y seguridad. La version actual trabaja con datos en memoria y queda preparada para integrar una base de datos en el siguiente entregable.

## Diapositiva 12: Referencias

- Spring Boot Documentation.
- Spring Security Reference.
- Thymeleaf Documentation.
- OWASP CSRF Prevention Cheat Sheet.
- WHO Global Strategy on Digital Health 2020-2025.
