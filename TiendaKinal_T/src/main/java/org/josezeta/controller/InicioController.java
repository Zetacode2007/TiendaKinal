
package org.josezeta.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.josezeta.system.Main;

/**
 * FXML Controller class
 *
 * @author RV
 */
public class InicioController implements Initializable {
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    @FXML private Button btnMenuPrincipal;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML private void clickManejadorEventos(ActionEvent evento){
        if (evento.getSource() == btnMenuPrincipal) {
            System.out.println("Nos vamos al Menu Principal");
            principal.getMenuPrincipalView();
        }
    
    }
            
}
