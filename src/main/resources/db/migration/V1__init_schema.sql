-- 1. Crear tabla de Categorías
CREATE TABLE reservation_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- 2. Crear tabla de Equipos
CREATE TABLE equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 3. Crear tabla de Reservas
CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL,
    reserved_by VARCHAR(100) NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_reservation_category FOREIGN KEY (category_id) REFERENCES reservation_category(id)
);

-- 4. Crear tabla intermedia para la relación Many-to-Many entre Reservation y Equipment
CREATE TABLE reservation_equipment (
    reservation_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    PRIMARY KEY (reservation_id, equipment_id),
    CONSTRAINT fk_res_eq_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE,
    CONSTRAINT fk_res_eq_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
);