drop database if exists DBTiendaKinal;
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

-- CRUD --
-- crud Clientes --
delimiter $$
create procedure sp_AgregarCliente(
	in p_nombreClientes varchar(64),
	in p_email varchar(128),
	in p_telefono varchar(12),
	in p_DPI varchar(16)
)
begin
	insert into Clientes (nombreClientes, email, telefono, DPI)
	values (p_nombreClientes, p_email, p_telefono, p_DPI);
end$$
delimiter ;
call sp_AgregarCliente("Juan Ceballos","juanCeballos@gmail.com","45794555","0203 04050 1234");

delimiter $$
create procedure sp_ListarClientes()
begin
	select * from Clientes;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarCliente(
	in p_idClientes int,
	in p_nombreClientes varchar(64),
	in p_email varchar(128),
	in p_telefono varchar(12),
	in p_DPI varchar(16)
)
begin
	update Clientes
	set nombreClientes = p_nombreClientes,
		email = p_email,
		telefono = p_telefono,
		DPI = p_DPI
	where idClientes = p_idClientes;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarCliente(in p_idClientes int)
begin
	delete from Clientes where idClientes = p_idClientes;
end$$
delimiter ;

-- crud Empleados --
delimiter $$
create procedure sp_AgregarEmpleado(
	in p_nombreEmpleados varchar(64),
	in p_puesto varchar(16),
	in p_email varchar(128),
	in p_estado ENUM('activo', 'Inactivo')
)
begin
	insert into Empleados (nombreEmpleados, puesto, email, estado)
	values (p_nombreEmpleados, p_puesto, p_email, p_estado);
end$$
delimiter ;

delimiter $$
create procedure sp_ListarEmpleados()
begin
	select * from Empleados;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarEmpleado(
	in p_idEmpleados int,
	in p_nombreEmpleados varchar(64),
	in p_puesto varchar(16),
	in p_email varchar(128),
	in p_estado ENUM('activo', 'Inactivo')
)
begin
	update Empleados
	set nombreEmpleados = p_nombreEmpleados,
		puesto = p_puesto,
		email = p_email,
		estado = p_estado
	where idEmpleados = p_idEmpleados;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarEmpleado(in p_idEmpleados int)
begin
	delete from Empleados where idEmpleados = p_idEmpleados;
end$$
delimiter ;

-- crud Productos --
delimiter $$
create procedure sp_AgregarProducto(
	in p_nombreProductos varchar(64),
	in p_descripcion varchar(128),
	in p_precio decimal(10,2)
)
begin
	insert into Productos (nombreProductos, descripcion, precio)
	values (p_nombreProductos, p_descripcion, p_precio);
end$$
delimiter ;

delimiter $$
create procedure sp_ListarProductos()
begin
	select * from Productos;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarProducto(
	in p_idProductos int,
	in p_nombreProductos varchar(64),
	in p_descripcion varchar(128),
	in p_precio decimal(10,2)
)
begin
	update Productos
	set nombreProductos = p_nombreProductos,
		descripcion = p_descripcion,
		precio = p_precio
	where idProductos = p_idProductos;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarProducto(in p_idProductos int)
begin
	delete from Productos where idProductos = p_idProductos;
end$$
delimiter ;

-- crud Inventario --
delimiter $$
create procedure sp_AgregarInventario(
	in p_idProductos int,
	in p_cantidad int,
	in p_estado ENUM('Disponible', 'Agotado')
)
begin
	insert into Inventario (idProductos, cantidad, estado)
	values (p_idProductos, p_cantidad, p_estado);
end$$
delimiter ;

delimiter $$
create procedure sp_ListarInventario()
begin
	select * from Inventario;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarInventario(
	in p_idInventario int,
	in p_idProductos int,
	in p_cantidad int,
	in p_estado ENUM('Disponible', 'Agotado')
)
begin
	update Inventario
	set idProductos = p_idProductos,
		cantidad = p_cantidad,
		estado = p_estado
	where idInventario = p_idInventario;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarInventario(in p_idInventario int)
begin
	delete from Inventario where idInventario = p_idInventario;
end$$
delimiter ;

-- crud Facturas -- 
delimiter $$
create procedure sp_AgregarFactura(
	in p_fecha date,
	in p_total decimal(10,2),
	in p_idClientes int,
	in p_idEmpleados int
)
begin
	insert into Facturas (fecha, total, idClientes, idEmpleados)
	values (p_fecha, p_total, p_idClientes, p_idEmpleados);
end$$
delimiter ;

delimiter $$
create procedure sp_ListarFacturas()
begin
	select * from Facturas;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarFactura(
	in p_idFacturas int,
	in p_fecha date,
	in p_total decimal(10,2),
	in p_idClientes int,
	in p_idEmpleados int
)
begin
	update Facturas
	set fecha = p_fecha,
		total = p_total,
		idClientes = p_idClientes,
		idEmpleados = p_idEmpleados
	where idFacturas = p_idFacturas;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarFactura(in p_idFacturas int)
begin
	delete from Facturas where idFacturas = p_idFacturas;
end$$
delimiter ;

-- crud Detalle de Facturas -- 
delimiter $$
create procedure sp_AgregarDetalleFactura(
	in p_idFacturas int,
	in p_idProductos int,
	in p_cantidad int,
	in p_subtotal decimal(10,2)
)
begin
	insert into DetalleFacturas (idFacturas, idProductos, cantidad, subtotal)
	values (p_idFacturas, p_idProductos, p_cantidad, p_subtotal);
end$$
delimiter ;

delimiter $$
create procedure sp_ListarDetalleFacturas()
begin
	select * from DetalleFacturas;
end$$
delimiter ;

delimiter $$
create procedure sp_ActualizarDetalleFactura(
	in p_idDetalles int,
	in p_idFacturas int,
	in p_idProductos int,
	in p_cantidad int,
	in p_subtotal decimal(10,2)
)
begin
	update DetalleFacturas
	set idFacturas = p_idFacturas,
		idProductos = p_idProductos,
		cantidad = p_cantidad,
		subtotal = p_subtotal
	where idDetalles = p_idDetalles;
end$$
delimiter ;

delimiter $$
create procedure sp_EliminarDetalleFactura(in p_idDetalles int)
begin
	delete from DetalleFacturas where idDetalles = p_idDetalles;
end$$
delimiter ;