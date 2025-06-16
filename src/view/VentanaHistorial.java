package view;

import gametechstock.Ajuste;
import gametechstock.Movimiento;
import gametechstock.SistemaStock;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter;

/**
 * Ventana de la interfaz gráfica que muestra el historial de movimientos (ingresos, egresos y ajustes).
 * Los datos se presentan en una tabla ordenada y legible.
 */
public class VentanaHistorial {

    // Referencia al sistema de stock, desde donde se obtienen los movimientos registrados
    private final SistemaStock sistema;

    /**
     * Constructor que recibe el sistema activo.
     * @param sistema instancia de SistemaStock para acceder a movimientos y productos
     */
    public VentanaHistorial(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana del historial en pantalla.
     * @param stage ventana proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Historial de Movimientos");

        // --- Tabla principal que contendrá todos los movimientos registrados ---
        TableView<Movimiento> tabla = new TableView<>();
        tabla.setPrefHeight(400); // Altura preferida de la tabla

        // Formateador para mostrar fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // --- Columnas de la tabla ---
        
        // Tipo de movimiento (Ingreso, Egreso, Ajuste)
        TableColumn<Movimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));

        // Fecha del movimiento (formateada)
        TableColumn<Movimiento, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFecha().format(formatter)));

        // Nombre del producto involucrado
        TableColumn<Movimiento, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));

        // Código del producto
        TableColumn<Movimiento, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProducto().getCodigo()));

        // Cantidad de unidades ingresadas, egresadas o ajustadas
        TableColumn<Movimiento, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Usuario que realizó el movimiento
        TableColumn<Movimiento, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));

        // Justificación (solo para ajustes)
        TableColumn<Movimiento, String> colJustificacion = new TableColumn<>("Justificación");
        colJustificacion.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Ajuste ajuste) {
                return new SimpleStringProperty(ajuste.getJustificacion());
            } else {
                return new SimpleStringProperty(""); // vacío para ingresos y egresos
            }
        });

        // Se agregan todas las columnas a la tabla
        tabla.getColumns().addAll(
            colTipo,
            colFecha,
            colProducto,
            colCodigo,
            colCantidad,
            colUsuario,
            colJustificacion
        );

        // Se cargan los movimientos actuales en la tabla
        tabla.setItems(FXCollections.observableArrayList(sistema.getMovimientos()));

        // --- Contenedor principal (VBox) ---
        VBox layout = new VBox(10, tabla); // espacio vertical de 10px entre componentes
        layout.setPadding(new Insets(20)); // margen interno

        // Se configura la escena y se muestra la ventana
        stage.setScene(new Scene(layout, 900, 500));
        stage.show();
    }
}

