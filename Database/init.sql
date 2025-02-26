-- usar la base de datos creada
use banco_bps;

-- tabla clientes
create table if not exists clientes (
    id varchar(36) not null,
    nombre varchar(255) not null,
    identificacion varchar(255) not null unique,
    tipo_identificacion varchar(50) not null,
    fecha_nacimiento date not null,
    primary key (id)
);

-- tabla cuentas (modificada con ON DELETE CASCADE)
create table if not exists cuentas (
    numero_cuenta varchar(16) not null,
    fk_id_cliente varchar(36) not null,
    fecha_apertura timestamp not null default current_timestamp,
    hora_apertura time not null,
    estado_cuenta varchar(50) not null,
    saldo decimal(15, 2) not null,
    primary key (numero_cuenta),
    foreign key (fk_id_cliente) references clientes(id) ON DELETE CASCADE
);

-- tabla tipos_movimiento
create table if not exists tipo_movimientos (
    id int auto_increment primary key,
    codigo varchar(10) not null unique,
    descripcion varchar(255) not null,
    fecha_registro timestamp not null default current_timestamp
);

-- tabla movimientos
create table if not exists movimientos (
    numero_referencia varchar(255) not null,
    cuenta_origen varchar(16) not null,
    cuenta_destino varchar(16) not null,
    fecha_movimiento timestamp not null default current_timestamp,
    hora_movimiento time not null,
    fk_codigo_movimiento varchar(10) not null,
    monto decimal(15, 2) not null,
    primary key (numero_referencia),
    foreign key (cuenta_origen) references cuentas(numero_cuenta) ON DELETE CASCADE,
    foreign key (cuenta_destino) references cuentas(numero_cuenta) ON DELETE CASCADE,
    foreign key (fk_codigo_movimiento) references tipo_movimientos(codigo)
);

-- tabla parametros_trama
create table if not exists parametros_trama (
    id_parametro int not null auto_increment,
    nombre_trama varchar(50) not null,
    nombre_campo varchar(50) not null,
    posicion_inicio int not null,
    longitud int not null,
    primary key (id_parametro)
);

-- insertar datos de ejemplo para tipos_movimiento
insert into tipo_movimientos (codigo, descripcion) values
('DEPOSITO', 'Depósito en cuenta'),
('RETIRO', 'Retiro de cuenta'),
('TRANSFER', 'Transferencia entre cuentas');

-- insertar datos de ejemplo para la parametrización de la trama de entrada de movimientos
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('MOVIMIENTO_IN', 'CUENTA_ORIGEN', 1, 16),
('MOVIMIENTO_IN', 'CUENTA_DESTINO', 17, 16),
('MOVIMIENTO_IN', 'FECHA_MOVIMIENTO', 33, 26), -- yyyy-mm-dd hh:mm:ss.nnnnnnnnn
('MOVIMIENTO_IN', 'FK_CODIGO_MOVIMIENTO', 60, 10),
('MOVIMIENTO_IN', 'MONTO', 70, 16);

-- insertar datos de ejemplo para la trama de salida de movimientos.
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('MOVIMIENTO_OUT', 'CODIGO_RESULTADO', 1, 3),
('MOVIMIENTO_OUT', 'DESCRIPCION', 4, 100),
('MOVIMIENTO_OUT', 'NUMERO_REFERENCIA', 104, 16);

-- insertar datos de ejemplo para la parametrización de la trama de entrada de clientes
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('CLIENTE_IN', 'ID', 1, 36),
('CLIENTE_IN', 'NOMBRE', 38, 255),
('CLIENTE_IN', 'IDENTIFICACION', 294, 255),
('CLIENTE_IN', 'TIPO_IDENTIFICACION', 550, 50),
('CLIENTE_IN', 'FECHA_NACIMIENTO', 601, 10); 

-- insertar datos de ejemplo para la trama de salida de clientes
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('CLIENTE_OUT', 'CODIGO_RESULTADO', 1, 3),
('CLIENTE_OUT', 'DESCRIPCION', 4, 100);

-- insertar datos de ejemplo para la parametrización de la trama de entrada de cuentas
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('CUENTA_IN', 'NUMERO_CUENTA', 1, 16),
('CUENTA_IN', 'FK_ID_CLIENTE', 18, 36),
('CUENTA_IN', 'FECHA_APERTURA', 55, 26), 
('CUENTA_IN', 'HORA_APERTURA', 82, 8),  
('CUENTA_IN', 'ESTADO_CUENTA', 91, 50),
('CUENTA_IN', 'SALDO', 142, 18); 

-- insertar datos de ejemplo para la trama de salida de cuentas
insert into parametros_trama (nombre_trama, nombre_campo, posicion_inicio, longitud)
values
('CUENTA_OUT', 'CODIGO_RESULTADO', 1, 3),
('CUENTA_OUT', 'DESCRIPCION', 4, 100);


-- Índices para optimizar búsquedas
create index idx_clientes_identificacion on clientes (identificacion);
create index idx_cuentas_cliente on cuentas (fk_id_cliente);

-- insertar cliente oficial del banco: 
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'f47ac10b-58cc-4372-a567-0e02b2c3d472',
    'BANCO BPS OFICIAL',
    '0801199002256',
    'CI',
    '1990-01-01'
);

-- insertar cuenta oficial del banco con la nomenclatura especial: 
INSERT INTO cuentas(numero_cuenta,fk_id_cliente,fecha_apertura,hora_apertura,estado_cuenta,saldo)
VALUES(
    'A000000000000001','f47ac10b-58cc-4372-a567-0e02b2c3d472','1990-05-15 14:30:00','14:30:00', 'ACT', '999999999999'
);



-- ------------------->>>> Registro de clientes & cuentas <<<-----------------------------
-- Cliente 1
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'a1b2c3d4-e5f6-4a1b-8c2d-123456789abc',
    'Carlos Rodriguez',
    '0801198805123',
    'CI',
    '1988-05-12'
);

INSERT INTO cuentas(numero_cuenta, fk_id_cliente, fecha_apertura, hora_apertura, estado_cuenta, saldo)
VALUES(
    '3254167895421563', 'a1b2c3d4-e5f6-4a1b-8c2d-123456789abc', '2015-03-10 09:45:00', '09:45:00', 'ACT', '25000.75'
);

-- Cliente 2
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'b2c3d4e5-f6a7-5b2c-9d3e-234567890bcd',
    'Maria Gonzalez',
    '0801199204067',
    'CI',
    '1992-04-06'
);

INSERT INTO cuentas(numero_cuenta, fk_id_cliente, fecha_apertura, hora_apertura, estado_cuenta, saldo)
VALUES(
    '9876543210123456', 'b2c3d4e5-f6a7-5b2c-9d3e-234567890bcd', '2017-08-22 14:15:00', '14:15:00', 'ACT', '18750.50'
);

-- Cliente 3
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'c3d4e5f6-a7b8-6c3d-0e4f-345678901cde',
    'Javier Fernandez',
    '0801198511294',
    'CI',
    '1985-11-29'
);

INSERT INTO cuentas(numero_cuenta, fk_id_cliente, fecha_apertura, hora_apertura, estado_cuenta, saldo)
VALUES(
    '1234987656781234', 'c3d4e5f6-a7b8-6c3d-0e4f-345678901cde', '2010-12-05 11:30:00', '11:30:00', 'ACT', '42800.25'
);

-- Cliente 4
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'd4e5f6a7-b8c9-7d4e-1f5a-456789012def',
    'Ana Martinez',
    '0801199707315',
    'CI',
    '1997-07-31'
);

INSERT INTO cuentas(numero_cuenta, fk_id_cliente, fecha_apertura, hora_apertura, estado_cuenta, saldo)
VALUES(
    '5678123498765432', 'd4e5f6a7-b8c9-7d4e-1f5a-456789012def', '2020-02-18 15:20:00', '15:20:00', 'ACT', '7500.00'
);

-- Cliente 5
INSERT INTO clientes (id, nombre, identificacion, tipo_identificacion, fecha_nacimiento)
VALUES (
    'e5f6a7b8-c9d0-8e5f-2g6b-567890123efg',
    'Luis Morales',
    '0801198309087',
    'CI',
    '1983-09-08'
);

INSERT INTO cuentas(numero_cuenta, fk_id_cliente, fecha_apertura, hora_apertura, estado_cuenta, saldo)
VALUES(
    '9876123487654321', 'e5f6a7b8-c9d0-8e5f-2g6b-567890123efg', '2012-06-30 10:10:00', '10:10:00', 'ACT', '65320.80'
);