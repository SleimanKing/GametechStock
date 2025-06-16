package ConexionBD;

import gametechstock.Producto;
import gametechstock.Deposito;
import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;

/**
 * DAO para manejar la persistencia de productos en la base de datos.
 */
public class ProductoDAO {

    /**
     * Genera automáticamente un nuevo código de producto único.
     * Formato: P001, P002, ...
     * @return el nuevo código generado
     */
    public static String generarCodigoProducto() {
        String sql = "SELECT MAX(CAST(SUBSTRING(codigo, 2) AS UNSIGNED)) AS max_codigo FROM productos";
        String nuevoCodigo = "P001";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Busca el código numérico más alto en la base de datos
            if (rs.next()) {
                int maxCodigo = rs.getInt("max_codigo");
                nuevoCodigo = String.format("P%03d", maxCodigo + 1); // Incrementa y da formato
            }

        } catch (Exception e) {
            e.printStackTrace(); // Muestra error si ocurre
        }

        return nuevoCodigo;
    }

    /**
     * Guarda un nuevo producto en la base de datos con código generado automáticamente.
     * @param producto el producto a guardar
     */
    public static void guardarProducto(Producto producto) {
        String nuevoCodigo = generarCodigoProducto();
        producto.setCodigo(nuevoCodigo); // Se asigna el nuevo código al producto

        String sql = "INSERT INTO productos (codigo, nombre, categoria, stock_minimo, stock_actual, id_deposito) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setInt(4, producto.getStockMinimo());
            ps.setInt(5, producto.getStockActual());

            // Si el producto no tiene depósito asignado, se asigna uno por defecto
            if (producto.getDeposito() != null) {
                ps.setInt(6, producto.getDeposito().getId());
            } else {
                Deposito d = Deposito.obtenerDepositoPorDefecto(); // Este método debe estar implementado en la clase Deposito
                ps.setInt(6, d.getId());
            }

            ps.executeUpdate(); // Ejecuta la inserción

        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            e.printStackTrace(); // Muestra errores si hay
        }
    }

    /**
     * Recupera todos los productos desde la base de datos.
     * @return una lista de productos
     */
    public static ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            // Recorre el resultado y crea objetos Producto
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                int stockMinimo = rs.getInt("stock_minimo");
                int stockActual = rs.getInt("stock_actual");
                int idDeposito = rs.getInt("id_deposito"); // Actualmente no se asigna a objeto

                Producto p = new Producto(codigo, nombre, categoria, stockMinimo, stockActual, null);
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Muestra error en consola
        }

        return lista;
    }

    /**
     * Exporta la lista de productos a un archivo CSV, preguntando al usuario la carpeta de destino.
     */
    public static void exportarProductosCSV() {
        List<Producto> productos = obtenerProductos(); // Carga todos los productos

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccionar carpeta para guardar productos.csv");
        File selectedDirectory = chooser.showDialog(null); // Abre diálogo para seleccionar carpeta

        if (selectedDirectory == null) {
            return; // Usuario canceló
        }

        File archivo = new File(selectedDirectory, "productos.csv"); // Archivo destino

        try (PrintWriter writer = new PrintWriter(archivo)) {
            // Escribe encabezado
            writer.println("Codigo,Nombre,Categoria,Stock Minimo,Stock Actual,Stock Crítico");

            // Escribe productos uno por uno
            for (Producto p : productos) {
                String critico = (p.getStockActual() < p.getStockMinimo()) ? "Sí" : "No";
                
                writer.printf("%s,%s,%s,%d,%d,%s%n",
                    p.getCodigo(),
                    p.getNombre(),
                    p.getCategoria(),
                    p.getStockMinimo(),
                    p.getStockActual(),
                    critico);
            }

            // Alerta de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación exitosa");
            alert.setHeaderText(null);
            alert.setContentText("Archivo guardado en: " + archivo.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace(); // Error de escritura
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo exportar productos");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

