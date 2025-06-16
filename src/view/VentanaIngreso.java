package view;

import gametechstock.Ingreso;
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
 * Clase que representa la ventana de ingreso de productos al stock.
 * Permite al usuario seleccionar un producto, ingresar una cantidad y registrar el movimiento.
 */
public class VentanaIngreso {

    // Referencia al sistema de stock actual para acceder a productos, usuario y movimientos
    private final SistemaStock sistema;

    /**
     * Constructor de la ventana.
     * @param sistema sistema de stock activo
     */
    public VentanaIngreso(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Método que muestra la interfaz de ingreso en la ventana proporcionada.
     * @param stage ventana principal (escenario) de JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Registrar Ingreso de Producto");

        // --- Tabla que muestra los productos existentes ---
        TableView<Producto> tablaProductos = new TableView<>();

        // Columna con el nombre del producto
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Columna con el código del producto
        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        // Columna con el stock actual del producto
        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        // Se agregan todas las columnas a la tabla
        tablaProductos.getColumns().addAll(colNombre, colCodigo, colStock);

        // Se cargan los productos del sistema a la tabla
        tablaProductos.setItems(FXCollections.observableArrayList(sistema.getProductos()));
        tablaProductos.setPrefHeight(200);

        // --- Campo para ingresar la cantidad ---
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad a ingresar");

        // --- Mensaje de estado y botón ---
        Label mensaje = new Label();
        Button btnRegistrar = new Button("Registrar Ingreso");

        // Acción que se ejecuta al hacer clic en el botón
        btnRegistrar.setOnAction(e -> {
            Producto producto = tablaProductos.getSelectionModel().getSelectedItem(); // producto seleccionado
            String input = txtCantidad.getText(); // cantidad ingresada

            // Validación: ambos campos deben estar completos
            if (producto == null || input.isEmpty()) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Debe seleccionar un producto e ingresar una cantidad.");
                return;
            }

            try {
                int cantidad = Integer.parseInt(input); // intenta convertir a entero

                // Se crea y aplica un nuevo ingreso
                Movimiento ingreso = new Ingreso(cantidad, producto, sistema.getUsuarioActual());
                ingreso.aplicar(); // actualiza stock del producto

                // Agrega el movimiento a la lista de movimientos del sistema
                sistema.getMovimientos().add(ingreso);

                mensaje.setStyle("-fx-text-fill: green;");
                mensaje.setText("Ingreso registrado correctamente.");

                tablaProductos.refresh(); // actualiza la tabla visualmente

                // Alerta si el stock queda en estado crítico luego del ingreso
                if (producto.esCritico()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Advertencia");
                    alerta.setHeaderText("Stock crítico");
                    alerta.setContentText("El stock actual está por debajo del mínimo.");
                    alerta.showAndWait();
                }

            } catch (NumberFormatException ex) {
                // El valor ingresado no es un número válido
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("La cantidad debe ser un número válido.");
            } catch (Exception ex) {
                // Error general (ej: reglas de negocio)
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Error: " + ex.getMessage());
            }
        });

        // --- Contenedor principal de la interfaz (VBox) ---
        VBox layout = new VBox(10, tablaProductos, txtCantidad, btnRegistrar, mensaje);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Se muestra la ventana
        stage.setScene(new Scene(layout, 500, 400));
        stage.show();
    }
}

