-- 1. Desactivar temporalmente la verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Vaciar las tablas relacionadas
TRUNCATE TABLE reservation_equipment;
TRUNCATE TABLE reservation;

-- 3. Reactivar la verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- 4. Añadir las nuevas columnas obligatorias
ALTER TABLE reservation 
ADD COLUMN start_time DATETIME NOT NULL,
ADD COLUMN end_time DATETIME NOT NULL;