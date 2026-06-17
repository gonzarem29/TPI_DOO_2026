package org.ubp.edu.doo.tpdooaguasvital2026.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import org.ubp.edu.doo.tpdooaguasvital2026.App;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Pedido;

public class PrincipalController extends Controller implements Initializable {

    private Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.alert = new Alert(Alert.AlertType.CONFIRMATION);
        this.alert.setTitle("Info");
        this.alert.setHeaderText(" Confirmacion");
        this.alert.setContentText(" Desea realizar esa accion");
    }

    @FXML
    private void salir() {
        this.alert.showAndWait()
                .ifPresent((btnType) -> {
                    if (btnType == ButtonType.OK) {
                        Platform.exit();
                    }
                });
    }

    @FXML
    private void opcRegistrarPedido() {
        try {
            FXMLLoader loader = App.openFXML("editarPedido", "Registrar Pedido", Modality.APPLICATION_MODAL);
            EditarPedidoController controller = loader.getController();
            controller.passData(new Pedido(-1, new Date(System.currentTimeMillis()), null, null), null);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
        }
    }

    @FXML
    private void opcRegistrarRendicion() {
        try {
            App.openFXML("rendirPedido", "Registrar Rendicion", Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
        }
    }

    @FXML
    private void opcConsultarPedidos() {
        try {
            App.openFXML("pedidos", "Consulta de pedidos", Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
        }
    }

    @FXML
    private void opcAcercaDe() {
        showAlert(Alert.AlertType.INFORMATION, null, "Info", "Aguas Vital SA - TP DOO 2026 - UBP");
    }

}
