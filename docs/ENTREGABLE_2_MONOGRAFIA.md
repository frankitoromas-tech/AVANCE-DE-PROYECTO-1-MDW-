# Entregable 2: Logica de negocio con Spring

## Caratula

**Curso:** Marcos de Desarrollo Web  
**Proyecto final:** Desarrollo de un Sistema Web para optimizar la gestion de citas en pacientes de una clinica en Lima, 2026  
**Entregable:** Entregable 2 - Logica de negocio con Spring  
**Integrantes:** [Completar nombres]  
**Docente:** [Completar nombre]  
**Institucion:** [Completar institucion]  
**Fecha:** Mayo de 2026

## Indice

1. Introduccion  
2. Objetivos del entregable  
3. Configuracion de Spring Boot  
4. Logica de negocio implementada  
5. Controladores del sistema  
6. Renderizado dinamico con Thymeleaf  
7. Seguridad y control de accesos  
8. Evidencias del sistema  
9. Conclusiones  
10. Referencias bibliograficas

## 1. Introduccion

La gestion de citas medicas es un proceso critico en las clinicas, debido a que influye directamente en la experiencia del paciente, la organizacion del personal de salud y el aprovechamiento de los recursos de atencion. En el contexto de Lima, una plataforma web permite centralizar el registro de pacientes, la programacion de citas y la administracion de servicios, reduciendo errores manuales y facilitando el acceso a la informacion.

Desde el punto de vista tecnologico, Spring Boot permite construir aplicaciones web Java de manera organizada, integrando controladores MVC, seguridad, validacion y plantillas dinamicas con Thymeleaf. Para este entregable se desarrollo una version funcional sin base de datos persistente, usando estructuras en memoria para simular pacientes, usuarios, servicios, horarios y citas.

## 2. Objetivos del entregable

El objetivo principal fue implementar la logica de negocio del sistema web con Spring Boot, integrando:

- Registro de usuarios pacientes.
- Gestion de pacientes.
- Registro y cancelacion de citas.
- Administracion de servicios medicos.
- Control de acceso por roles.
- Renderizado dinamico de informacion usando Thymeleaf.

## 3. Configuracion de Spring Boot

El proyecto fue convertido de una pagina estatica HTML/CSS/JS a una aplicacion Maven con Spring Boot.

Archivo principal de configuracion:

- `pom.xml`: declara dependencias de Spring Web, Thymeleaf, Spring Security y validacion.
- `src/main/resources/application.properties`: configura el nombre de la aplicacion, puerto 8080 y cache de Thymeleaf desactivada para desarrollo.
- `LimaSaludApplication.java`: clase principal con `@SpringBootApplication`.

**Imagen sugerida:** captura del archivo `pom.xml` mostrando las dependencias principales.

## 4. Logica de negocio implementada

La logica se implemento en servicios Java, sin base de datos persistente:

- `ClinicaService`: administra pacientes, servicios, horarios y citas en memoria.
- `UsuarioService`: administra usuarios, roles y autenticacion desde memoria.

Entidades principales:

- `Paciente`: representa los datos personales del paciente.
- `Usuario`: representa el acceso al sistema y su rol.
- `ServicioMedico`: representa una especialidad o servicio clinico.
- `HorarioMedico`: representa disponibilidad por medico, dia, hora y modalidad.
- `Cita`: representa la reserva de atencion medica.

**Imagen sugerida:** captura del paquete `model` y `service` en el explorador del IDE.

## 5. Controladores del sistema

### 5.1 UsuarioController

El controlador `UsuarioController` gestiona:

- Pagina de inicio: `GET /`
- Login personalizado: `GET /login`
- Registro de usuario paciente: `POST /usuarios/registrar`
- Redireccion por rol despues del login: `GET /redirigir`

Este controlador conecta la vista de acceso con la logica de usuarios y define el flujo inicial del sistema.

**Imagen sugerida:** captura del archivo `UsuarioController.java`.

### 5.2 PacienteController

El controlador `PacienteController` gestiona:

- Portal del paciente: `GET /portal-paciente`
- Registro de cita por paciente: `POST /citas`
- Cancelacion de cita: `POST /citas/{id}/cancelar`
- Agenda medica: `GET /medico/agenda`
- Registro de pacientes desde el portal medico: `POST /pacientes`
- Registro de citas desde el portal medico: `POST /medico/citas`
- Administracion de servicios: `POST /servicios`

Este controlador cumple la funcion central del entregable, porque conecta los datos dinamicos con las vistas Thymeleaf.

**Imagen sugerida:** captura del archivo `PacienteController.java`.

## 6. Renderizado dinamico con Thymeleaf

Las vistas fueron ubicadas en `src/main/resources/templates`:

- `index.html`: pagina principal con servicios renderizados desde Spring.
- `login.html`: formulario de autenticacion y registro de pacientes.
- `portal-paciente.html`: muestra datos del paciente autenticado, servicios y citas.
- `agenda-medico.html`: muestra pacientes, citas y servicios para el personal medico.

Ejemplos de renderizado dinamico:

- `th:each` para listar servicios medicos.
- `th:text` para mostrar nombres, DNI, horarios y medicos.
- Formularios `th:action` para enviar datos al backend.
- Token CSRF en formularios protegidos.

**Imagen sugerida:** captura del portal del paciente con una cita renderizada.

## 7. Seguridad y control de accesos

La seguridad fue configurada en `SecurityConfig.java` con Spring Security.

Se implemento:

- Login personalizado en `/login`.
- Contraseñas cifradas con BCrypt.
- Roles: `PACIENTE`, `MEDICO` y `ADMIN`.
- Proteccion de rutas:
  - Paciente: `/portal-paciente`, `/citas`.
  - Medico/Admin: `/medico/**`, `/pacientes`, `/servicios`.
- Logout mediante `POST /logout`.
- Proteccion CSRF en formularios.

Credenciales de prueba:

- Paciente: `paciente` / `Paciente2026!`
- Medico: `medico` / `Medico2026!`
- Admin: `admin` / `Admin2026!`

**Imagen sugerida:** captura del archivo `SecurityConfig.java` y captura del login.

## 8. Evidencias del sistema

Capturas recomendadas para insertar en el documento:

1. Inicio del sistema en `http://localhost:8080/`.
2. Formulario de login.
3. Portal del paciente autenticado.
4. Modal de registro de cita.
5. Lista "Mis Citas".
6. Portal medico.
7. Registro de paciente desde el portal medico.
8. Administracion de servicios medicos.
9. Archivo `pom.xml`.
10. Archivos `PacienteController.java` y `UsuarioController.java`.

Comando de ejecucion:

```powershell
mvn spring-boot:run
```

URL local:

```text
http://localhost:8080/
```

## 9. Conclusiones

Se logro implementar la logica de negocio del sistema web usando Spring Boot, integrando controladores MVC, seguridad con Spring Security y renderizado dinamico con Thymeleaf. La aplicacion permite registrar pacientes, gestionar citas, administrar servicios y controlar accesos por roles sin usar una base de datos persistente.

El entregable demuestra la transicion de un prototipo front-end estatico hacia una aplicacion web funcional con backend, preparada para una futura etapa donde se incorpore persistencia con base de datos relacional.

## 10. Referencias bibliograficas

Spring. (s. f.). *Spring Boot Reference Documentation*. https://docs.spring.io/spring-boot/

Spring Security. (s. f.). *Spring Security Reference*. https://docs.spring.io/spring-security/reference/

The Thymeleaf Project. (s. f.). *Thymeleaf Documentation*. https://www.thymeleaf.org/documentation.html

OWASP Foundation. (s. f.). *Cross-Site Request Forgery Prevention Cheat Sheet*. https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html

World Health Organization. (2021). *Global strategy on digital health 2020-2025*. https://www.who.int/publications/i/item/9789240020924
