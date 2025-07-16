package org.josezeta.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.josezeta.database.Conexion;
import org.josezeta.model.Cliente;
import org.josezeta.model.Empleado;
import org.josezeta.model.Factura;
import org.josezeta.system.Main;

/**
 * FXML Controller class
 *
 * @author RV
 */
public class FacturaController implements Initializable {

    @FXML private Button btnRegresar, btnAnterior, btnNuevo, btnEditar, btnEliminar, btnSiguiente, btnBuscar;
    @FXML private TableView<Factura> tablaFacturas;
    @FXML private TableColumn colId, colFecha, colTotal,  colMetodoPago, colCliente, colEmpleado;
    @FXML private TextField txtID, txtTotal, txtMetodoPago, txtCliente, txtEmpleado, txtBuscar;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Cliente> cbxCliente;
    @FXML private ComboBox<Empleado> cbxEmpleado;
    
    private ObservableList<Factura> listarFacturas = FXCollections.observableArrayList();
    private Factura modeloFactura;
    private Cliente cliente;
    private Empleado empleado;
    
    private enum EstadoFormulario{AGREGAR, EDITAR, ELIMINAR, NINGUNA}
    private EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;
    
    private Main principal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarColumnas();
        cargarTablaFacturas();
        cargarFormulario();
        cargarClienteComboBox();
        cargarEmpleadoComboBox();
        tablaFacturas.setOnMouseClicked(eventHandler -> cargarFormulario());
        dpFecha.setDisable(true);
        txtTotal.setDisable(true);
        txtMetodoPago.setDisable(true);
        txtCliente.setDisable(true);
        txtEmpleado.setDisable(true);
    }    
    
    private void configurarColumnas(){
        colId.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, LocalDate>("fechaEmision"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("total"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<Factura, String>("metodoPago"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoCliente"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoEmpleado"));
    }
    
    public void cargarTablaFacturas(){
        listarFacturas = FXCollections.observableArrayList(listarFacturas());
        tablaFacturas.setItems(listarFacturas);
        tablaFacturas.getSelectionModel().selectFirst();
        cargarFormulario();
    }
    
    private void cargarFormulario(){
       //obtener un modelo-> Mascota0
       Factura facturaSeleccionado = tablaFacturas.getSelectionModel().getSelectedItem();
        if (facturaSeleccionado != null) {
            //configurar los atributos del modelo en textfield
            txtID.setText(String.valueOf(facturaSeleccionado.getCodigoFactura()));
            dpFecha.setValue(facturaSeleccionado.getFechaEmision());
            txtTotal.setText(String.valueOf(facturaSeleccionado.getTotal()));
            txtMetodoPago.setText(facturaSeleccionado.getMetodoPago());
            txtCliente.setText(String.valueOf(facturaSeleccionado.getCodigoCliente()));
            txtEmpleado.setText(String.valueOf(facturaSeleccionado.getCodigoEmpleado()));
            //Nuevo
            for (Cliente cliente : cbxCliente.getItems()) {
                if (cliente.getCodigoCliente() == facturaSeleccionado.getCodigoCliente()) {
                    cbxCliente.setValue(cliente);
                    break;
                }
            }
            for (Empleado empleado : cbxEmpleado.getItems()) {
                if (empleado.getCodigoEmpleado() == facturaSeleccionado.getCodigoEmpleado()) {
                    cbxEmpleado.setValue(empleado);
                    break;
                }
            }
            
        }
    }
    
    private void cargarClienteComboBox(){
        ObservableList<Cliente> listarClientes = FXCollections.observableArrayList();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = enunciado.executeQuery();
            
            
            while (resultado.next()) {
                listarClientes.add(new Cliente(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5)));
            }
        } catch (Exception e) {
            System.out.println("Error al listar clientes: "+e);
            
        }
        
        cbxCliente.setItems(listarClientes);
    }
    
    private void cargarEmpleadoComboBox(){
        ObservableList<Empleado> listarEmpleado = FXCollections.observableArrayList();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                listarEmpleado.add(new Empleado(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4)));
            }
        } catch (Exception e) {
            System.out.println("Error al listar Empleados: "+e);
        }
        cbxEmpleado.setItems(listarEmpleado);
    }
    
    public ArrayList<Factura> listarFacturas(){
        ArrayList<Factura> facturas = new ArrayList<>();
        try {
            Connection conexionv = Conexion.getInstancia().getConexion();
            String sql = "{call sp_listarFacturas()}";
            Statement enunciado = conexionv.createStatement();
            ResultSet resultado = enunciado.executeQuery(sql);
            while (resultado.next()) {
                facturas.add(new Factura(
                        resultado.getInt(1),
                        resultado.getDate(2).toLocalDate(),
                        resultado.getDouble(3),
                        resultado.getString(4),
                        resultado.getInt(5),
                        resultado.getInt(6)));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar: "+e.getMessage());
        }
        return facturas;
    }
    
    private Factura cargarModeloFacturas(){
//      evaluacion ? resultdo vedadero : resultado falso
        int codigoFactura = txtID.getText().isEmpty() ? 0 :  Integer.parseInt(txtID.getText());
        Cliente clienteSeleccionado = cbxCliente.getValue();
        Empleado empleadoSeleccionado = cbxEmpleado.getValue();
        return new Factura(codigoFactura, dpFecha.getValue(), Double.parseDouble(txtTotal.getText()),
                txtMetodoPago.getText(), clienteSeleccionado.getCodigoCliente(), empleadoSeleccionado.getCodigoEmpleado());
                //clienteSeleccionado.getCodigoCliente()
    }
    
    public void insertarNuevaFactura(){
        if (txtTotal.getText().isEmpty()||
            txtMetodoPago.getText().isEmpty()||
            dpFecha.getValue() == null||
            cbxCliente.getValue() == null||
            cbxEmpleado.getValue() == null) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtTotal.getText().matches("-?\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "¡El total debe ser un número decimal!", "¡Total inválido!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtMetodoPago.getText().matches("Efectivo|Tarjeta|Transferencia")) {
            JOptionPane.showMessageDialog(null, "¡El Metodo de pago debe ser 'Efectivo', 'Tarjeta' o 'Transferencia' ", "¡Metodo de pago invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloFactura = cargarModeloFacturas();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarFactura(?,?,?,?,?)}");
            enunciado.setDate(1, Date.valueOf(modeloFactura.getFechaEmision()));
            enunciado.setDouble(2, modeloFactura.getTotal());
            enunciado.setString(3, modeloFactura.getMetodoPago());
            enunciado.setInt(4, modeloFactura.getCodigoCliente());
            enunciado.setInt(5, modeloFactura.getCodigoEmpleado());
            enunciado.execute();
            cargarTablaFacturas();
            System.out.println("Factura Agregado Correctamente");
        } catch (Exception e) {
            System.out.println("Error al agregar nueva Factura: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void actualizarFacturas(){
        if (txtTotal.getText().isEmpty()||
            txtMetodoPago.getText().isEmpty()||
            dpFecha.getValue() == null||
            cbxCliente.getValue() == null||
            cbxEmpleado.getValue() == null) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtTotal.getText().matches("-?\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "¡El total debe ser un número decimal!", "¡Total inválido!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtMetodoPago.getText().matches("Efectivo|Tarjeta|Transferencia")) {
            JOptionPane.showMessageDialog(null, "¡El Metodo de pago debe ser 'Efectivo', 'Tarjeta' o 'Transferencia' ", "¡Metodo de pago invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }
       modeloFactura = cargarModeloFacturas();
       try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarFactura(?,?,?,?)}");
            enunciado.setInt(1, modeloFactura.getCodigoFactura());
            enunciado.setDate(2, Date.valueOf(modeloFactura.getFechaEmision()));
            enunciado.setDouble(3, modeloFactura.getTotal());
            enunciado.setString(4, modeloFactura.getMetodoPago());
            enunciado.execute();
            System.out.println("Se edito Factura Correctamente");
            cargarTablaFacturas();
           
       } catch (SQLException e) {
           System.out.println("Error al actualizar Factura"+ e.getMessage());
           e.printStackTrace();
       }
    } 
   
    public void eliminarFacturas(){
       modeloFactura = cargarModeloFacturas();
        try {
           CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarFactura(?)}");
           enunciado.setInt(1, modeloFactura.getCodigoFactura());
           enunciado.execute();
           System.out.println("Se elimino Factura Correctamente");
           cargarTablaFacturas(); 
        }catch (SQLException e) {
           System.out.println("Error al eliminar Factura"+ e.getMessage());
           e.printStackTrace();
       }
    }
   
    public void actualizarEstadoFormulario(boolean estado){
        txtID.setDisable(estado);
        dpFecha.setDisable(estado);
        txtTotal.setDisable(estado);
        txtMetodoPago.setDisable(estado);
        txtEmpleado.setDisable(estado);
        txtCliente.setDisable(estado);
    }
    
    public void habilitarDeshabilitarNodo(){
        boolean deshabilitado = txtMetodoPago.isDisable();
        actualizarEstadoFormulario(!deshabilitado);
        btnSiguiente.setDisable(deshabilitado);
        btnAnterior.setDisable(deshabilitado);
        tablaFacturas.setDisable(deshabilitado);
    }
   
   public void limpiarFormulario(){
        txtID.clear();
        dpFecha.setValue(null);
        txtTotal.clear();
        txtMetodoPago.clear();
        txtEmpleado.clear();
        txtCliente.clear();
    }
   
    public void btnNuevoActionEvent(){
        estadoActual = FacturaController.EstadoFormulario.AGREGAR;
        limpiarFormulario();
        agregarGuardar();
    }
    //
    @FXML
    public void agregarGuardar(){
        if (estadoActual == FacturaController.EstadoFormulario.AGREGAR) {
            btnNuevo.setText("Guardar");
            insertarNuevaFactura();
        }else if(estadoActual == FacturaController.EstadoFormulario.EDITAR){
            btnNuevo.setText("Guardar");
            actualizarFacturas();
        }
        estadoActual = FacturaController.EstadoFormulario.NINGUNA;          
    }
    
    @FXML
    private void editarFactura(){
        cambiarGuardarEditar();
        estadoActual = FacturaController.EstadoFormulario.EDITAR;
        habilitarDeshabilitarNodo();
    }
    
    public void cambiarGuardarEditar(){
        btnNuevo.setText("Guardar");
        btnEliminar.setText("Cancelar");
        btnEditar.setDisable(true);
    }
    
    public void cambiarOriginal(){
        btnNuevo.setText("Nuevo+");
        btnEliminar.setText("Eliminar");
        btnEditar.setDisable(false);
        estadoActual = EstadoFormulario.NINGUNA;
    }
    //
    @FXML
    private void eliminarCancelar(){
        //Elimine si no esta guardado o actualizando
        if (estadoActual == FacturaController.EstadoFormulario.NINGUNA) {
            eliminarFacturas();
        }else if (estadoActual == FacturaController.EstadoFormulario.AGREGAR || estadoActual == FacturaController.EstadoFormulario.EDITAR) {
            cambiarOriginal();
            habilitarDeshabilitarNodo();
        } 
    }
    
    @FXML
    private void buscarPorMetodoPago(){
        //nuevo ArrayList -> obtener el texto -> usar un for each del OL -> mostrar resultado
        ArrayList<Factura> resultadoBusqueda = new ArrayList<>();
        String metodoPagoBuscado = txtBuscar.getText();
        for (Factura factura : listarFacturas) {
            if (factura.getMetodoPago().toLowerCase().contains(metodoPagoBuscado.toLowerCase())) {
                resultadoBusqueda.add(factura);
            }
        }
        tablaFacturas.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (resultadoBusqueda.isEmpty()) {
           tablaFacturas.getSelectionModel().selectFirst();
       }
    }
    
    
    @FXML
    private void cambiarNuevaFactura(){
        //cambiartexto y deshabilitar->cambiarAccion->LinpiarTexto
        if (null != estadoActual) 
        switch (estadoActual) {
            case NINGUNA:
                cambiarGuardarEditar();
                estadoActual = FacturaController.EstadoFormulario.AGREGAR;
                limpiarFormulario();
                habilitarDeshabilitarNodo();
                break;
            case AGREGAR:
                System.out.println("Accion del metodo agregar");
                insertarNuevaFactura();
                cambiarOriginal();
                habilitarDeshabilitarNodo();
                break;
            case EDITAR:
                System.out.println("Accion del metodo editar");
                actualizarFacturas();
                cambiarOriginal();
                habilitarDeshabilitarNodo();
                break;
            default:
                break;
        }
    }
   
    
    
    @FXML
    private void btnSiguienteAccion(){
        //en donde estamos (index), comprobar no estar fuera de la lista, 
        //select se mueve segun el index(select +1)
        int indice = tablaFacturas.getSelectionModel().getSelectedIndex();
        if (indice < listarFacturas.size()-1) {
            tablaFacturas.getSelectionModel().select(indice+1);
            cargarFormulario();
        }
        
    }
    
    @FXML
    private void btnAnteriorAccion(){
        int indice = tablaFacturas.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaFacturas.getSelectionModel().select(indice-1);
            cargarFormulario();
        }
    }
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void btnRegresarButtonActionEvent(ActionEvent evento){
        System.out.println("Regreso a Menu Principal");
        principal.getMenuPrincipalView();
    }    
    
}
