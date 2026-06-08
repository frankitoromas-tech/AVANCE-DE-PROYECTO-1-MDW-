# Lima Salud

Sistema web para gestion de citas medicas. Spring Boot 3 + Thymeleaf + JPA/Hibernate + MySQL.

## Estructura del proyecto

```
lima-salud/
├── database/              # Scripts SQL y guia MySQL
├── docs/entregables/      # Monografia, diapositivas y assets academicos
├── src/main/java/com/clinica/limasalud/
│   ├── config/            # Seguridad, datos iniciales
│   ├── controller/        # Controladores web (MVC)
│   ├── dto/               # Formularios y validaciones
│   ├── entity/            # Entidades JPA (Usuario, Paciente, Cita...)
│   ├── repository/        # Acceso a datos (Spring Data JPA)
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
