package org.josezeta.system;

import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.josezeta.controller.ClienteController;
import org.josezeta.controller.InicioController;
import org.josezeta.controller.MenuPrincipalController;

public class Main extends Application {

    private static String URL = "/org/josezeta/view/";
    private Stage escenarioPrincipal;
    private Scene siguienteEscena;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        getInicioView();
        stage.show();
    }

    public Initializable cambiarEscena(String fxml, double ancho, double alto) throws Exception {
        Initializable interfazDeCambio = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivoFXML = Main.class.getResourceAsStream(URL + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Main.class.getResource(URL + fxml));

        siguienteEscena = new Scene(cargadorFXML.load(archivoFXML), ancho, alto);
        escenarioPrincipal.setScene(siguienteEscena);
        escenarioPrincipal.sizeToScene();//se acopla a las dimensiones de la escena

        //Configurar interfaz Initializable
        interfazDeCambio = cargadorFXML.getController();
        return interfazDeCambio;
    }

    public void getInicioView() {
        try {
            InicioController control = (InicioController) cambiarEscena("InicioView.fxml", 600, 400);
            control.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al ir al inicio: " + ex);
            ex.printStackTrace();
        }
    }

    public void getMenuPrincipalView() {
        try {
            MenuPrincipalController control = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 631.5, 388);
            control.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al ir al Menu Principal: " + ex);
            ex.printStackTrace();
        }
    }

    public void getClientesView() {
        try {
            ClienteController control = (ClienteController) cambiarEscena("ClientesView.fxml", 1000, 705);
            control.setPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al ir a Clientes" + e);
            e.printStackTrace();
        }
    }
}
