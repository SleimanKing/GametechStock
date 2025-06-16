package view;

import gametechstock.Producto;
import gametechstock.SistemaStock;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;

/**
 * Ventana que permite consultar el stock actual de todos los productos registrados.
 * El usuario puede ordenar la lista por nombre o por código de producto.
 */
public class VentanaStock {
    private final SistemaStock sistema;

    public VentanaStock(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana de consulta de stock.
     * @param stage ventana de JavaFX donde se cargará la interfaz
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Consulta de Stock");

        // --- Grupo de radio buttons para elegir el criterio de ordenamiento ---
        ToggleGroup ordenGroup = new ToggleGroup();

        RadioButton rbNombre = new RadioButton("Ordenar por nombre");
        RadioButton rbCodigo = new RadioButton("Ordenar por código");

        rbNombre.setToggleGroup(ordenGroup);
        rbCodigo.setToggleGroup(ordenGroup);
        rbNombre.setSelected(true); // opción predeterminada

        // --- Área de texto donde se mostrará el stock ---
        TextArea stockArea = new TextArea();
        stockArea.setEditable(false); // solo lectura
        stockArea.setStyle("-fx-font-family: monospace;"); // fuente monoespaciada para alinear columnas

        // --- Botón para mostrar los productos en el área de texto ---
        Button btnMostrar = new Button("Mostrar productos");

        // Acción del botón
        btnMostrar.setOnAction(e -> {
            stockArea.clear(); // limpia el contenido anterior
            var productos = sistema.getProductos(); // obtiene productos actuales

            // Ordena la lista según la opción seleccionada
            if (rbNombre.isSelected()) {
                productos.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
            } else {
                productos.sort(Comparator.comparing(Producto::getCodigo));
            }

            // Recorre y muestra los productos en el TextArea
            for (Producto p : productos) {
                String linea = String.format(
                        "%-30s - Stock: %3d [%s]%s\n",
                        p.getNombre(),
                        p.getStockActual(),
                        p.getCodigo(),
                        p.esCritico() ? " ⚠️ CRÍTICO" : ""
                );
                stockArea.appendText(linea);
            }
        });

        // --- Layout de la ventana (VBox) ---
        VBox layout = new VBox(10, rbNombre, rbCodigo, btnMostrar, stockArea);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: top-left;");

        // Se establece la escena y se muestra
        stage.setScene(new Scene(layout, 500, 400));
        stage.show();
    }
}
