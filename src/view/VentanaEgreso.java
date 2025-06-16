package view;

import gametechstock.Egreso;
import gametechstock.Movimiento;
import gametechstock.Producto;
import gametechstock.SistemaStock;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase de la interfaz gráfica que permite registrar egresos de productos desde el stock.
 * Un egreso representa una salida de unidades del inventario.
 */
public class VentanaEgreso {

    // Referencia al sistema de stock para acceder a productos, movimientos y usuario
    private final SistemaStock sistema;

    /**
     * Constructor de la ventana de egreso.
     * @param sistema sistema de stock que mantiene el estado de la aplicación
     */
    public VentanaEgreso(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana de egreso de productos.
     * @param stage ventana proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Registrar Egreso de Producto");

        // --- Tabla para mostrar productos disponibles ---
        TableView<Producto> tablaProductos = new TableView<>();

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        tablaProductos.getColumns().addAll(colNombre, colCodigo, colStock);
        tablaProductos.setItems(FXCollections.observableArrayList(sistema.getProductos()));
        tablaProductos.setPrefHeight(200);

        // --- Campo de ingreso de cantidad ---
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad a egresar");

        // --- Elementos de feedback ---
        Label mensaje = new Label();
        Button btnRegistrar = new Button("Registrar Egreso");

        // Acción al presionar el botón
        btnRegistrar.setOnAction(e -> {
            Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
            String input = txtCantidad.getText();

            // Validación de campos obligatorios
            if (producto == null || input.isEmpty()) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Debe seleccionar un producto e ingresar una cantidad.");
                return;
            }

            try {
                int cantidad = Integer.parseInt(input);

                // Se crea y aplica el egreso
                Movimiento egreso = new Egreso(cantidad, producto, sistema.getUsuarioActual());
                egreso.aplicar(); // puede lanzar excepción si no hay suficiente stock

                sistema.getMovimientos().add(egreso); // se registra en el historial

                mensaje.setStyle("-fx-text-fill: green;");
                mensaje.setText("Egreso registrado correctamente.");

                tablaProductos.refresh(); // actualiza el stock en la tabla

                // Alerta si el stock queda por debajo del mínimo
                if (producto.esCritico()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Advertencia");
                    alerta.setHeaderText("Stock crítico");
                    alerta.setContentText("El stock actual está por debajo del mínimo.");
                    alerta.showAndWait();
                }

            } catch (NumberFormatException ex) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("La cantidad debe ser un número válido.");
            } catch (Exception ex) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Error: " + ex.getMessage());
            }
        });

        // --- Layout de la ventana ---
        VBox layout = new VBox(10, tablaProductos, txtCantidad, btnRegistrar, mensaje);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Establece la escena y muestra la ventana
        stage.setScene(new Scene(layout, 500, 400));
        stage.show();
    }
}

