package org.ubp.edu.doo.tpdooaguasvital2026.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class Controller {

    @FXML
    protected ProgressBar progress;

    @FXML
    public void cerrarVentana(ActionEvent e) {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    protected void cerrarVentana() {
        if (progress != null && progress.getScene() != null) {
            Stage stage = (Stage) progress.getScene().getWindow();
            stage.close();
        }
    }

    public void loadData() {
    }

    protected void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

}
