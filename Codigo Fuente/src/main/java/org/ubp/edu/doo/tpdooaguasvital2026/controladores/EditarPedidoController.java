package org.ubp.edu.doo.tpdooaguasvital2026.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.synedra.validatorfx.Validator;
import org.modelmapper.ModelMapper;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.DistribuidorDao;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.EmpleadoDao;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.PrecioDao;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.DistribuidorDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.OperadorDto;
import org.ubp.edu.doo.tpdooaguasvital2026.dto.PrecioDto;
import org.ubp.edu.doo.tpdooaguasvital2026.factories.FabricaModelo;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Cliente;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.DetallePedido;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Distribuidor;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Operador;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Pedido;
import org.ubp.edu.doo.tpdooaguasvital2026.modelo.Producto;

public class EditarPedidoController extends Controller implements Initializable {

    private Cliente cliente;
    private Producto producto;
    private Pedido pedido;
    private int ultimoNroPedido;
    @FXML
    private DatePicker txtFecha;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtTotal;
    @FXML
    private DatePicker txtFechaEstimada;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnAgregarItem;
    @FXML
    private Button btnQuitarItem;
    @FXML
    private Button btnCerrarEditarPedido;
    @FXML
    private Button btnLimpiar;
    @FXML
    private CheckBox chkAcepta;
    @FXML
    private TableView<DetallePedido> tableView;
    private ObservableList<DetallePedido> datos = null;
    @FXML
    private ComboBox<Cliente> cmbCliente;
    private ObservableList<Cliente> datosCmbCliente = null;
    @FXML
    private ComboBox<Producto> cmbProducto;
    private ObservableList<Producto> datosCmbProducto = null;
    @FXML
    private ComboBox<Operador> cmbOperador;
    private ObservableList<Operador> datosCmbOperador = null;
    @FXML
    private ComboBox<Distribuidor> cmbDistribuidor;
    private ObservableList<Distribuidor> datosCmbDistribuidor = null;
    @FXML
    private Label lblZona;
    private Controller otherCtrl;
    private Validator validador;
    private boolean esNuevo = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente = (Cliente) FabricaModelo.fabricar("Cliente");
        producto = (Producto) FabricaModelo.fabricar("Producto");
        this.validador = new Validator();
        this.txtCantidad.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCantidad.setText(oldValue);
            }
        });
        this.validador.createCheck()
                .dependsOn("cantidad", this.txtCantidad.textProperty())
                .withMethod(context -> {
                    String texto = context.get("cantidad");
                    if (texto == null || texto.isEmpty()) {
                        context.error("Campo requerido");
                    }
                })
                .decorates(this.txtCantidad)
                .immediate();
        cmbCliente.setOnAction(e -> actualizarZonaYDistribuidor());
        txtFecha.setValue(LocalDate.now());
        txtFechaEstimada.setValue(LocalDate.now().plusDays(2));
        if (this.tableView != null) {
            this.configureTable();
            this.loadData();
        }
    }

    @Override
    public void loadData() {
        this.progress.setVisible(true);
        List<Producto> productos = this.producto.listarTodos();
        List<Cliente> clientes = this.cliente.listarTodos();
        this.datosCmbCliente = FXCollections.observableList(clientes);
        this.cmbCliente.setItems(this.datosCmbCliente);
        this.datosCmbProducto = FXCollections.observableList(productos);
        this.cmbProducto.setItems(this.datosCmbProducto);
        EmpleadoDao empDao = new EmpleadoDao();
        List<OperadorDto> opDtos = empDao.listarOperadores();
        List<Operador> operadores = Arrays.asList(new ModelMapper().map(opDtos, Operador[].class));
        this.datosCmbOperador = FXCollections.observableList(operadores);
        this.cmbOperador.setItems(this.datosCmbOperador);
        DistribuidorDao distDao = new DistribuidorDao();
        List<DistribuidorDto> distDtos = distDao.listarTodos();
        List<Distribuidor> distribuidores = Arrays.asList(new ModelMapper().map(distDtos, Distribuidor[].class));
        this.datosCmbDistribuidor = FXCollections.observableList(distribuidores);
        this.cmbDistribuidor.setItems(this.datosCmbDistribuidor);
        this.progress.setVisible(false);
    }

    private void configureTable() {
        TableColumn<DetallePedido, String> tcProd = (TableColumn<DetallePedido, String>) this.tableView.getColumns().get(0);
        tcProd.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getProducto().toString());
        });
        TableColumn<DetallePedido, Double> tcCant = (TableColumn<DetallePedido, Double>) this.tableView.getColumns().get(1);
        tcCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        TableColumn<DetallePedido, Double> tcPrecVta = (TableColumn<DetallePedido, Double>) this.tableView.getColumns().get(2);
        tcPrecVta.setCellValueFactory(new PropertyValueFactory<>("precio"));
        TableColumn<DetallePedido, Double> tcSubtotal = (TableColumn<DetallePedido, Double>) this.tableView.getColumns().get(3);
        tcSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    public void passData(Pedido pedido, Controller otherCtrl) {
        this.otherCtrl = otherCtrl;
        this.esNuevo = (pedido.getNroPedido() <= -1);
        if (pedido.getFecha() != null) {
            this.txtFecha.setValue(new java.sql.Date(pedido.getFecha().getTime()).toLocalDate());
        }
        this.cmbCliente.setValue(pedido.getCliente());
        if (pedido.getOperador() != null) {
            this.cmbOperador.setValue(pedido.getOperador());
        }
        this.cmbDistribuidor.setValue(pedido.getDistribuidor());
        this.pedido = pedido;
        this.pedido.buscarDetalles();
        this.datos = FXCollections.observableList(this.pedido.getDetallePedido());
        this.tableView.setItems(this.datos);
        this.btnGuardar.setDisable(this.datos.isEmpty());
        double total = this.pedido.calcularTotalDetalle();
        this.txtTotal.setText(String.valueOf(total));
    }

    private void actualizarZonaYDistribuidor() {
        Cliente clienteSel = cmbCliente.getValue();
        if (clienteSel == null) {
            lblZona.setText("-");
            return;
        }
        String zonaCodigo = clienteSel.getZona();
        String zonaNombre = clienteSel.getZonaNombre();
        if (zonaCodigo != null && !zonaCodigo.isEmpty()) {
            lblZona.setText(zonaNombre != null ? zonaNombre : zonaCodigo);
            DistribuidorDao distDao = new DistribuidorDao();
            DistribuidorDto distDto = distDao.buscarPorZona(zonaCodigo);
            if (distDto != null && datosCmbDistribuidor != null) {
                for (Distribuidor d : datosCmbDistribuidor) {
                    if (d.getId() == distDto.getId()) {
                        cmbDistribuidor.setValue(d);
                        break;
                    }
                }
            }
        } else {
            lblZona.setText("-");
        }
    }

    @FXML
    private void alCambiarProducto(ActionEvent event) {
        Producto prod = cmbProducto.getSelectionModel().getSelectedItem();
        txtCantidad.setText("");
        if (prod != null) {
            txtCantidad.setText("1");
            PrecioDao precioDao = new PrecioDao();
            PrecioDto precioDto = precioDao.buscarPrecioActual(prod.getCodProducto());
            if (precioDto != null) {
                txtPrecio.setText(String.valueOf(precioDto.getMonto()));
            }
        }
    }

    @FXML
    private void agregarItemDetalle() {
        if (validador.containsErrors() || cmbProducto.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Debe seleccionar un producto y la cantidad");
            return;
        }
        Producto prod = cmbProducto.getSelectionModel().getSelectedItem();
        if (prod != null) {
            String cantStr = txtCantidad.getText();
            int cant = Integer.parseInt(cantStr);
            if (cant <= 0) {
                showAlert(Alert.AlertType.WARNING, null, "Info", "La cantidad debe ser mayor a cero");
                return;
            }
            double prec = txtPrecio.getText().isEmpty() ? 0
                    : Double.parseDouble(txtPrecio.getText());
            if (pedido.agregarItemDetallePedido(prod, prec, cant)) {
                datos = FXCollections.observableList(pedido.getDetallePedido());
                tableView.setItems(datos);
                double total = pedido.calcularTotalDetalle();
                txtTotal.setText(String.valueOf(total));
            } else {
                showAlert(Alert.AlertType.WARNING, null, "Info", "Ese producto ya esta en el detalle");
            }
            txtCantidad.setText("");
            txtPrecio.setText("");
            cmbProducto.setValue(null);
            tableView.getSelectionModel().clearSelection();
        }
        btnGuardar.setDisable(tableView.getItems().isEmpty());
    }

    @FXML
    private void quitarItemDetalle() {
        DetallePedido det = tableView.getSelectionModel().getSelectedItem();
        if (det != null) {
            tableView.getItems().remove(det);
            double total = pedido.calcularTotalDetalle();
            txtTotal.setText(String.valueOf(total));
            tableView.getSelectionModel().clearSelection();
        }
        txtCantidad.setText("");
        txtPrecio.setText("");
        cmbProducto.setValue(null);
        btnGuardar.setDisable(tableView.getItems().isEmpty());
    }

    @FXML
    private void guardarPedido() {
        if (txtFecha.getValue() == null
                || cmbCliente.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Debe seleccionar fecha y cliente");
            return;
        }
        if (datos.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "No puede guardar un pedido sin detalle");
            return;
        }
        if (!chkAcepta.isSelected()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Debe aceptar las condiciones para confirmar el pedido");
            return;
        }
        pedido.setCliente(cmbCliente.getSelectionModel().getSelectedItem());
        pedido.setOperador(cmbOperador.getSelectionModel().getSelectedItem());
        pedido.setDistribuidor(cmbDistribuidor.getSelectionModel().getSelectedItem());
        pedido.setEstado("PENDIENTE");
        pedido.setDetallePedido(datos);
        pedido.setFecha(Date.from(txtFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        boolean resp;
        if (!esNuevo) {
            resp = pedido.modificar();
        } else {
            resp = pedido.guardar();
        }
        if (resp) {
            if (otherCtrl != null) {
                otherCtrl.loadData();
            }
            showAlert(Alert.AlertType.INFORMATION, null, "Info",
                    "Pedido N\u00ba " + pedido.getNroPedido() + " registrado con exito");
            cerrarVentana();
        } else {
            showAlert(Alert.AlertType.ERROR, null, "Info", "El pedido no pudo ser registrado");
        }
    }

    @FXML
    private void limpiarFormulario() {
        cmbProducto.setValue(null);
        txtCantidad.setText("");
        txtPrecio.setText("");
        tableView.getItems().clear();
        if (pedido != null) {
            pedido.getDetallePedido().clear();
        }
        txtTotal.setText("0.0");
        chkAcepta.setSelected(false);
        btnGuardar.setDisable(true);
    }

    @FXML
    private void obtenerDetallePedidoSeleccionado(MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            DetallePedido det = tableView.getSelectionModel().getSelectedItem();
            if (det != null) {
                cmbProducto.setValue(det.getProducto());
                txtCantidad.setText(String.valueOf(det.getCantidad()));
                txtPrecio.setText(String.valueOf(det.getPrecio()));
            }
        }
    }
}
