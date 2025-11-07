-- Datos de prueba para loan-service
-- Se ejecuta automáticamente al inicializar MySQL

-- Crear usuarios de prueba
INSERT IGNORE INTO user (id, code, name, user_type, active) VALUES 
(1, 'EST001', 'Juan Estudiante', 'STUDENT', true),
(2, 'EST002', 'María López', 'STUDENT', true),
(3, 'EST003', 'Carlos Rivera', 'STUDENT', false),
(4, 'DOC001', 'Prof. García', 'TEACHER', true),
(5, 'DOC002', 'Prof. Martínez', 'TEACHER', true),
(6, 'DOC003', 'Prof. Hernández', 'TEACHER', false);

-- Crear préstamos de ejemplo para demostrar funcionalidad
INSERT IGNORE INTO loan (id, user_code, classroom_code, loan_date, start_time, end_time, start_hour, duration, week_number, month_number, purpose, status) VALUES
-- Préstamos activos
(1, 'EST001', 'AULA101', '2025-11-07', '09:00:00', '11:00:00', 9, 120, 45, 11, 'Estudio grupal proyecto final', 'ACTIVE'),
(2, 'DOC001', 'LAB205', '2025-11-07', '14:00:00', '16:00:00', 14, 120, 45, 11, 'Clase práctica de programación', 'ACTIVE'),
(3, 'EST002', 'AULA102', '2025-11-07', '16:00:00', '18:00:00', 16, 120, 45, 11, 'Reunión de grupo', 'ACTIVE'),

-- Préstamos reservados (futuros)
(4, 'DOC002', 'AULA103', '2025-11-08', '08:00:00', '10:00:00', 8, 120, 45, 11, 'Clase magistral', 'RESERVED'),
(5, 'EST001', 'LAB301', '2025-11-08', '10:00:00', '12:00:00', 10, 120, 45, 11, 'Proyecto de grado', 'RESERVED'),
(6, 'DOC001', 'AULA104', '2025-11-09', '15:00:00', '17:00:00', 15, 120, 45, 11, 'Tutorías académicas', 'RESERVED'),

-- Préstamos cancelados (ejemplos históricos)
(7, 'EST002', 'AULA101', '2025-11-06', '13:00:00', '15:00:00', 13, 120, 45, 11, 'Estudio cancelado', 'CANCELLED'),
(8, 'EST003', 'LAB205', '2025-11-05', '11:00:00', '13:00:00', 11, 120, 45, 11, 'Usuario inactivo', 'CANCELLED'),

-- Datos adicionales para analytics (diferentes horarios y días)
(9, 'DOC001', 'AULA105', '2025-11-04', '07:00:00', '09:00:00', 7, 120, 45, 11, 'Clase temprana', 'ACTIVE'),
(10, 'EST001', 'LAB206', '2025-11-04', '19:00:00', '21:00:00', 19, 120, 45, 11, 'Estudio nocturno', 'ACTIVE'),
(11, 'DOC002', 'AULA106', '2025-11-03', '06:00:00', '08:00:00', 6, 120, 44, 11, 'Reunión administrativa', 'ACTIVE'),
(12, 'EST002', 'LAB301', '2025-11-03', '20:00:00', '22:00:00', 20, 120, 44, 11, 'Proyecto avanzado', 'ACTIVE');

-- Insertar más datos para demostrar analytics por mes y semana
INSERT IGNORE INTO loan (id, user_code, classroom_code, loan_date, start_time, end_time, start_hour, duration, week_number, month_number, purpose, status) VALUES
-- Octubre (mes 10)
(13, 'DOC001', 'AULA107', '2025-10-15', '09:00:00', '11:00:00', 9, 120, 42, 10, 'Clase octubre', 'ACTIVE'),
(14, 'EST001', 'LAB207', '2025-10-20', '14:00:00', '16:00:00', 14, 120, 42, 10, 'Práctica octubre', 'ACTIVE'),

-- Septiembre (mes 9)  
(15, 'DOC002', 'AULA108', '2025-09-10', '10:00:00', '12:00:00', 10, 120, 37, 9, 'Clase septiembre', 'ACTIVE'),
(16, 'EST002', 'LAB208', '2025-09-25', '16:00:00', '18:00:00', 16, 120, 39, 9, 'Práctica septiembre', 'ACTIVE');