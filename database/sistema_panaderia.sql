DROP DATABASE IF EXISTS sistema_panaderia;
CREATE DATABASE sistema_panaderia;
USE sistema_panaderia;

CREATE TABLE CLIENTE (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    calle VARCHAR(40) NOT NULL,
    colonia VARCHAR(40) NOT NULL,
    numero INT NOT NULL
);

CREATE TABLE TELEFONO (
    id_telefono INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    etiqueta VARCHAR(40) NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente)
);

CREATE TABLE EMPLEADO (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL
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

INSERT INTO EMPLEADO (nombre_completo)
VALUES 
('Carlos Ramírez'),
('Ana López');

INSERT INTO CLIENTE (nombre_completo, fecha_nacimiento, calle, colonia, numero)
VALUES
('Luis Martínez', '1998-05-12', 'Av. Juárez', 'Centro', 120),
('María González', '2000-08-21', 'Calle Hidalgo', 'Reforma', 45);

INSERT INTO TELEFONO (numero, etiqueta, id_cliente)
VALUES
('5551234567', 'Personal', 1),
('5559876543', 'Trabajo', 1),
('5551112233', 'Personal', 2);

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
(1, 1, 1);

INSERT INTO EXPRESS (id_pedido, folio, pin_seguridad)
VALUES
(2, 1, '1234HASH'),
(3, 2, '1234HASH'),
(4, 3, '1234HASH'),
(5, 4, '1234HASH'),
(6, 5, '1234HASH');

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
(7, 1, 1),
(8, 2, 1),
(9, 1, 1),
(10, 2, 1),
(11, 1, 1);

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

-- CONSULTAS DE PRUEBA

-- 1 Ver pedidos con su empleado
SELECT p.id_pedido, p.estado, e.nombre_completo AS empleado
FROM PEDIDO p
JOIN EMPLEADO e ON p.id_empleado = e.id_empleado;

-- 2 Ver pedidos programados con nombre del cliente
SELECT pe.id_pedido, c.nombre_completo
FROM PROGRAMADO pe
JOIN CLIENTE c ON pe.id_cliente = c.id_cliente;

-- 3 Ver detalle de pedido con nombre del producto
SELECT dp.id_pedido, pr.nombre, dp.cantidad, dp.precio_unitario
FROM DETALLE_PEDIDO dp
JOIN PRODUCTO pr ON dp.id_producto = pr.id_producto;

-- 4 Ver total vendido (pedidos listos o entregados)
SELECT SUM(total) AS total_vendido
FROM PEDIDO
WHERE estado = 'Entregado' OR estado = 'Listo';

-- 5 Ver pagos realizados
SELECT p.id_pago, p.monto, pe.estado
FROM PAGO p
JOIN PEDIDO pe ON p.id_pedido = pe.id_pedido;

-- 6 Cantidad de pedidos por cliente
SELECT c.nombre_completo, COUNT(pe.id_pedido) AS total_pedidos
FROM CLIENTE c
LEFT JOIN PROGRAMADO pe ON c.id_cliente = pe.id_cliente
GROUP BY c.nombre_completo;

-- 7 Total vendido por empleado
SELECT e.nombre_completo, SUM(p.total) AS total_generado
FROM EMPLEADO e
JOIN PEDIDO p ON e.id_empleado = p.id_empleado
GROUP BY e.nombre_completo;

-- 8 Pedidos pendientes
SELECT * 
FROM programado;

DELIMITER //

-- SP
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

-- Trigger
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

UPDATE PEDIDO
SET estado = 'Entregado'
WHERE id_pedido = 1;

SELECT * FROM HISTORIAL_ESTADO;

UPDATE PEDIDO
SET estado = 'Entregado'
WHERE id_pedido = 2;

SELECT h.id_historial, h.id_pedido, h.estado_anterior, 
       h.estado_nuevo, h.fecha_cambio, p.total
FROM HISTORIAL_ESTADO h
JOIN PEDIDO p ON h.id_pedido = p.id_pedido
JOIN EXPRESS e ON p.id_pedido = e.id_pedido
WHERE e.folio = 2
ORDER BY h.fecha_cambio ASC;

SELECT * FROM PAGO;