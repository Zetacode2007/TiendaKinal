package org.josezeta.controller;

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
import org.josezeta.system.Main;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.josezeta.database.Conexion;
import org.josezeta.model.Empleado;

/**
 * FXML Controller class
 *
 * @author RV
 */
public class EmpleadoController implements Initializable {

    @FXML
    private Button btnRegresar, btnAnterior, btnNuevo, btnEditar, btnEliminar, btnSiguiente, btnBuscar;
    @FXML
    private TableView<Empleado> tablaEmpleados;
    @FXML
    private TableColumn colNombre, colCargo, colCorreo, colId;
    @FXML
    private TextField txtID, txtNombre, txtCargo, txtCorreo, txtBuscar;

    private ObservableList<Empleado> listarEmpleados = FXCollections.observableArrayList();
    private Empleado modeloEmpleado;

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
        tablaEmpleados.setOnMouseClicked(eventHandler -> getEmpleadoTextField());
        txtNombre.setDisable(true);
        txtCargo.setDisable(true);
        txtCorreo.setDisable(true);
    }

    public void cargarDatos() {
        listarEmpleados = FXCollections.observableArrayList(listarEmpleados());
        tablaEmpleados.setItems(listarEmpleados);
        tablaEmpleados.getSelectionModel().selectFirst();
        getEmpleadoTextField();
    }

    public void getEmpleadoTextField() {
        //obtener un modelo-> Empleado
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            //configurar los atributos del modelo en textfield
            txtID.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
            txtNombre.setText(empleadoSeleccionado.getNombreEmpleado());
            txtCargo.setText(empleadoSeleccionado.getPuesto());
            txtCorreo.setText(empleadoSeleccionado.getCorreo());
        }
    }

    public void setFormatoColumnaModelo() {
        //formato de celdas -> expresiones lamba;
        colId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("correoEmpleado"));
    }

    public ArrayList<Empleado> listarEmpleados() {
        ArrayList<Empleado> empleados = new ArrayList<>();
        try {
            Connection conexionv = Conexion.getInstancia().getConexion();
            String sql = "{call sp_ListarEmpleados()}";
            Statement enunciado = conexionv.createStatement();
            ResultSet resultado = enunciado.executeQuery(sql);
            while (resultado.next()) {
                empleados.add(new Empleado(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4)));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }

        return empleados;
    }

    private Empleado getModeloEmpleado() {
        int codigoEmpleado;
        if (txtID.getText().isEmpty()) {
            //verdadero
            codigoEmpleado = 1;
        } else {
            codigoEmpleado = Integer.parseInt(txtID.getText());
        }
        String nombre = txtNombre.getText();
        String cargo = txtCargo.getText();
        String correo = txtCorreo.getText();
        //cntrol+ espacio luego enter en el modelo y elegis el controlador lleno
        Empleado empleado = new Empleado(codigoEmpleado, nombre, cargo, correo);
        return empleado;
    }

    public void agregarEmpleado() {
    modeloEmpleado = getModeloEmpleado();

    if (txtNombre.getText().isEmpty()
            || txtCargo.getText().isEmpty()
            || txtCorreo.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!txtCorreo.getText().matches("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
        JOptionPane.showMessageDialog(null, "¡El correo electrónico no es válido!", "Correo inválido", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        CallableStatement enunciado = Conexion.getInstancia().getConexion().
                prepareCall("{call sp_AgregarEmpleado(?,?,?)}");
        enunciado.setString(1, modeloEmpleado.getNombreEmpleado());
        enunciado.setString(2, modeloEmpleado.getPuesto());
        enunciado.setString(3, modeloEmpleado.getCorreo());
        enunciado.execute();
        cargarDatos();
        System.out.println("Empleado agregado correctamente.");
    } catch (Exception e) {
        System.out.println("Error al agregar: " + e.getMessage());
        e.printStackTrace();
    }
}

    public void editarEmpleado(){
        modeloEmpleado = getModeloEmpleado();
        if (txtNombre.getText().isEmpty()
                || txtCargo.getText().isEmpty()
                || txtCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡ALERTA! debe rellenar todos los campos", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!txtCorreo.getText().matches("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
           JOptionPane.showMessageDialog(null, "¡El correo electrónico no es válido!", "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEmpleado(?,?,?,?,?,?)}");
            enunciado.setInt(1, modeloEmpleado.getCodigoEmpleado());
            enunciado.setString(2, modeloEmpleado.getNombreEmpleado());
            enunciado.setString(3, modeloEmpleado.getPuesto());
            enunciado.setString(4, modeloEmpleado.getCorreo());
            enunciado.execute();
            System.out.println("Se edito Empleado Correctamente");
            cargarDatos();

        } catch (SQLException e) {
            System.out.println("Error al actualizar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarEmpleado() {
        modeloEmpleado = getModeloEmpleado();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarEmpleado(?)}");
            enunciado.setInt(1, modeloEmpleado.getCodigoEmpleado());
            enunciado.execute();
            System.out.println("Se elimino Empleado Correctamente");
            cargarDatos();

        } catch (SQLException e) {
            System.out.println("Error al eliminar Empleado " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limpiarTexto() {
        txtID.clear();
        txtNombre.clear();
        txtCargo.clear();
        txtCorreo.clear();
    }

    public void btnNuevoActionEvent() {
        accion = Acciones.AGREGAR;
        limpiarTexto();
        guardarNuevoEmpleado();
    }

    public void guardarNuevoEmpleado() {
        if (accion == Acciones.AGREGAR) {
            btnNuevo.setText("Guardar");
            agregarEmpleado();
        } else if (accion == Acciones.EDITAR) {
            btnNuevo.setText("Guardar");
            editarEmpleado();
        }
        accion = Acciones.NINGUNA;
    }

    @FXML
    private void cambiarNuevoEmpleado() {
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
                    agregarEmpleado();
                    cambiarOriginal();
                    habilitarDeshabilitarNodo();
                    break;
                case EDITAR:
                    System.out.println("Accion del metodo editar");
                    editarEmpleado();
                    cambiarOriginal();
                    habilitarDeshabilitarNodo();
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void cambiarEdicionEmpleado() {
        cambiarGuardarEditar();
        accion = Acciones.EDITAR;
        habilitarDeshabilitarNodo();
    }

    @FXML
    private void cambiarCancelarEliminar() {
        //Elimine si no esta guardado o actualizando
        if (accion == Acciones.NINGUNA) {
            eliminarEmpleado();
        } else if (accion == Acciones.AGREGAR || accion == Acciones.EDITAR) {
            cambiarOriginal();
            habilitarDeshabilitarNodo();
        }
    }

    @FXML
    private void btnSiguienteAccion() {
        //en donde estamos (index), comprobar no estar fuera de la lista, 
        //select se mueve segun el index(select +1)
        int indice = tablaEmpleados.getSelectionModel().getSelectedIndex();
        if (indice < listarEmpleados.size() - 1) {
            tablaEmpleados.getSelectionModel().select(indice + 1);
            getEmpleadoTextField();
        }

    }

    @FXML
    private void btnAnteriorAccion() {
        int indice = tablaEmpleados.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaEmpleados.getSelectionModel().select(indice - 1);
            getEmpleadoTextField();
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
        txtCargo.setDisable(estado);
        txtCorreo.setDisable(estado);
    }

    public void habilitarDeshabilitarNodo() {
        boolean deshabilitado = txtNombre.isDisable();
        cambiarEstado(!deshabilitado);
        btnSiguiente.setDisable(deshabilitado);
        btnAnterior.setDisable(deshabilitado);
        tablaEmpleados.setDisable(deshabilitado);
    }

    @FXML
    private void btnBuscarPorNombre() {
        //nuevo ArrayList -> obtener el texto -> usar un for each del OL -> mostrar resultado
        ArrayList<Empleado> resultadoBusqueda = new ArrayList<>();
        String nombreBuscado = txtBuscar.getText();
        for (Empleado empleado : listarEmpleados) {
            if (empleado.getNombreEmpleado().toLowerCase().contains(nombreBuscado.toLowerCase())) {
                resultadoBusqueda.add(empleado);
            }
        }
        tablaEmpleados.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (resultadoBusqueda.isEmpty()) {
            tablaEmpleados.getSelectionModel().selectFirst();
        }
    }

    public void btnRegresarButtonActionEvent(ActionEvent evento) {
        System.out.println("Regreso a Menu Principal");
        principal.getMenuPrincipalView();
    }

}
