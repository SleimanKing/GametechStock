package view;

import ConexionBD.ProductoDAO;
import ConexionBD.MovimientoDAO;
import ConexionBD.UsuarioDAO;
import gametechstock.SistemaStock;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Ventana del menú principal del sistema GametechStock.
 * Permite al usuario acceder a las distintas operaciones del sistema.
 */
public class VentanaMenu {

    // Referencia al sistema de stock (modelo central)
    private final SistemaStock sistema;

    /**
     * Constructor que recibe el sistema actual.
     * @param sistema instancia del sistema de stock
     */
    public VentanaMenu(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana del menú principal con las distintas opciones.
     * @param stage ventana proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Menú Principal - GametechStock");

        // --- Botones del menú ---
        Button btnIngreso = new Button("Registrar Ingreso");
        Button btnEgreso = new Button("Registrar Egreso");
        Button btnStock = new Button("Consultar Stock");
        Button btnAjuste = new Button("Ajustar Stock");
        Button btnHistorial = new Button("Ver historial de movimientos");
        Button btnNuevo = new Button("Agregar Producto");
        Button btnListProd = new Button("Generar lista de productos");
        Button btnListMov = new Button("Generar lista de movimientos");
        Button btnSalir = new Button("Cerrar Sesión");

        // --- Acciones de los botones ---
        // Abre ventana para registrar ingreso de productos
        btnIngreso.setOnAction(e -> new VentanaIngreso(sistema).mostrar(new Stage()));

        // Abre ventana para registrar egreso de productos
        btnEgreso.setOnAction(e -> new VentanaEgreso(sistema).mostrar(new Stage()));

        // Abre ventana para consultar el stock de productos
        btnStock.setOnAction(e -> new VentanaStock(sistema).mostrar(new Stage()));

        // Abre ventana para ajustar el stock manualmente
        btnAjuste.setOnAction(e -> new VentanaAjuste(sistema).mostrar(new Stage()));

        // Abre ventana para ver el historial de movimientos (ingresos, egresos, ajustes)
        btnHistorial.setOnAction(e -> new VentanaHistorial(sistema).mostrar(new Stage()));

        // Abre ventana para crear archivo csv de lista de productos
        btnListProd.setOnAction(e -> ProductoDAO.exportarProductosCSV());
        
        // Abre ventana para crear archivo csv de lista de movimientos
        btnListMov.setOnAction(e -> MovimientoDAO.exportarMovimientosCSV(ProductoDAO.obtenerProductos(),UsuarioDAO.obtenerUsuarios()));
        
        // Abre ventana para agregar un nuevo producto al sistema
        btnNuevo.setOnAction(e -> new VentanaNuevoProducto(sistema).mostrar(new Stage()));

        // Cierra la ventana actual (equivale a cerrar sesión)
        btnSalir.setOnAction(e -> stage.close());

        // --- Layout de la ventana ---
        VBox layout = new VBox(12); // separación entre botones
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Agrega todos los botones al layout
        layout.getChildren().addAll(
            btnIngreso, btnEgreso, btnStock, btnAjuste, btnHistorial, btnNuevo, btnListProd, btnListMov, btnSalir
        );

        // Configura y muestra la escena
        stage.setScene(new Scene(layout, 300, 350));
        stage.show();
    }
}
