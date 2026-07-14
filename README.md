# Lima Salud

Sistema web para gestion de citas medicas. Spring Boot 3 + Thymeleaf + JPA/Hibernate + MySQL.

## Estructura del proyecto

```
lima-salud/
├── database/              # Scripts SQL y guia MySQL
├── docs/entregables/      # Monografia, diapositivas y assets academicos
├── src/main/java/com/clinica/limasalud/
│   ├── api/               # API REST protegida con JWT (login + citas + pacientes)
│   ├── config/            # Seguridad (Spring Security + JWT), datos iniciales
│   ├── controller/        # Controladores web (MVC)
│   ├── dto/               # Formularios y validaciones
│   ├── entity/            # Entidades JPA (Usuario, Paciente, Cita...)
│   ├── repository/        # Acceso a datos (Spring Data JPA)
│   ├── security/          # JwtService y JwtAuthenticationFilter
│   └── service/           # Logica de negocio
└── src/main/resources/
    ├── static/            # CSS, JS e imagenes
    ├── templates/         # Vistas Thymeleaf (admin, auth, medico, paciente, public)
    ├── application.properties
    ├── application-local.properties.example
    └── application-prod.properties
```

## Requisitos previos

- Java 17+
- Maven 3.9+
- MySQL 8.x ([MySQL Installer](https://dev.mysql.com/downloads/installer/))
- MySQL Workbench (recomendado)

## Configuracion MySQL local (para cada desarrollador)

Cada desarrollador configura **su propio MySQL local**. No compartas contraseñas en el repo.

1. **Instala MySQL** (Server + Workbench) y define la clave del usuario `root`.
2. **Copia la plantilla** de propiedades:
   ```bash
   copy src\main\resources\application-local.properties.example src\main\resources\application-local.properties
   ```
3. **Edita** `application-local.properties` y reemplaza `TU_CLAVE_AQUI` por tu contraseña local.
4. **Ejecuta** la aplicacion:
   ```bash
   mvn spring-boot:run
   ```
5. Abre http://localhost:8080

> La base `lima_salud` y las tablas se crean automaticamente al arrancar (Hibernate `ddl-auto=update`).

Mas detalle en [`database/README.md`](database/README.md).

## Ejecutar sin MySQL (perfil `h2`, rapido para demo/pruebas)

Si solo quieres **probar la app o generar evidencias** sin instalar ni configurar MySQL,
usa el perfil `h2` (base de datos en memoria, con datos de ejemplo precargados):

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

Abre http://localhost:8080. Los perfiles `local` y `prod` (MySQL) no se ven afectados.

## Verificar tablas en MySQL Workbench

1. Conectate a tu instancia local de MySQL.
2. Abre el archivo [`database/consultas.sql`](database/consultas.sql).
3. Ejecuta las consultas para ver tablas, usuarios, pacientes y citas.

## Despliegue Railway

1. Conecta el repositorio en [Railway](https://railway.app).
2. **Root Directory**: dejalo **vacio** (raiz del repo, donde esta `pom.xml`).
3. Anade el **plugin MySQL** y vincula las variables al servicio web:
   - `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`
4. Variable de entorno del servicio web:
   ```
   SPRING_PROFILES_ACTIVE=prod
   ```
5. El archivo `railway.toml` define build (`mvn clean package`) y start (`java -jar ... -Dspring.profiles.active=prod`).

## Usuarios demo

| Usuario  | Contrasena    | Rol      |
|----------|---------------|----------|
| admin    | Admin2026!    | ADMIN    |
| medico   | Medico2026!   | MEDICO   |
| paciente | Paciente2026! | PACIENTE |

Se crean automaticamente al primer arranque (`DataInitializer`).

## Seguridad (Spring Security + JWT)

El sistema implementa las dos capas de seguridad exigidas en el Avance 3, mediante **dos
cadenas de filtros independientes** ([`SecurityConfig`](src/main/java/com/clinica/limasalud/config/SecurityConfig.java)):

1. **Aplicacion web (Spring Security)** — login por formulario con control por roles
   (`ADMIN`, `MEDICO`, `PACIENTE`). Las contrasenas se almacenan cifradas con **BCrypt**.
2. **API REST (JWT)** — proteccion *stateless* de `/api/**`. El cliente obtiene un token en
   `POST /api/auth/login` y lo envia luego en la cabecera `Authorization: Bearer <token>`.

### Endpoints de la API

| Metodo | Endpoint | Rol requerido |
|--------|----------------------|-------------------------|
| POST | `/api/auth/login` | Publico (devuelve el JWT) |
| GET | `/api/perfil` | Cualquier usuario autenticado |
| GET | `/api/citas` | ADMIN, MEDICO, PACIENTE |
| GET | `/api/pacientes` | Solo ADMIN |

### Ejemplo de uso

```bash
# 1) Obtener el token
curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"Admin2026!"}'
# -> { "token": "eyJhbGciOiJIUzM4NCJ9...", "tipo": "Bearer", "rol": "ADMIN", ... }

# 2) Consumir un endpoint protegido con el token
curl http://localhost:8080/api/pacientes \
     -H "Authorization: Bearer <TOKEN>"
```

La clave de firma y la expiracion del token se configuran con las variables
`JWT_SECRET` y `JWT_EXPIRATION_MS` (ver `application.properties`).

## Pruebas

Las pruebas de integracion de seguridad se ejecutan sobre una base de datos **H2 en memoria**
(perfil `test`), por lo que **no requieren MySQL**:

```bash
mvn test
```

[`SeguridadJwtTest`](src/test/java/com/clinica/limasalud/SeguridadJwtTest.java) verifica que la
API rechaza peticiones sin token (401), que el login entrega un JWT valido con su rol, y que los
roles se respetan (ADMIN accede a pacientes; PACIENTE accede a citas pero no a pacientes → 403).
