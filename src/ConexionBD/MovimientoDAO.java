package ConexionBD;

import gametechstock.Ajuste;
import gametechstock.Egreso;
import gametechstock.Ingreso;
import gametechstock.Movimiento;
import gametechstock.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import gametechstock.Usuario;
import java.sql.*;
import java.util.*;
import java.io.PrintWriter;
import java.io.File;
import java.util.Queue;
import java.util.List;

import java.time.LocalDateTime;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;

/**
 * Clase DAO (Data Access Object) para manejar el acceso a la base de datos relacionado con movimientos.
 * Permite guardar, recuperar y exportar movimientos (ingresos, egresos, ajustes).
 */
public class MovimientoDAO {

    /**
     * Guarda un movimiento (Ingreso, Egreso o Ajuste) en la base de datos.
     * @param m el movimiento a guardar
     */
    public static void guardarMovimiento(Movimiento m) {
        // Consulta SQL para insertar un nuevo movimiento
        String sql = "INSERT INTO movimientos (tipo, fecha, cantidad, justificacion, producto_codigo, usuario_id) VALUES (?, NOW(), ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion(); // Abre conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta

            // Guarda el tipo de movimiento como nombre de clase en mayúsculas (INGRESO, EGRESO, AJUSTE)
            stmt.setString(1, m.getClass().getSimpleName().toUpperCase());

            int cantidad = m.getCantidad();

            // Si es un Egreso, se guarda como cantidad negativa
            if (m instanceof Egreso) {
                cantidad = -cantidad;
            }

            stmt.setInt(2, cantidad); // Cantidad ajustada
            stmt.setString(3, m.getJustificacion()); // Justificación (vacía para ingresos/egresos)
            stmt.setString(4, m.getProducto().getCodigo()); // Código del producto
            stmt.setInt(5, m.getUsuario().getId()); // ID del usuario que realizó el movimiento

            stmt.executeUpdate(); // Ejecuta la inserción en la base de datos

        } catch (Exception e) {
            e.printStackTrace(); // Muestra cualquier error ocurrido
        }
    }

    /**
     * Recupera todos los movimientos desde la base de datos, asociándolos con sus productos y usuarios.
     * @param productos lista de productos disponibles
     * @param usuarios lista de usuarios registrados
     * @return una cola con todos los movimientos recuperados
     */
    public static Queue<Movimiento> obtenerTodos(List<Producto> productos, List<Usuario> usuarios) {
        Queue<Movimiento> lista = new LinkedList<>();

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movimientos")) {

            while (rs.next()) {
                // Extrae datos de cada fila del ResultSet
                String tipo = rs.getString("tipo");
                LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
                int cantidad = rs.getInt("cantidad");
                String justificacion = rs.getString("justificacion");
                String productoCodigo = rs.getString("producto_codigo");
                int usuarioId = rs.getInt("usuario_id");

                // Busca el producto y usuario correspondiente
                Producto producto = buscarProductoPorCodigo(productos, productoCodigo);
                Usuario usuario = buscarUsuarioPorId(usuarios, usuarioId);

                // Crea el objeto de movimiento adecuado según el tipo
                Movimiento movimiento = switch (tipo) {
                    case "INGRESO" -> new Ingreso(cantidad, producto, usuario);
                    case "EGRESO" -> new Egreso(cantidad, producto, usuario);
                    case "AJUSTE" -> new Ajuste(cantidad, producto, usuario, justificacion);
                    default -> null;
                };

                // Si se creó correctamente, se ajusta la fecha y se agrega a la lista
                if (movimiento != null) {
                    movimiento.setFecha(fecha);
                    lista.add(movimiento);
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores si los hay
        }

        return lista;
    }

    /**
     * Busca un producto en la lista por su código.
     * @param lista lista de productos
     * @param codigo código a buscar
     * @return el producto correspondiente, o null si no se encuentra
     */
    private static Producto buscarProductoPorCodigo(List<Producto> lista, String codigo) {
        for (Producto p : lista) {
            if (p.getCodigo().equals(codigo)) return p;
        }
        return null;
    }

    /**
     * Busca un usuario en la lista por su ID.
     * @param lista lista de usuarios
     * @param id ID a buscar
     * @return el usuario correspondiente, o null si no se encuentra
     */
    private static Usuario buscarUsuarioPorId(List<Usuario> lista, int id) {
        for (Usuario u : lista) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    /**
     * Exporta todos los movimientos a un archivo CSV, solicitando al usuario una carpeta para guardar el archivo.
     * @param productos lista de productos para reconstruir los movimientos
     * @param usuarios lista de usuarios para reconstruir los movimientos
     */
    public static void exportarMovimientosCSV(List<Producto> productos, List<Usuario> usuarios) {
        // Obtiene todos los movimientos desde la base de datos
        Queue<Movimiento> movimientos = obtenerTodos(productos, usuarios);

        // Muestra un selector de carpetas para elegir dónde guardar el archivo
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccionar carpeta para guardar movimientos.csv");
        File selectedDirectory = chooser.showDialog(null);

        if (selectedDirectory == null) {
            return; // Usuario canceló la selección
        }

        // Crea el archivo "movimientos.csv" en la carpeta elegida
        File archivo = new File(selectedDirectory, "movimientos.csv");

        try (PrintWriter writer = new PrintWriter(archivo)) {
            // Escribe encabezado del CSV
            writer.println("Fecha,Tipo,Cantidad,Justificacion,Codigo Producto,Nombre Producto,Usuario");

            // Escribe cada movimiento en una línea del CSV
            for (Movimiento m : movimientos) {
                String justificacion = (m instanceof Ajuste ajuste) ? ajuste.getJustificacion() : "";
                writer.printf("%s,%s,%d,%s,%s,%s,%s%n",
                        m.getFecha(),
                        m.getTipo(),
                        m.getCantidad(),
                        justificacion,
                        m.getProducto().getCodigo(),
                        m.getProducto().getNombre(),
                        m.getUsuario().getNombre());
            }

            // Muestra alerta informando éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación exitosa");
            alert.setHeaderText(null);
            alert.setContentText("Archivo guardado en: " + archivo.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en consola

            // Muestra alerta de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo exportar movimientos");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
