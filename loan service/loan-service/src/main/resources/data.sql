-- ============================================
-- LOAN-SERVICE: DATOS DE EJEMPLO UPTC
-- ============================================
-- Este archivo se ejecuta automáticamente al iniciar Spring Boot
-- cuando spring.jpa.hibernate.ddl-auto=create o create-drop

-- ============================================
-- 1. USUARIOS DE EJEMPLO CON PROGRAMAS ACADÉMICOS
-- ============================================

-- Estudiantes por programa académico
INSERT INTO user (code, name, user_type, academic_program, active) VALUES
('EST001', 'Juan Pérez García', 'STUDENT', 'Ingenieria-de-Sistemas', 1),
('EST002', 'María García López', 'STUDENT', 'Ingenieria-Industrial', 1),
('EST003', 'Carlos López Martínez', 'STUDENT', 'Administracion-de-Empresas', 1),
('EST004', 'Ana Martínez Rodríguez', 'STUDENT', 'Contaduria-Publica', 1),
('EST005', 'Luis Rodríguez Pérez', 'STUDENT', 'Ingenieria-Electronica', 1),
('EST006', 'Carmen Díaz Morales', 'STUDENT', 'Ingenieria-de-Sistemas', 1),
('EST007', 'Pedro González Silva', 'STUDENT', 'Ingenieria-Industrial', 1),
('EST008', 'Laura Hernández Castro', 'STUDENT', 'Administracion-de-Empresas', 1);

-- Profesores por programa académico
INSERT INTO user (code, name, user_type, academic_program, active) VALUES
('PROF001', 'Dr. Ana Martínez Silva', 'TEACHER', 'Ingenieria-de-Sistemas', 1),
('PROF002', 'Ing. Luis Rodríguez Castro', 'TEACHER', 'Ingenieria-Industrial', 1),
('PROF003', 'Mgtr. Carmen Díaz Morales', 'TEACHER', 'Administracion-de-Empresas', 1),
('PROF004', 'PhD. Roberto García Pérez', 'TEACHER', 'Ingenieria-de-Sistemas', 1),
('PROF005', 'Esp. Sandra López Martín', 'TEACHER', 'Contaduria-Publica', 1);

-- ============================================
-- 2. PRÉSTAMOS DE EJEMPLO PARA TESTING
-- ============================================

-- Préstamos ACTIVOS (en curso) - SOLO DATOS BASE
INSERT INTO loan (user_code, classroom_code, loan_date, start_time, end_time, purpose, status) VALUES
('EST001', 'AULA101', '2025-11-06', '08:00:00', '10:00:00', 'Clase de Programación Java', 'ACTIVE'),
('EST002', 'AULA102', '2025-11-06', '10:00:00', '12:00:00', 'Taller de Manufactura', 'ACTIVE'),
('PROF001', 'AULA201', '2025-11-06', '14:00:00', '16:00:00', 'Cátedra Algoritmos', 'ACTIVE'),
('EST003', 'AULA103', '2025-11-06', '16:00:00', '18:00:00', 'Presentación Proyecto Empresarial', 'ACTIVE');

-- Préstamos RESERVADOS (futuros) - SOLO DATOS BASE  
INSERT INTO loan (user_code, classroom_code, loan_date, start_time, end_time, purpose, status) VALUES
('EST004', 'AULA104', '2025-11-07', '08:00:00', '10:00:00', 'Examen de Contabilidad', 'RESERVED'),
('EST005', 'AULA105', '2025-11-07', '12:00:00', '14:00:00', 'Laboratorio Electrónica', 'RESERVED'),
('PROF002', 'AULA201', '2025-11-07', '16:00:00', '18:00:00', 'Seminario Industrial', 'RESERVED'),
('EST006', 'AULA106', '2025-11-08', '10:00:00', '11:00:00', 'Reunión Grupo Sistemas', 'RESERVED');

-- Préstamos CANCELADOS (histórico) - SOLO DATOS BASE
INSERT INTO loan (user_code, classroom_code, loan_date, start_time, end_time, purpose, status) VALUES
('EST007', 'AULA107', '2025-11-05', '14:00:00', '16:00:00', 'Conferencia - CANCELADA', 'CANCELLED'),
('EST008', 'AULA108', '2025-11-04', '08:00:00', '10:00:00', 'Exposición - CANCELADA', 'CANCELLED');

-- Préstamos históricos (diferentes fechas) - SOLO DATOS BASE
INSERT INTO loan (user_code, classroom_code, loan_date, start_time, end_time, purpose, status) VALUES
-- Octubre (mes anterior)
('EST001', 'AULA101', '2025-10-15', '09:00:00', '11:00:00', 'Práctica Java', 'ACTIVE'),
('EST002', 'AULA102', '2025-10-16', '13:00:00', '15:00:00', 'Taller Industrial', 'ACTIVE'),
('EST003', 'AULA103', '2025-10-17', '11:00:00', '13:00:00', 'Análisis Empresarial', 'ACTIVE'),

-- Semana pasada  
('PROF003', 'AULA201', '2025-11-01', '15:00:00', '17:00:00', 'Cátedra Administración', 'ACTIVE'),
('EST004', 'AULA104', '2025-11-02', '07:00:00', '09:00:00', 'Estudio Contabilidad', 'ACTIVE'),
('EST005', 'AULA105', '2025-11-03', '17:00:00', '19:00:00', 'Lab Electrónica', 'ACTIVE');

-- ============================================
-- RESUMEN DE DATOS INSERTADOS (SOLO DATOS BASE):
-- ============================================
-- ✅ 13 Usuarios (8 estudiantes + 5 profesores)
-- ✅ 16 Préstamos (diferentes estados y fechas)  
-- ✅ 5 Programas académicos UPTC
-- ✅ Estados: ACTIVE, RESERVED, CANCELLED
-- ✅ Propósitos educativos realistas
-- ============================================