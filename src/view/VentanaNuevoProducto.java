package view;


import ConexionBD.MovimientoDAO;
import ConexionBD.ProductoDAO;
import gametechstock.Ajuste;
import gametechstock.Deposito;
import gametechstock.Movimiento;
import gametechstock.Producto;
import gametechstock.SistemaStock;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase que representa la ventana para agregar un nuevo producto al sistema.
 * Permite al usuario ingresar el nombre, categoría, stock mínimo y stock actual.
 */
public class VentanaNuevoProducto {

    // Referencia al sistema para acceder y agregar productos
    private final SistemaStock sistema;

    /**
     * Constructor que recibe el sistema de stock.
     * @param sistema instancia activa del sistema de stock
     */
    public VentanaNuevoProducto(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana de creación de producto.
     * @param stage ventana principal proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Agregar Nuevo Producto");

        // --- Campos de entrada para el nuevo producto ---
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");

        TextField txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría");

        TextField txtMinimo = new TextField();
        txtMinimo.setPromptText("Stock mínimo");

        TextField txtActual = new TextField();
        txtActual.setPromptText("Stock actual");

        // --- Etiqueta para mostrar mensajes (éxito o error) ---
        Label mensaje = new Label();

        // --- Botón para crear el producto ---
        Button btnCrear = new Button("Crear producto");

        // Acción al hacer clic en el botón
        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String minimo = txtMinimo.getText().trim();
            String actual = txtActual.getText().trim();

            // Validación: todos los campos deben estar completos
            if (nombre.isEmpty() || categoria.isEmpty() || minimo.isEmpty() || actual.isEmpty()) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Todos los campos son obligatorios.");
                return;
            }

            try {
                // Conversión de campos numéricos
                int stockMinimo = Integer.parseInt(minimo);
                int stockActual = Integer.parseInt(actual);

                // Se asigna un depósito por defecto
                Deposito deposito = Deposito.obtenerDepositoPorDefecto();

                // Se crea el producto SIN código (lo asignará el DAO)
                Producto nuevo = new Producto(null, nombre, categoria, stockMinimo, 0, deposito);
                //Se toma en valor 0 el stock actual, para que al ingresar la mercadería nueva sea
                //tomada como un nuevo movimiento, y así se refleje también en el  listado del historial
                //sin duplicar los ingresos nuevo al usar dos metodos que sumarían
                
                // Se guarda en la BD (y genera el código internamente)
                ProductoDAO.guardarProducto(nuevo);
                
                Movimiento ajuste = new Ajuste(
                    stockActual,                     // cantidad cargada
                    nuevo,                           // producto nuevo
                    sistema.getUsuarioActual(),      // usuario responsable
                    "Ingreso nuevo"               // justificación
                );

                // Guardar el movimiento en la base de datos
                MovimientoDAO.guardarMovimiento(ajuste);

                // Agregar el movimiento a la lista en memoria
                sistema.getMovimientos().add(ajuste);
                
                // Esta línea es clave para que el producto en memoria tenga el stock correcto
                nuevo.setStockActual(stockActual);

                // Agregar a la lista en memoria si todo fue bien
                sistema.getProductos().add(nuevo);

                mensaje.setStyle("-fx-text-fill: green;");
                mensaje.setText("Producto creado con código: " + nuevo.getCodigo());

            } catch (NumberFormatException ex) {
                mensaje.setStyle("-fx-text-fill: red;");
                mensaje.setText("Stock mínimo y actual deben ser números válidos.");
            }
        });


        // --- Layout principal con separación y márgenes ---
        VBox layout = new VBox(10, txtNombre, txtCategoria, txtMinimo, txtActual, btnCrear, mensaje);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Se crea y muestra la ventana
        stage.setScene(new Scene(layout, 400, 300));
        stage.show();
    }
}
