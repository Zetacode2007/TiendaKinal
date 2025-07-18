package org.josezeta.controller;

import java.time.LocalDate;//atributo del cliete para feca
import java.net.URL;
import java.sql.CallableStatement;
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
import org.josezeta.model.Cliente;
import org.josezeta.system.Main;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.josezeta.database.Conexion;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class ClienteController implements Initializable {

    @FXML
    private Button btnRegresar, btnAnterior, btnNuevo, btnEditar, btnEliminar, btnSiguiente, btnBuscar;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn colNombre,  colTelefono,  colCorreo,  colId, colDPI;
    @FXML
    private TextField txtID, txtNombre, txtDPI, txtTelefono,  txtCorreo, txtBuscar;

    private ObservableList<Cliente> listarClientes = FXCollections.observableArrayList();
    private Cliente modeloCliente;

    private enum Acciones {
        AGREGAR, EDITAR, ELIMINAR, NINGUNA
    }
    private Acciones accion = Acciones.NINGUNA;

    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setFormatoColumnaModelo();
        cargarDatos();
        tablaClientes.setOnMouseClicked(eventHandler -> getClienteTextField());
        txtNombre.setDisable(true);
        txtTelefono.setDisable(true);
        txtCorreo.setDisable(true);
        txtDPI.setDisable(true);
    }

    public ArrayList<Cliente> listarCliente() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            Connection conexionv = Conexion.getInstancia().getConexion();
            String sql = "{call sp_ListarClientes()}";
            Statement enunciado = conexionv.createStatement();
            ResultSet resultado = enunciado.executeQuery(sql);
            while (resultado.next()) {
                clientes.add(new Cliente(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5)));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
        return clientes;
    }

    public void cargarDatos() {
        listarClientes = FXCollections.observableArrayList(listarCliente());
        tablaClientes.setItems(listarClientes);
        tablaClientes.getSelectionModel().selectFirst();
        getClienteTextField();
    }

    public void getClienteTextField() {
    if (tablaClientes == null) {
        System.out.println("tablaClientes es null");
        return;
    }
    Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
    if (clienteSeleccionado != null) {
        txtID.setText(String.valueOf(clienteSeleccionado.getCodigoCliente()));
        txtNombre.setText(clienteSeleccionado.getNombreCliente());
        txtTelefono.setText(clienteSeleccionado.getTelefono());
        txtCorreo.setText(clienteSeleccionado.getCorreo());
        txtDPI.setText(clienteSeleccionado.getDPI());
    } else {
        System.out.println("No hay cliente seleccionado");
        limpiarTexto();
    }
}

    public void setFormatoColumnaModelo() {
        //formato de celdas -> expresiones lamba;
        colId.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigoCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombreCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("correo"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Cliente, String>("DPI"));
    }

    private Cliente getModeloCliente() {
        int codigoCliente;
        if (txtID.getText().isEmpty()) {
            //verdadero
            codigoCliente = 1;
        } else {
            codigoCliente = Integer.parseInt(txtID.getText());
        }
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String dpi = txtDPI.getText();
        //cntrol+ espacio luego enter en el modelo y elegis el controlador lleno
        Cliente cliente = new Cliente(codigoCliente, nombre, telefono,  correo, dpi);
        return cliente;
    }

    public void agregarCliente() {
        modeloCliente = getModeloCliente();
        if (txtNombre.getText().isEmpty()
                || txtTelefono.getText().isEmpty()
                || txtCorreo.getText().isEmpty()
                || txtDPI.getText() == null) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtCorreo.getText().matches("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(null, "¡El correo electronico no es valido", "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtTelefono.getText().matches("\\d{8}")) {
            JOptionPane.showMessageDialog(null, "¡El numero de telefono debe tener 8 digitos", "¡Telefono invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?,?)}");
            enunciado.setString(1, modeloCliente.getNombreCliente());
            enunciado.setString(2, modeloCliente.getTelefono());
            enunciado.setString(3, modeloCliente.getCorreo());
            enunciado.setString(4, modeloCliente.getDPI());
            enunciado.execute();
            cargarDatos();
            System.out.println("Cliente Agregado Correctamente");
        } catch (Exception e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editarCliente() {
        modeloCliente = getModeloCliente();
        if (txtNombre.getText().isEmpty()
                || txtTelefono.getText().isEmpty()
                || txtCorreo.getText().isEmpty()
                || txtDPI.getText()== null) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtCorreo.getText().matches("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(null, "¡El correo electronico no es valido", "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtTelefono.getText().matches("\\d{8}")) {
            JOptionPane.showMessageDialog(null, "¡El numero de telefono debe tener 8 digitos", "¡Telefono invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCliente(?,?,?,?,?)}");
            enunciado.setInt(1, modeloCliente.getCodigoCliente());
            enunciado.setString(2, modeloCliente.getNombreCliente());
            enunciado.setString(3, modeloCliente.getTelefono());
            enunciado.setString(4, modeloCliente.getCorreo());
            enunciado.setString(5, modeloCliente.getDPI());
            enunciado.execute();
            System.out.println("Se edito Correctamente");
            cargarDatos();

        } catch (SQLException e) {
            System.out.println("Error al actualizar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarCliente() {
        modeloCliente = getModeloCliente();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
            enunciado.setInt(1, modeloCliente.getCodigoCliente());
            enunciado.execute();
            System.out.println("Se elimino Correctamente");
            cargarDatos();

        } catch (SQLException e) {
            System.out.println("Error al eliminar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limpiarTexto() {
        txtID.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtDPI.clear();
    }

    public void btnNuevoActionEvent() {
        accion = Acciones.AGREGAR;
        limpiarTexto();
        guardarNuevoCliente();
    }

    public void guardarNuevoCliente() {
        if (accion == Acciones.AGREGAR) {
            btnNuevo.setText("Guardar");
            agregarCliente();
        } else if (accion == Acciones.EDITAR) {
            btnNuevo.setText("Guardar");
            editarCliente();
        }
        accion = Acciones.NINGUNA;
    }

    @FXML
    private void cambiarNuevoCliente() {
        //cambiartexto y deshabilitar->cambiarAccion->LinpiarTexto
        if (null != accion) {
            switch (accion) {
                case NINGUNA:
                    cambiarGuardarEditar();
                    accion = Acciones.AGREGAR;
                    limpiarTexto();
                    habilitarDeshabilitarNodo();
                    break;
                case AGREGAR:
                    System.out.println("Accion del metodo agregar");
                    agregarCliente();
                    cambiarOriginal();
                    habilitarDeshabilitarNodo();
                    break;
                case EDITAR:
                    System.out.println("Accion del metodo editar");
                    editarCliente();
                    cambiarOriginal();
                    habilitarDeshabilitarNodo();
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void cambiarEdicionCliente() {
        cambiarGuardarEditar();
        accion = Acciones.EDITAR;
        habilitarDeshabilitarNodo();
    }

    @FXML
    private void cambiarCancelarEliminar() {
        //Elimine si no esta guardado o actualizando
        if (accion == Acciones.NINGUNA) {
            eliminarCliente();
        } else if (accion == Acciones.AGREGAR || accion == Acciones.EDITAR) {
            cambiarOriginal();
            habilitarDeshabilitarNodo();
        }
    }

    @FXML
    private void btnSiguienteAccion() {
        //en donde estamos (index), comprobar no estar fuera de la lista, 
        //select se mueve segun el index(select +1)
        int indice = tablaClientes.getSelectionModel().getSelectedIndex();
        if (indice < listarClientes.size() - 1) {
            tablaClientes.getSelectionModel().select(indice + 1);
            getClienteTextField();
        }

    }

    @FXML
    private void btnAnteriorAccion() {
        int indice = tablaClientes.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaClientes.getSelectionModel().select(indice - 1);
            getClienteTextField();
        }
    }

    public void cambiarGuardarEditar() {
        btnNuevo.setText("Guardar");
        btnEliminar.setText("Cancelar");
        btnEditar.setDisable(true);
    }

    public void cambiarOriginal() {
        btnNuevo.setText("Nuevo+");
        btnEliminar.setText("Eliminar");
        btnEditar.setDisable(false);
        accion = Acciones.NINGUNA;
    }

    public void cambiarEstado(boolean estado) {
        txtID.setDisable(estado);
        txtNombre.setDisable(estado);
        txtTelefono.setDisable(estado);
        txtCorreo.setDisable(estado);
        txtDPI.setDisable(estado);
    }

    public void habilitarDeshabilitarNodo() {
        boolean deshabilitado = txtNombre.isDisable();
        cambiarEstado(!deshabilitado);
        btnSiguiente.setDisable(deshabilitado);
        btnAnterior.setDisable(deshabilitado);
        tablaClientes.setDisable(deshabilitado);
    }

    @FXML
    private void btnBuscarPorNombre() {
        //nuevo ArrayList -> obtener el texto -> usar un for each del OL -> mostrar resultado
        ArrayList<Cliente> resultadoBusqueda = new ArrayList<>();
        String nombreBuscado = txtBuscar.getText();
        for (Cliente cliente : listarClientes) {
            if (cliente.getNombreCliente().toLowerCase().contains(nombreBuscado.toLowerCase())) {
                resultadoBusqueda.add(cliente);
            }
        }
        tablaClientes.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (resultadoBusqueda.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
        }
    }

    public void btnRegresarButtonActionEvent(ActionEvent evento) {
        System.out.println("Regreso a Menu Principal");
        principal.getMenuPrincipalView();
    }

}
