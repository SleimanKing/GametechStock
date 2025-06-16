package view;

import gametechstock.Ajuste;
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
 * Ventana de la interfaz gráfica que permite registrar ajustes de stock.
 * Un ajuste puede sumar o restar unidades a un producto, e incluye una justificación.
 */
public class VentanaAjuste {

    // Referencia al sistema central de stock
    private final SistemaStock sistema;

    /**
     * Constructor que recibe el sistema de stock activo.
     * @param sistema instancia de SistemaStock para acceder a productos, usuario y movimientos
     */
    public VentanaAjuste(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana de ajuste de stock.
     * @param stage la ventana principal (Stage) proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Ajuste de Stock");

        // --- Tabla que muestra los productos disponibles ---
        TableView<Producto> tablaProductos = new TableView<>();

        // Columna para el nombre del producto
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Columna para el código del producto
        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        // Columna para el stock actual del producto
        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        // Agrega columnas a la tabla
        tablaProductos.getColumns().addAll(colNombre, colCodigo, colStock);

        // Carga la lista de productos desde el sistema
        tablaProductos.setItems(FXCollections.observableArrayList(sistema.getProductos()));
        tablaProductos.setPrefHeight(200); // altura visual de la tabla

        // --- Campos de entrada de datos ---

        // Campo para ingresar la cantidad del ajuste (puede ser positivo o negativo)
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad (+ o -)"); // texto de ayuda

        // Campo para ingresar la justificación del ajuste
        TextField txtJustificacion = new TextField();
        txtJustificacion.setPromptText("Justificación");

        // Etiqueta para mostrar mensajes de error o éxito
        Label mensaje = new Label();

        // Botón para aplicar el ajuste
        Button btnAplicar = new Button("Aplicar ajuste");

        // --- Acción al hacer clic en "Aplicar ajuste" ---
        btnAplicar.setOnAction(e -> {
            // Obtiene el producto seleccionado en la tabla
            Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
            String inputCantidad = txtCantidad.getText();
            String justificacion = txtJustificacion.getText();

            // Validación: todos los campos deben estar completos
            if (producto == null || inputCantidad.isEmpty() || justificacion.isEmpty()) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Debe completar todos los campos.");
                return;
            }

            try {
                // Intenta convertir el texto de cantidad a un número entero
                int cantidad = Integer.parseInt(inputCantidad);

                // Crea un nuevo movimiento de tipo Ajuste con los datos ingresados
                Movimiento ajuste = new Ajuste(cantidad, producto, sistema.getUsuarioActual(), justificacion);

                // Aplica el ajuste (modifica el stock del producto)
                ajuste.aplicar();

                // Agrega el ajuste al historial de movimientos del sistema
                sistema.getMovimientos().add(ajuste);

                // Muestra mensaje de éxito en verde
                mensaje.setStyle("-fx-text-fill: green;");
                mensaje.setText("Ajuste aplicado correctamente.");

                // Actualiza la tabla para reflejar el nuevo stock
                tablaProductos.refresh();

                // Muestra una alerta si el stock del producto queda en estado crítico
                if (producto.esCritico()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Advertencia");
                    alerta.setHeaderText("Stock crítico");
                    alerta.setContentText("El stock actual está por debajo del mínimo.");
                    alerta.showAndWait(); // espera a que el usuario cierre la alerta
                }

            } catch (NumberFormatException ex) {
                // Error: la cantidad no es un número válido
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("La cantidad debe ser un número válido.");
            } catch (Exception ex) {
                // Cualquier otro error inesperado
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Error: " + ex.getMessage());
            }
        });

        // --- Organiza todos los componentes en un contenedor vertical (VBox) ---
        VBox layout = new VBox(10, tablaProductos, txtCantidad, txtJustificacion, btnAplicar, mensaje);
        layout.setPadding(new Insets(20)); // espacio interno del VBox
        layout.setStyle("-fx-alignment: center;"); // centra los elementos horizontalmente

        // Crea la escena con el layout y la muestra en el stage
        stage.setScene(new Scene(layout, 500, 400));
        stage.show();
    }
}


