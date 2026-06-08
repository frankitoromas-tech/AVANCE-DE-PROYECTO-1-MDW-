-- Clinica Lima Salud | Consultas para verificar datos en MySQL
-- Ejecutar despues de arrancar la app (mvn spring-boot:run)

USE lima_salud;

-- Tablas creadas por Hibernate
SHOW TABLES;

-- CRUD: Usuarios
SELECT id, username, nombre_completo, correo, rol, paciente_id FROM usuarios;

-- CRUD: Pacientes
SELECT id, nombre_completo, dni, edad, parentesco, telefono, correo FROM pacientes;

-- CRUD: Citas (con paciente y horario)
SELECT c.id, p.nombre_completo AS paciente, c.estado,
       h.nombre_medico, h.dia, h.hora, s.nombre AS servicio
FROM citas c
JOIN pacientes p ON p.id = c.paciente_id
JOIN horarios h ON h.id = c.horario_id
JOIN servicios s ON s.id = h.servicio_id
ORDER BY c.id;

-- Servicios y horarios disponibles
SELECT s.nombre AS servicio, h.nombre_medico, h.dia, h.hora, h.modalidad
FROM horarios h
JOIN servicios s ON s.id = h.servicio_id
ORDER BY s.nombre, h.dia;
