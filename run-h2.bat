@echo off
REM ============================================================
REM  Lima Salud - Ejecutar la aplicacion SIN MySQL (perfil h2)
REM  Base de datos en memoria con datos de ejemplo precargados.
REM  Uso:  doble clic, o desde la terminal:  .\run-h2.bat
REM  Luego abre:  http://localhost:8080
REM  Usuarios: admin/Admin2026!  medico/Medico2026!  paciente/Paciente2026!
REM ============================================================
cd /d "%~dp0"
call mvn spring-boot:run -Dspring-boot.run.profiles=h2
