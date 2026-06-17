package org.ubp.edu.doo.tpdooaguasvital2026.controladores;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.modelmapper.ModelMapper;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.EmpleadoDao;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.OperadorDto;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaModelo;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.DetallePedido;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Operador;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Pedido;

public class RendirPedidoController extends Controller implements Initializable {

    private Pedido pedido;
    private Controller otherCtrl;

    @FXML
    private TextField txtBuscarPedido;
    @FXML
    private TextField txtNro;
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtFactura;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtEstado;
    @FXML
    private TableView<DetallePedido> tableView;
    @FXML
    private CheckBox chkEntregado;
    @FXML
    private DatePicker txtFechaEntrega;
    @FXML
    private TextField txtHoraEntrega;
    @FXML
    private ComboBox<String> cmbFormaPago;
    @FXML
    private TextField txtMontoPercibido;
    @FXML
    private TextField txtMotivoNoEntrega;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private ComboBox<Operador> cmbAdministrativo;
    @FXML
    private Button btnRendir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbFormaPago.setItems(FXCollections.observableArrayList(
                "Contado", "Electronico"));
        cmbFormaPago.setValue("Contado");
        EmpleadoDao empDao = new EmpleadoDao();
        List<OperadorDto> opDtos = empDao.listarOperadores();
        List<Operador> admins = Arrays.asList(new ModelMapper().map(opDtos, Operador[].class));
        cmbAdministrativo.setItems(FXCollections.observableList(admins));
        txtHoraEntrega.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        configureTable();
    }

    private void configureTable() {
        TableColumn<DetallePedido, String> tcProd = (TableColumn<DetallePedido, String>) tableView.getColumns().get(0);
        tcProd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProducto().toString()));
        TableColumn<DetallePedido, Double> tcCant = (TableColumn<DetallePedido, Double>) tableView.getColumns().get(1);
        tcCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        TableColumn<DetallePedido, Double> tcPrec = (TableColumn<DetallePedido, Double>) tableView.getColumns().get(2);
        tcPrec.setCellValueFactory(new PropertyValueFactory<>("precio"));
        TableColumn<DetallePedido, Double> tcSub = (TableColumn<DetallePedido, Double>) tableView.getColumns().get(3);
        tcSub.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    public void passData(Pedido pedido, Controller otherCtrl) {
        this.otherCtrl = otherCtrl;
        this.pedido = pedido;
        mostrarPedido();
    }

    @FXML
    private void buscarPedido() {
        String nroStr = txtBuscarPedido.getText();
        if (nroStr == null || nroStr.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Ingrese un numero de pedido");
            return;
        }
        int nro = Integer.parseInt(nroStr);
        Pedido p = (Pedido) FabricaModelo.fabricar("Pedido");
        List<Pedido> resultado = p.listarPorNro(nro);
        if (resultado.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Pedido no encontrado");
            return;
        }
        this.pedido = resultado.get(0);
        this.pedido.buscarDetalles();
        mostrarPedido();
    }

    private void mostrarPedido() {
        txtNro.setText(String.valueOf(pedido.getNroPedido()));
        txtCliente.setText(pedido.getCliente() != null
                ? pedido.getCliente().toString() : "");
        txtEstado.setText(pedido.getEstado());
        double total = pedido.calcularTotalDetalle();
        txtTotal.setText(String.valueOf(total));
        txtFactura.setText(pedido.getFactura() != null
                ? String.valueOf(pedido.getFactura().getNroFactura()) : "-");
        ObservableList<DetallePedido> detList = FXCollections.observableList(
                pedido.getDetallePedido());
        tableView.setItems(detList);
        txtFechaEntrega.setValue(new java.sql.Date(
                System.currentTimeMillis()).toLocalDate());
        txtHoraEntrega.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        txtMontoPercibido.setText(String.valueOf(total));
        txtMotivoNoEntrega.setText("");
        txtObservaciones.setText("");
        chkEntregado.setSelected(true);
    }

    @FXML
    private void toggleEntregado() {
        txtMotivoNoEntrega.setDisable(chkEntregado.isSelected());
        if (!chkEntregado.isSelected()) {
            txtMotivoNoEntrega.setText("");
        }
    }

    @FXML
    private void registrarRendicion() {
        if (pedido == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Busque un pedido primero");
            return;
        }
        if ("ENTREGADO".equals(pedido.getEstado()) || "CANCELADO".equals(pedido.getEstado())) {
            showAlert(Alert.AlertType.WARNING, null, "Info",
                    "No se puede rendir un pedido en estado " + pedido.getEstado());
            return;
        }
        if (txtFechaEntrega.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Ingrese la fecha de entrega");
            return;
        }
        String hora = txtHoraEntrega.getText();
        if (hora == null || hora.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Ingrese la hora de entrega (HH:mm)");
            return;
        }
        if (chkEntregado.isSelected()) {
            pedido.setEstado("ENTREGADO");
        } else {
            String motivo = txtMotivoNoEntrega.getText();
            if (motivo == null || motivo.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, null, "Info", "Ingrese el motivo de no entrega");
                return;
            }
        }
        pedido.setFechaEntrega(Date.from(
                txtFechaEntrega.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        boolean resp = pedido.modificar();
        if (resp) {
            if (this.otherCtrl != null) {
                this.otherCtrl.loadData();
            }
            showAlert(Alert.AlertType.INFORMATION, null, "Info",
                    "Rendicion registrada - Pedido " + pedido.getNroPedido()
                    + " en estado " + pedido.getEstado());
            cerrarVentana();
        } else {
            showAlert(Alert.AlertType.ERROR, null, "Info",
                    "La rendicion no pudo ser registrada");
        }
    }
}
