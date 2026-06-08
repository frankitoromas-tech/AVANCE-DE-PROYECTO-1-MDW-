# Base de datos MySQL — Lima Salud

## Desarrollo local (cada desarrollador)

Cada persona del equipo usa **su propia instancia MySQL** en su computadora. Las claves no se comparten ni se suben al repositorio.

1. Instala MySQL 8.x y MySQL Workbench.
2. Copia la plantilla de configuracion:
   ```bash
   copy src\main\resources\application-local.properties.example src\main\resources\application-local.properties
   ```
3. Edita `application-local.properties` y pon tu contraseña local en `MYSQLPASSWORD`.
4. Ejecuta la aplicacion:
   ```bash
   mvn spring-boot:run
   ```
5. Hibernate crea la base `lima_salud` y las tablas al arrancar.

## Verificar datos

Abre `consultas.sql` en MySQL Workbench y ejecuta las consultas despues de iniciar la app. Incluye `SHOW TABLES` y consultas de usuarios, pacientes y citas.

## Produccion (Railway)

Railway provee las variables de entorno `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER` y `MYSQLPASSWORD` al vincular el plugin MySQL. La app usa el perfil `prod` (`application-prod.properties`).
