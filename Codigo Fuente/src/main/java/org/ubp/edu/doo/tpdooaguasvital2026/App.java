package org.ubp.edu.doo.tpdooaguasvital2026;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import org.ubp.edu.doo.tpdooaguasvital2026.util.InicializadorBD;

public class App extends Application {

    private static Scene scene;

    static {
        InicializadorBD.inicializar();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("principal"), 640, 480);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Aguas Vital SA - Sistema de Gesti\u00f3n");
        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar salida");
            alert.setHeaderText("\u00bfDesea salir del sistema?");
            alert.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    Platform.exit();
                } else {
                    e.consume();
                }
            });
        });
        stage.show();
    }

    static public void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static FXMLLoader openFXML(String fxml, String title, Modality modality) throws IOException {
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        newWindow.setScene(new Scene(loader.load()));
        newWindow.setResizable(false);
        newWindow.initModality(modality);
        newWindow.show();
        return loader;
    }

    public static void main(String[] args) {
        launch();
    }
}
