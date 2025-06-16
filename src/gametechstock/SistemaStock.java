package gametechstock; // Paquete principal del sistema

import ConexionBD.MovimientoDAO;  // DAO para manejar movimientos desde/hacia la base de datos
import ConexionBD.ProductoDAO;    // DAO para manejar productos desde/hacia la base de datos
import ConexionBD.UsuarioDAO;     // DAO para manejar usuarios desde/hacia la base de datos
import java.util.*;

/**
 * Clase que representa la lógica central del sistema de gestión de stock.
 * Contiene y administra productos, movimientos, usuarios y la sesión activa.
 */
public class SistemaStock {

    // Lista de productos registrados en el sistema
    private ArrayList<Producto> productos = new ArrayList<>();

    // Cola de movimientos históricos (FIFO)
    private Queue<Movimiento> movimientos = new LinkedList<>();

    // Lista de usuarios disponibles en el sistema
    private List<Usuario> usuarios = new ArrayList<>();

    // Usuario actualmente logueado en el sistema
    private Usuario usuarioActual;

    /**
     * Carga todos los datos iniciales del sistema desde la base de datos:
     * productos, usuarios y movimientos.
     */
    public void cargarDatosDesdeBD() {
        this.productos = ProductoDAO.obtenerProductos();                      // Carga productos desde BD
        this.usuarios = UsuarioDAO.obtenerUsuarios();                         // Carga usuarios desde BD
        this.movimientos = MovimientoDAO.obtenerTodos(productos, usuarios);   // Carga movimientos desde BD con referencias
    }

    /**
     * Establece el usuario que está actualmente logueado.
     * @param usuario objeto Usuario activo
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario; // Guarda el usuario como sesión activa
    }

    /**
     * Devuelve la lista de productos del sistema.
     * @return lista de productos
     */
    public ArrayList<Producto> getProductos() {
        return productos;
    }

    /**
     * Devuelve la cola de movimientos registrados.
     * @return movimientos en orden cronológico (FIFO)
     */
    public Queue<Movimiento> getMovimientos() {
        return movimientos;
    }

    /**
     * Devuelve el usuario actualmente autenticado.
     * @return usuario logueado
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Devuelve la lista de usuarios registrados en el sistema.
     * @return lista de usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
