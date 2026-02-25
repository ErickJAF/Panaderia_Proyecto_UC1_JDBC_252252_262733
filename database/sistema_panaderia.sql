DROP DATABASE IF EXISTS sistema_panaderia;
CREATE DATABASE sistema_panaderia;
USE sistema_panaderia;

CREATE TABLE USUARIO (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(40) NOT NULL UNIQUE,
    rol ENUM('Cliente','Empleado') NOT NULL,
    contrasena VARCHAR(64) NOT NULL
);

CREATE TABLE EMPLEADO (
    id_empleado INT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_empleado) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE CLIENTE (
    id_cliente INT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    calle VARCHAR(40) NOT NULL,
    colonia VARCHAR(40) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE TELEFONO (
    id_telefono INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    etiqueta VARCHAR(40) NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente)
);

CREATE TABLE PRODUCTO (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    disponible BOOLEAN NOT NULL
);

CREATE TABLE PEDIDO (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL,
    estado ENUM('Pendiente', 'Listo', 'Entregado', 'Cancelado', 'No Entregado') 
        NOT NULL DEFAULT 'Pendiente',
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (descuento >= 0),
    total DECIMAL(10,2) NOT NULL CHECK (total >= 0),
    id_empleado INT NOT NULL,
    FOREIGN KEY (id_empleado) REFERENCES EMPLEADO(id_empleado)
);

CREATE TABLE CUPON (
    id_cupon INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    descuento DECIMAL(10,2) NOT NULL CHECK (descuento >= 0),
    usos_maximos INT NOT NULL CHECK (usos_maximos >= 0),
    usos_actuales INT NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL
);

CREATE TABLE PROGRAMADO (
    id_pedido INT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_cupon INT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido),
    FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente),
    FOREIGN KEY (id_cupon) REFERENCES CUPON(id_cupon)
);

CREATE TABLE EXPRESS (
    id_pedido INT PRIMARY KEY,
    folio INT NOT NULL UNIQUE,
    pin_seguridad VARCHAR(255) NOT NULL,
    fecha_listo DATETIME DEFAULT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido)
);

CREATE TABLE DETALLE_PEDIDO (
    id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    nota VARCHAR(200),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
);

CREATE TABLE PAGO (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    fecha_pago DATETIME NOT NULL,
    monto DECIMAL(10,2) NOT NULL CHECK (monto >= 0),
    metodo_pago VARCHAR(30) NOT NULL,
    id_pedido INT NOT NULL UNIQUE,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido)
);

CREATE TABLE HISTORIAL_ESTADO (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    estado_anterior ENUM('Pendiente', 'Listo', 'Entregado', 'Cancelado', 'No Entregado'),
    estado_nuevo ENUM('Pendiente', 'Listo', 'Entregado', 'Cancelado', 'No Entregado'),
    fecha_cambio DATETIME NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido)
);

-- REGISTROS DE PRUEBA

INSERT INTO USUARIO (nombreUsuario, rol, contrasena)
VALUES
('carlos.emp', 'Empleado', SHA2('1234',256)),
('ana.emp', 'Empleado', SHA2('1234',256)),
('luis.cli', 'Cliente', SHA2('1234',256)),
('maria.cli', 'Cliente', SHA2('1234',256));

INSERT INTO EMPLEADO (id_empleado, nombre_completo)
VALUES
(1, 'Carlos Ramírez'),
(2, 'Ana López');

INSERT INTO CLIENTE (id_cliente, nombre_completo, fecha_nacimiento, calle, colonia)
VALUES
(3, 'Luis Martínez', '1998-05-12', 'Av. Juárez', 'Centro'),
(4, 'María González', '2000-08-21', 'Calle Hidalgo', 'Reforma');

INSERT INTO TELEFONO (numero, etiqueta, id_cliente)
VALUES
('5551234567', 'Personal', 3),
('5559876543', 'Trabajo', 3),
('5551112233', 'Personal', 4);

-- =========================
-- PRODUCTOS
-- =========================
INSERT INTO PRODUCTO (nombre, tipo, descripcion, precio, disponible)
VALUES
('Pan de Caja Integral', 'Panadería', 'Pan integral de 500g', 40.00, TRUE),
('Baguette Francesa', 'Panadería', 'Baguette recién horneada', 25.00, TRUE),
('Croissant de Mantequilla', 'Panadería', 'Croissant hojaldrado con mantequilla', 30.00, TRUE),
('Pan de Chocolate', 'Panadería', 'Pan dulce relleno de chocolate', 35.00, TRUE),
('Concha Tradicional', 'Panadería', 'Pan dulce con cobertura de azúcar', 20.00, TRUE),
('Pan de Queso', 'Panadería', 'Pan salado relleno de queso', 28.00, TRUE),
('Galletas Artesanales', 'Panadería', 'Paquete de 6 galletas surtidas', 25.00, TRUE),
('Pan de Nuez', 'Panadería', 'Pan dulce con nuez troceada', 32.00, TRUE),
('Rollito de Canela', 'Panadería', 'Rollito suave con canela y azúcar', 30.00, TRUE),
('Empanada de Fruta', 'Panadería', 'Empanada rellena de fruta natural', 27.00, TRUE);

INSERT INTO CUPON (codigo, descuento, usos_maximos, usos_actuales, activo, fecha_inicio, fecha_fin)
VALUES
('DESC10', 10.00, 100, 0, TRUE, '2025-01-01', '2025-12-31');

INSERT INTO PEDIDO (fecha_creacion, estado, subtotal, descuento, total, id_empleado)
VALUES
(CURDATE(), 'Pendiente', 175.00, 10.00, 165.00, 1),
(CURDATE(), 'Pendiente', 50.00, 0.00, 50.00, 1),
(CURDATE(), 'Pendiente', 120.00, 10.00, 110.00, 2),
(CURDATE(), 'Pendiente', 200.00, 15.00, 185.00, 1),
(CURDATE(), 'Pendiente', 75.00, 5.00, 70.00, 2),
(CURDATE(), 'Pendiente', 90.00, 0.00, 90.00, 1);

INSERT INTO PROGRAMADO (id_pedido, id_cliente, id_cupon)
VALUES
(1, 3, 1);

INSERT INTO EXPRESS (id_pedido, folio, pin_seguridad)
VALUES
(2, 1, SHA2('1234', 256)),
(3, 2, SHA2('5678', 256)),
(4, 3, SHA2('4321', 256)),
(5, 4, SHA2('9876', 256)),
(6, 5, SHA2('1111', 256));

INSERT INTO DETALLE_PEDIDO (cantidad, nota, precio_unitario, id_pedido, id_producto)
VALUES
(2, 'Sin corteza', 40.00, 1, 1),
(1, '', 25.00, 1, 2),
(3, 'Muy mantequilla', 30.00, 1, 3),
(1, '', 35.00, 1, 4),
(2, '', 20.00, 1, 5),
(1, '', 28.00, 1, 6),
(1, '', 25.00, 1, 7),
(1, '', 32.00, 1, 8),
(2, '', 30.00, 1, 9),
(1, '', 27.00, 1, 10);

INSERT INTO PEDIDO (fecha_creacion, estado, subtotal, descuento, total, id_empleado)
VALUES
(CURDATE(), 'Pendiente', 120.00, 10.00, 110.00, 1),
(CURDATE(), 'Pendiente', 85.00, 5.00, 80.00, 2),
(CURDATE(), 'Pendiente', 150.00, 15.00, 135.00, 1),
(CURDATE(), 'Pendiente', 60.00, 0.00, 60.00, 2),
(CURDATE(), 'Pendiente', 200.00, 20.00, 180.00, 1);

INSERT INTO PROGRAMADO (id_pedido, id_cliente, id_cupon)
VALUES
(7, 3, 1),
(8, 4, 1),
(9, 3, 1),
(10, 4, 1),
(11, 3, 1);

INSERT INTO DETALLE_PEDIDO (cantidad, nota, precio_unitario, id_pedido, id_producto)
VALUES
(2, 'Sin corteza', 40.00, 2, 1),
(1, '', 25.00, 2, 2),
(1, 'Extra mantequilla', 30.00, 3, 3),
(2, '', 20.00, 3, 5),
(2, '', 40.00, 7, 1),
(1, '', 25.00, 7, 2),
(1, 'Extra mantequilla', 30.00, 8, 3),
(2, '', 20.00, 8, 5),
(1, '', 28.00, 9, 6),
(2, '', 25.00, 9, 7),
(2, '', 30.00, 10, 9),
(1, '', 27.00, 10, 10),
(3, 'Sin azúcar', 35.00, 11, 4),
(2, '', 32.00, 11, 8);

DELIMITER //

CREATE PROCEDURE sp_crear_pedido_programado(
    IN p_fecha DATETIME,
    IN p_estado VARCHAR(30),
    IN p_subtotal DECIMAL(10,2),
    IN p_descuento DECIMAL(10,2),
    IN p_total DECIMAL(10,2),
    IN p_id_empleado INT,
    IN p_id_cliente INT,
    IN p_id_cupon INT,
    OUT p_id_generado INT
)
BEGIN
    DECLARE nuevo_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    INSERT INTO PEDIDO(
        fecha_creacion,
        estado,
        subtotal,
        descuento,
        total,
        id_empleado
    )
    VALUES(
        p_fecha,
        p_estado,
        p_subtotal,
        p_descuento,
        p_total,
        p_id_empleado
    );

    SET nuevo_id = LAST_INSERT_ID();

    INSERT INTO PROGRAMADO(
        id_pedido,
        id_cliente,
        id_cupon
    )
    VALUES(
        nuevo_id,
        p_id_cliente,
        p_id_cupon
    );

    SET p_id_generado = nuevo_id;

    COMMIT;

END //

CREATE TRIGGER trg_historial_estado
BEFORE UPDATE ON PEDIDO
FOR EACH ROW
BEGIN
    IF OLD.estado <> NEW.estado THEN
        INSERT INTO HISTORIAL_ESTADO(
            id_pedido,
            estado_anterior,
            estado_nuevo,
            fecha_cambio
        )
        VALUES(
            OLD.id_pedido,
            OLD.estado,
            NEW.estado,
            NOW()
        );
    END IF;
END //

DELIMITER ;

alter table usuario add column activo boolean not null default true;