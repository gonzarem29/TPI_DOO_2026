-- Esquema SQLite - Aguas Vital SA
-- TP Integrador DOO 2026 - UBP

PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS detallePedido;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS empleado;
DROP TABLE IF EXISTS distribuidor;
DROP TABLE IF EXISTS precio;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS tipoProducto;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS telefono;
DROP TABLE IF EXISTS domicilio;
DROP TABLE IF EXISTS barrio;
DROP TABLE IF EXISTS zona;

CREATE TABLE zona (
    codigo TEXT PRIMARY KEY,
    nombre TEXT NOT NULL
);

CREATE TABLE barrio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    codigoZona TEXT NOT NULL,
    FOREIGN KEY (codigoZona) REFERENCES zona(codigo)
);

CREATE TABLE domicilio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    calle TEXT NOT NULL,
    numero TEXT NOT NULL,
    idBarrio INTEGER,
    FOREIGN KEY (idBarrio) REFERENCES barrio(id)
);

CREATE TABLE telefono (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero TEXT NOT NULL,
    tipo TEXT
);

CREATE TABLE cliente (
    nroCliente INTEGER PRIMARY KEY AUTOINCREMENT,
    documento TEXT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    razonSocial TEXT,
    idDomicilio INTEGER,
    idTelefono INTEGER,
    FOREIGN KEY (idDomicilio) REFERENCES domicilio(id),
    FOREIGN KEY (idTelefono) REFERENCES telefono(id)
);

CREATE TABLE tipoProducto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL
);

CREATE TABLE producto (
    codProducto TEXT PRIMARY KEY,
    nomProducto TEXT NOT NULL,
    idTipoProducto INTEGER,
    FOREIGN KEY (idTipoProducto) REFERENCES tipoProducto(id)
);

CREATE TABLE precio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codProducto TEXT NOT NULL,
    precio REAL NOT NULL,
    fechaDesde TEXT NOT NULL,
    fechaHasta TEXT,
    FOREIGN KEY (codProducto) REFERENCES producto(codProducto)
);

CREATE TABLE stock (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codProducto TEXT NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (codProducto) REFERENCES producto(codProducto)
);

CREATE TABLE distribuidor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cantMaxEntrega INTEGER NOT NULL DEFAULT 0,
    radio TEXT,
    codigoZona TEXT,
    FOREIGN KEY (codigoZona) REFERENCES zona(codigo)
);

CREATE TABLE empleado (
    legajo INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    documento TEXT,
    idDomicilio INTEGER,
    idTelefono INTEGER,
    tipo TEXT NOT NULL CHECK(tipo IN ('OPERADOR','ENCARGADO','PRESIDENTE')),
    usuario TEXT,
    contrasenia TEXT,
    FOREIGN KEY (idDomicilio) REFERENCES domicilio(id),
    FOREIGN KEY (idTelefono) REFERENCES telefono(id)
);

CREATE TABLE factura (
    nroFactura INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha TEXT NOT NULL,
    nroCliente INTEGER NOT NULL,
    detalleFactura TEXT,
    nroPedido INTEGER,
    FOREIGN KEY (nroCliente) REFERENCES cliente(nroCliente),
    FOREIGN KEY (nroPedido) REFERENCES pedido(nroPedido)
);

CREATE TABLE pedido (
    nroPedido INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha TEXT NOT NULL,
    fechaEntrega TEXT,
    estado TEXT NOT NULL DEFAULT 'PENDIENTE' CHECK(estado IN ('PENDIENTE','ENVIADO','ENTREGADO','CANCELADO','FACTURADO')),
    nroCliente INTEGER NOT NULL,
    legajoOperador INTEGER NOT NULL,
    legajoOpCancela INTEGER,
    idDistribuidor INTEGER,
    codigoZona TEXT,
    nroFactura INTEGER,
    FOREIGN KEY (nroCliente) REFERENCES cliente(nroCliente),
    FOREIGN KEY (legajoOperador) REFERENCES empleado(legajo),
    FOREIGN KEY (legajoOpCancela) REFERENCES empleado(legajo),
    FOREIGN KEY (idDistribuidor) REFERENCES distribuidor(id),
    FOREIGN KEY (codigoZona) REFERENCES zona(codigo),
    FOREIGN KEY (nroFactura) REFERENCES factura(nroFactura)
);

CREATE TABLE detallePedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nroPedido INTEGER NOT NULL,
    codProducto TEXT NOT NULL,
    precio REAL NOT NULL,
    cantidad INTEGER NOT NULL,
    FOREIGN KEY (nroPedido) REFERENCES pedido(nroPedido),
    FOREIGN KEY (codProducto) REFERENCES producto(codProducto)
);

-- Datos de prueba
INSERT INTO zona (codigo, nombre) VALUES
    ('ZONA-NORTE', 'Zona Norte'),
    ('ZONA-SUR', 'Zona Sur'),
    ('ZONA-ESTE', 'Zona Este'),
    ('ZONA-OESTE', 'Zona Oeste');

INSERT INTO barrio (nombre, codigoZona) VALUES
    ('Centro', 'ZONA-NORTE'),
    ('Nueva Córdoba', 'ZONA-SUR'),
    ('Cerro de las Rosas', 'ZONA-ESTE'),
    ('Urca', 'ZONA-OESTE');

INSERT INTO domicilio (calle, numero, idBarrio) VALUES
    ('Av. Colón', '123', 1),
    ('Av. Vucetich', '456', 2),
    ('Recta Martinoli', '789', 3),
    ('Av. Gauss', '101', 4);

INSERT INTO telefono (numero, tipo) VALUES
    ('351-1234567', 'FIJO'),
    ('351-7654321', 'MOVIL'),
    ('351-1112233', 'FIJO'),
    ('351-9988776', 'MOVIL');

INSERT INTO cliente (documento, nombre, apellido, razonSocial, idDomicilio, idTelefono) VALUES
    ('20123456', 'Carlos', 'García', NULL, 1, 1),
    ('20987654', 'María', 'López', 'Agua Pura SRL', 2, 2);

INSERT INTO tipoProducto (nombre) VALUES
    ('Agua Mineral'),
    ('Bebida Saborizada'),
    ('Soda');

INSERT INTO producto (codProducto, nomProducto, idTipoProducto) VALUES
    ('AGU-001', 'Agua Mineral 500ml', 1),
    ('AGU-002', 'Agua Mineral 1.5L', 1),
    ('AGU-003', 'Agua Mineral 5L', 1),
    ('BEB-001', 'Naranja 500ml', 2),
    ('BEB-002', 'Pomelo 500ml', 2),
    ('SOD-001', 'Soda 1L', 3);

INSERT INTO precio (codProducto, precio, fechaDesde) VALUES
    ('AGU-001', 150.00, '2026-01-01'),
    ('AGU-002', 250.00, '2026-01-01'),
    ('AGU-003', 400.00, '2026-01-01'),
    ('BEB-001', 200.00, '2026-01-01'),
    ('BEB-002', 200.00, '2026-01-01'),
    ('SOD-001', 180.00, '2026-01-01');

INSERT INTO stock (codProducto, cantidad) VALUES
    ('AGU-001', 500),
    ('AGU-002', 300),
    ('AGU-003', 200),
    ('BEB-001', 400),
    ('BEB-002', 350),
    ('SOD-001', 250);

INSERT INTO distribuidor (cantMaxEntrega, radio, codigoZona) VALUES
    (50, 'Zona Norte - Centro', 'ZONA-NORTE'),
    (30, 'Zona Sur - Nueva Cordoba', 'ZONA-SUR');

INSERT INTO empleado (nombre, apellido, documento, idDomicilio, idTelefono, tipo, usuario, contrasenia) VALUES
    ('Juan', 'Pérez', '30123456', 3, 3, 'OPERADOR', 'jperez', '1234'),
    ('Ana', 'Martínez', '30987654', 4, 4, 'ENCARGADO', NULL, NULL);

INSERT INTO pedido (fecha, estado, nroCliente, legajoOperador, codigoZona) VALUES
    ('2026-06-01 10:00:00', 'PENDIENTE', 1, 1, 'ZONA-NORTE'),
    ('2026-06-02 14:30:00', 'ENTREGADO', 2, 1, 'ZONA-SUR');

INSERT INTO detallePedido (nroPedido, codProducto, precio, cantidad) VALUES
    (1, 'AGU-001', 150.00, 10),
    (1, 'BEB-001', 200.00, 5),
    (2, 'AGU-002', 250.00, 8);
