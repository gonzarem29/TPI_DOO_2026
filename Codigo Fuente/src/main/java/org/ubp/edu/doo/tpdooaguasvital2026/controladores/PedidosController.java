package org.ubp.edu.doo.tpdooaguasvital2026.controladores;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import org.ubp.edu.doo.tpdooaguasvital2026.App;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaModelo;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Pedido;

public class PedidosController extends Controller implements Initializable {

    private Pedido modelo;
    @FXML
    private TableView<Pedido> tableView;
    private ObservableList<Pedido> datos = null;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TextField txtBuscarCliente;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRendir;
    @FXML
    private Button btnLimpiar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.modelo = (Pedido) FabricaModelo.fabricar("Pedido");
        this.txtBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtBuscar.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        this.cmbEstado.setItems(FXCollections.observableArrayList(
                "Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO", "CANCELADO"));
        this.cmbEstado.setValue("Todos");
        if (this.tableView != null) {
            this.configureTable();
            this.loadData();
        }
        this.btnModificar.disableProperty().bind(
                this.tableView.getSelectionModel().selectedItemProperty().isNull());
        this.btnEliminar.disableProperty().bind(
                this.tableView.getSelectionModel().selectedItemProperty().isNull());
        this.btnRendir.disableProperty().bind(
                this.tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    @Override
    public void loadData() {
        this.progress.setVisible(true);
        List<Pedido> pedidos = this.modelo.listarTodos();
        this.datos = FXCollections.observableList(pedidos);
        this.tableView.setItems(datos);
        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.progress.setVisible(false);
    }

    private void configureTable() {
        TableColumn<Pedido, Integer> tcNro = (TableColumn<Pedido, Integer>) this.tableView.getColumns().get(0);
        tcNro.setCellValueFactory(new PropertyValueFactory<>("nroPedido"));
        TableColumn<Pedido, String> tcFecha = (TableColumn<Pedido, String>) this.tableView.getColumns().get(1);
        tcFecha.setCellValueFactory(celda -> {
            Date fecha = celda.getValue().getFecha();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return new SimpleStringProperty(fecha != null ? sdf.format(fecha) : "");
        });
        TableColumn<Pedido, String> tcCliente = (TableColumn<Pedido, String>) this.tableView.getColumns().get(2);
        tcCliente.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().toString() : "");
        });
        TableColumn<Pedido, String> tcEstado = (TableColumn<Pedido, String>) this.tableView.getColumns().get(3);
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    @FXML
    private void obtenerPedidoSeleccionado(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            this.btnModificar.fire();
        }
    }

    @FXML
    private void buscarPedidos() {
        this.progress.setVisible(true);
        String textoNro = this.txtBuscar.getText();
        String textoCliente = this.txtBuscarCliente.getText();
        String estadoFiltro = this.cmbEstado.getValue();
        List<Pedido> pedidos;
        if (!textoNro.isEmpty()) {
            Integer nro = Integer.valueOf(textoNro);
            pedidos = this.modelo.listarPorNro(nro);
        } else {
            pedidos = this.modelo.listarTodos();
        }
        if (!textoCliente.isEmpty()) {
            String filtro = textoCliente.toLowerCase();
            pedidos = pedidos.stream()
                    .filter(p -> p.getCliente() != null
                            && p.getCliente().toString().toLowerCase().contains(filtro))
                    .collect(Collectors.toList());
        }
        if (estadoFiltro != null && !"Todos".equals(estadoFiltro)) {
            pedidos = pedidos.stream()
                    .filter(p -> estadoFiltro.equals(p.getEstado()))
                    .collect(Collectors.toList());
        }
        this.datos = FXCollections.observableList(pedidos);
        this.tableView.setItems(this.datos);
        this.progress.setVisible(false);
    }

    @FXML
    private void modificarPedido(ActionEvent event) {
        Pedido ped = this.tableView.getSelectionModel().getSelectedItem();
        if (ped != null) {
            try {
                FXMLLoader loader = App.openFXML("editarPedido", "Actualizar Pedido", Modality.APPLICATION_MODAL);
                EditarPedidoController controller = loader.getController();
                controller.passData(ped, this);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Seleccione un pedido para modificar");
        }
    }

    @FXML
    private void nuevoPedido(ActionEvent event) {
        try {
            FXMLLoader loader = App.openFXML("editarPedido", "Registrar Pedido", Modality.APPLICATION_MODAL);
            EditarPedidoController controller = loader.getController();
            controller.passData(new Pedido(-1, new Date(System.currentTimeMillis()), null, null), this);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
        }
    }

    @FXML
    private void eliminarPedido(ActionEvent event) {
        Pedido ped = this.tableView.getSelectionModel().getSelectedItem();
        if (ped == null) {
            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Seleccione un pedido para eliminar");
            return;
        }
        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
        confirma.setTitle("Info");
        confirma.setHeaderText(" Seguro desea eliminar ese registro?");
        confirma.showAndWait()
                .ifPresent((btnType) -> {
                    if (btnType == ButtonType.OK) {
                        if (ped.eliminar()) {
                            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Registro eliminado con exito");
                            this.loadData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, null, "Info", "El registro no pudo ser eliminado");
                        }
                    }
                });
    }

    @FXML
    private void rendirPedido(ActionEvent event) {
        Pedido ped = this.tableView.getSelectionModel().getSelectedItem();
        if (ped != null) {
            try {
                FXMLLoader loader = App.openFXML("rendirPedido", "Registrar Rendicion", Modality.APPLICATION_MODAL);
                RendirPedidoController controller = loader.getController();
                ped.buscarDetalles();
                controller.passData(ped, this);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
            }
        }
    }

    @FXML
    private void limpiarBusqueda(ActionEvent event) {
        this.txtBuscar.setText("");
        this.txtBuscarCliente.setText("");
        this.cmbEstado.setValue("Todos");
        this.loadData();
    }

}
