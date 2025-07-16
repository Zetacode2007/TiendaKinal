package org.josezeta.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.josezeta.system.Main;

/**
 *
 * @author RV
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private Button btnCerrar;
    @FXML
    private Button btnCliente;
    @FXML
    private Button btnVeterinario;
    @FXML
    private Button btnProveedor;
    @FXML
    private Button btnVacuna;
    @FXML
    private Button btnEmpleado;
    @FXML
    private Button btnMascota;
    @FXML
    private Button btnConsulta;
    @FXML
    private Button btnTratamiento;
    @FXML
    private Button btnCita;
    @FXML
    private Button btnVacunacion;
    @FXML
    private Button btnMedicamento;
    @FXML
    private Button btnCompra;
    @FXML
    private Button btnReceta;
    @FXML
    private Button btnFactura;
    private Main principal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @FXML
    public void clickManejadorEventos(ActionEvent evento) {
        if (evento.getSource() == btnCerrar) {
            System.out.println("Cerrando, adios.");
            principal.getInicioView();
//        }else if (evento.getSource() == btnCliente) {
//            System.out.println("Nos vamos con el Cliente");
//            principal.getClientesView(); 
//        }else if (evento.getSource() == btnEmpleado) {
//            System.out.println("Nos vamos a Empleados");
//            principal.getEmpleadosView();
//        }else if (evento.getSource() == btnFactura) {
//            System.out.println("Nos vamos a Facturas");
//            principal.getFacturasView();
//        } 
        }
    }
}
