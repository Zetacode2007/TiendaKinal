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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.josezeta.database.Conexion;
import org.josezeta.system.Main;

import org.josezeta.model.Producto;

public class ProductoController implements Initializable {

    @FXML
    private Button btnRegresar, btnAnterior, btnNuevo, btnEditar, btnEliminar, btnSiguiente, btnBuscar;
    @FXML
    private TableView<Producto> tablaProducto;
    @FXML
    private TableColumn colNombreProducto, colDescripcion, colId, colPrecio;
    @FXML
    private TextField txtID, txtNombre, txtDescripcion, txtPrecio, txtBuscar;

    private ObservableList<Producto> listarProductos = FXCollections.observableArrayList();
    private Producto modeloProducto;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNA
    }
    private EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;

    private Main principal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        cargarTablaProductos();
        cargarFormulario();
        tablaProducto.setOnMouseClicked(eventHandler -> cargarFormulario());
        txtNombre.setDisable(true);
        txtDescripcion.setDisable(true);
        txtPrecio.setDisable(true);
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precio"));
    }

    public void cargarTablaProductos() {
        listarProductos = FXCollections.observableArrayList(listarProductos());
        tablaProducto.setItems(listarProductos);
        tablaProducto.getSelectionModel().selectFirst();
        cargarFormulario();
    }

    private void cargarFormulario() {
        Producto productoSeleccionado = tablaProducto.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtID.setText(String.valueOf(productoSeleccionado.getCodigoProducto()));
            txtNombre.setText(productoSeleccionado.getNombreProducto());
            txtDescripcion.setText(productoSeleccionado.getDescripcion());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
        }
    }
    
    public ArrayList<Producto> listarProductos(){
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            Connection conexionv = Conexion.getInstancia().getConexion();
            String sql = "{call sp_ListarProductos()}";
            Statement enunciado = conexionv.createStatement();
            ResultSet resultado = enunciado.executeQuery(sql);
            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getDouble(4)));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar: "+e.getMessage());
        }
        
        return productos;
    }
    
    private Producto cargarModeloProducto(){
//      evaluacion ? resultdo vedadero : resultado falso
        int codigoProducto = txtID.getText().isEmpty() ? 0 :  Integer.parseInt(txtID.getText());
        return new Producto(codigoProducto, txtNombre.getText(), txtDescripcion.getText(),Double.parseDouble(txtPrecio.getText()));
    }
    
    public void insertarNuevoMedicamento(){
        if (txtNombre.getText().isEmpty()||
            txtDescripcion.getText().isEmpty()||
            txtPrecio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        if (!txtPrecio.getText().matches("-?\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "¡El precio debe ser un número decimal!", "¡Precio inválido!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloProducto = cargarModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicamento(?,?,?,?,?,?)}");
            enunciado.setString(1, modeloProducto.getNombreProducto());
            enunciado.setString(2, modeloProducto.getDescripcion());
            enunciado.setDouble(4, modeloProducto.getPrecio());
            enunciado.execute();
            cargarTablaProductos();
            System.out.println("Proveedor Agregada Correctamente");
        } catch (Exception e) {
            System.out.println("Error al agregar nuevo Proveedor: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void actualizarProducto(){
        if (txtNombre.getText().isEmpty()||
            txtDescripcion.getText().isEmpty()||
            txtPrecio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!txtPrecio.getText().matches("-?\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "¡El precio debe ser un número decimal!", "¡Precio inválido!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        modeloProducto = cargarModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarMedicamento(?,?,?,?,?,?)}");
            enunciado.setInt(1, modeloProducto.getCodigoProducto());
            enunciado.setString(2, modeloProducto.getNombreProducto());
            enunciado.setString(3, modeloProducto.getDescripcion());
            enunciado.setDouble(5, modeloProducto.getPrecio());
            enunciado.execute();
            System.out.println("Se edito Correctamente Proveedor");
            cargarTablaProductos();
       } catch (SQLException e) {
           System.out.println("Error al actualizar Proveedor "+ e.getMessage());
           e.printStackTrace();
       }
    }
    
    public void eliminarMedicamento(){
        modeloProducto = cargarModeloProducto();
        try {
           CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarProducto(?)}");
           enunciado.setInt(1, modeloProducto.getCodigoProducto());
           enunciado.execute();
           System.out.println("Se elimino Correctamente Proveedor");
           cargarTablaProductos(); 
        }catch (SQLException e) {
           System.out.println("Error al eliminar Proveedor"+ e.getMessage());
           e.printStackTrace();
       }
    }
    
    public void actualizarEstadoFormulario(boolean estado){
        txtID.setDisable(estado);
        txtNombre.setDisable(estado);
        txtDescripcion.setDisable(estado);
        txtPrecio.setDisable(estado);
    }
    
    public void habilitarDeshabilitarNodo(){
        boolean deshabilitado = txtNombre.isDisable();
        actualizarEstadoFormulario(!deshabilitado);
        btnSiguiente.setDisable(deshabilitado);
        btnAnterior.setDisable(deshabilitado);
        tablaProducto.setDisable(deshabilitado);
    }
   
   public void limpiarFormulario(){
        txtID.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear(); 
    }
   
    public void btnNuevoActionEvent(){
        estadoActual = ProductoController.EstadoFormulario.AGREGAR;
        limpiarFormulario();
        agregarGuardar();
    }
    
    @FXML
    public void agregarGuardar(){
        if (estadoActual == ProductoController.EstadoFormulario.AGREGAR) {
            btnNuevo.setText("Guardar");
            insertarNuevoMedicamento();
        }else if(estadoActual == ProductoController.EstadoFormulario.EDITAR){
            btnNuevo.setText("Guardar");
            actualizarProducto();
        }
        estadoActual = ProductoController.EstadoFormulario.NINGUNA;          
    }
    
    @FXML
    private void editarMedicamento(){
        cambiarGuardarEditar();
        estadoActual = ProductoController.EstadoFormulario.EDITAR;
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
    
    @FXML
    private void eliminarCancelar(){
        //Elimine si no esta guardado o actualizando
        if (estadoActual == ProductoController.EstadoFormulario.NINGUNA) {
            eliminarMedicamento();
        }else if (estadoActual == ProductoController.EstadoFormulario.AGREGAR || estadoActual == ProductoController.EstadoFormulario.EDITAR) {
            cambiarOriginal();
            habilitarDeshabilitarNodo();
        } 
    }
    
    @FXML
    private void buscarPorNombre(){
        //nuevo ArrayList -> obtener el texto -> usar un for each del OL -> mostrar resultado
        ArrayList<Producto> resultadoBusqueda = new ArrayList<>();
        String nombreBuscado = txtBuscar.getText();
        for (Producto medicamento : listarProductos()) {
            if (medicamento.getNombreProducto().toLowerCase().contains(nombreBuscado.toLowerCase())) {
                resultadoBusqueda.add(medicamento);
            }
        }
        tablaProducto.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (resultadoBusqueda.isEmpty()) {
           tablaProducto.getSelectionModel().selectFirst();
       }
    }
    
    
    @FXML
    private void cambiarNuevoMedicamento(){
        //cambiartexto y deshabilitar->cambiarAccion->LinpiarTexto
        if (null != estadoActual) 
        switch (estadoActual) {
            case NINGUNA:
                cambiarGuardarEditar();
                estadoActual = ProductoController.EstadoFormulario.AGREGAR;
                limpiarFormulario();
                habilitarDeshabilitarNodo();
                break;
            case AGREGAR:
                System.out.println("Accion del metodo agregar");
                insertarNuevoMedicamento();
                cambiarOriginal();
                habilitarDeshabilitarNodo();
                break;
            case EDITAR:
                System.out.println("Accion del metodo editar");
                actualizarProducto();
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
        int indice = tablaProducto.getSelectionModel().getSelectedIndex();
        if (indice < listarProductos().size()-1) {
            tablaProducto.getSelectionModel().select(indice+1);
            cargarFormulario();
        }
    }
    
    @FXML
    private void btnAnteriorAccion(){
        int indice = tablaProducto.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaProducto.getSelectionModel().select(indice-1);
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



