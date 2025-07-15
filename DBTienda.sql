create database DBTiendaKinal;
use DBTiendaKinal;

create table Clientes (
	idClientes int auto_increment,
    nombreClientes varchar(64),
    email varchar(128),
    telefono varchar(12),
    DPI varchar(16),
    primary key fk_clientes (idClientes)
);

create table Empleados (
	idEmpleados int auto_increment,
    nombreEmpleados varchar(64),
    puesto varchar(16),
    email varchar(128),
    estado ENUM('activo', 'Inactivo') default 'activo',
    primary key pk_empleados (idEmpleados)
);

create table Productos (
	idProductos int auto_increment,
    nombreProductos varchar(64),
    descripcion varchar(128),
    precio decimal(10,2),
    primary key pk_productos (idProductos)
);

create table Inventario (
	idInventario int auto_increment,
    idProductos int,
    cantidad int,
    estado ENUM('Disponible', 'Agotado') default 'Disponible',
    primary key pk_inventario (idInventario),
    constraint fk_inventario_productos foreign key (idProductos)
		references Productos (idProductos) 
);

create table Facturas (
	idFacturas int auto_increment,
    fecha date,
    total decimal(10,2),
    idClientes int,
    idEmpleados int,
    primary key pk_facturas (idFacturas),
    constraint fk_facturas_clientes foreign key (idClientes)
		references Clientes (idClientes),
	constraint fk_facturas_empleados foreign key (idEmpleados)
		references Empleados (idEmpleados)
);

create table DetalleFacturas (
	idDetalles int auto_increment,
    idFacturas int,
    idProductos int,
    cantidad int,
    subtotal decimal(10,2),
    primary key pk_detalle_facturas (idDetalles),
    constraint fk_detalle_facturas_facturas foreign key (idFacturas)
		references Facturas (idFacturas),
	constraint fk_detalle_facturas_productos foreign key (idProductos)
		references Productos (idProductos)
);
