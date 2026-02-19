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

-- =========================
-- TABLA INGREDIENTE
-- =========================
CREATE TABLE INGREDIENTE (
    id_ingrediente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE PRODUCTO_INGREDIENTE (
    id_producto INT,
    id_ingrediente INT,
    PRIMARY KEY (id_producto, id_ingrediente),
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto),
    FOREIGN KEY (id_ingrediente) REFERENCES INGREDIENTE(id_ingrediente)
);

CREATE TABLE PEDIDO (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion DATE NOT NULL,
    estado ENUM('Pendiente', 'En Preparación', 'Listo', 'Entregado', 'Cancelado', 'No Reclamado') NOT NULL DEFAULT 'Pendiente',
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
    folio VARCHAR(20) NOT NULL,
    pin_seguridad VARCHAR(255) NOT NULL,
    fecha_preparacion DATE NOT NULL,
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
    fecha_pago DATE NOT NULL,
    monto DECIMAL(10,2) NOT NULL CHECK (monto >= 0),
    metodo_pago VARCHAR(30) NOT NULL,
    id_pedido INT NOT NULL,
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
('Pizza Pepperoni', 'Comida', 'Pizza grande con pepperoni', 150.00, TRUE),
('Hamburguesa Clásica', 'Comida', 'Carne, queso y vegetales', 95.00, TRUE),
('Refresco', 'Bebida', 'Refresco 600ml', 25.00, TRUE);

INSERT INTO INGREDIENTE (nombre)
VALUES
('Queso'),
('Pepperoni'),
('Carne'),
('Pan'),
('Lechuga');

INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente)
VALUES
(1,1),
(1,2),
(2,3),
(2,4),
(2,5);

INSERT INTO CUPON (codigo, descuento, usos_maximos, usos_actuales, activo, fecha_inicio, fecha_fin)
VALUES
('DESC10', 10.00, 100, 0, TRUE, '2025-01-01', '2025-12-31');

INSERT INTO PEDIDO (fecha_creacion, estado, subtotal, descuento, total, id_empleado)
VALUES
(CURDATE(), 'Pendiente', 175.00, 10.00, 165.00, 1),
(CURDATE(), 'Listo', 95.00, 0.00, 95.00, 2);

INSERT INTO PROGRAMADO (id_pedido, id_cliente, id_cupon)
VALUES
(1, 1, 1);

INSERT INTO EXPRESS (id_pedido, folio, pin_seguridad, fecha_preparacion)
VALUES
(2, 'EXP-001', '1234HASH', CURDATE());

INSERT INTO DETALLE_PEDIDO (cantidad, nota, precio_unitario, id_pedido, id_producto)
VALUES
(1, 'Sin orilla', 150.00, 1, 1),
(1, NULL, 25.00, 1, 3),
(1, NULL, 95.00, 2, 2);

INSERT INTO PAGO (fecha_pago, monto, metodo_pago, id_pedido)
VALUES
(CURDATE(), 165.00, 'Tarjeta', 1),
(CURDATE(), 95.00, 'Efectivo', 2);

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

-- 5 Ver ingredientes de un producto
SELECT pr.nombre AS producto, i.nombre AS ingrediente
FROM PRODUCTO pr
JOIN PRODUCTO_INGREDIENTE pi ON pr.id_producto = pi.id_producto
JOIN INGREDIENTE i ON pi.id_ingrediente = i.id_ingrediente
WHERE pr.id_producto = 1;

-- 6 Ver pagos realizados
SELECT p.id_pago, p.monto, pe.estado
FROM PAGO p
JOIN PEDIDO pe ON p.id_pedido = pe.id_pedido;

-- 7 Cantidad de pedidos por cliente
SELECT c.nombre_completo, COUNT(pe.id_pedido) AS total_pedidos
FROM CLIENTE c
LEFT JOIN PROGRAMADO pe ON c.id_cliente = pe.id_cliente
GROUP BY c.nombre_completo;

-- 8 Total vendido por empleado
SELECT e.nombre_completo, SUM(p.total) AS total_generado
FROM EMPLEADO e
JOIN PEDIDO p ON e.id_empleado = p.id_empleado
GROUP BY e.nombre_completo;

DELIMITER //

CREATE PROCEDURE sp_crear_pedido_programado(
    IN p_fecha DATE,
    IN p_estado VARCHAR(30),
    IN p_subtotal DECIMAL(10,2),
    IN p_descuento DECIMAL(10,2),
    IN p_total DECIMAL(10,2),
    IN p_id_empleado INT,
    IN p_id_cliente INT,
    IN p_id_cupon INT
)
BEGIN
    DECLARE nuevo_id INT;

    -- Insertar en PEDIDO
    INSERT INTO PEDIDO(fecha_creacion, estado, subtotal, descuento, total, id_empleado)
    VALUES(p_fecha, p_estado, p_subtotal, p_descuento, p_total, p_id_empleado);

    SET nuevo_id = LAST_INSERT_ID();

    -- Insertar en PROGRAMADO
    INSERT INTO PROGRAMADO(id_pedido, id_cliente, id_cupon)
    VALUES(nuevo_id, p_id_cliente, p_id_cupon);

END //

DELIMITER ;

CALL sp_crear_pedido_programado(
    CURDATE(),
    'Pendiente',
    200.00,
    10.00,
    190.00,
    1,
    1,
    1
);

DELIMITER //

CREATE TRIGGER trg_actualizar_usos_cupon
AFTER INSERT ON PROGRAMADO
FOR EACH ROW
BEGIN
    IF NEW.id_cupon IS NOT NULL THEN
        
        UPDATE CUPON
        SET usos_actuales = usos_actuales + 1
        WHERE id_cupon = NEW.id_cupon;

        -- Desactivar si alcanza el máximo
        UPDATE CUPON
        SET activo = FALSE
        WHERE id_cupon = NEW.id_cupon
        AND usos_actuales >= usos_maximos;

    END IF;
END //

DELIMITER ;

