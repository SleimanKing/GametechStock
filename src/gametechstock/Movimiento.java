package gametechstock; // Paquete principal del sistema

import java.time.LocalDateTime; // Importa la clase para manejar fecha y hora del sistema

/**
 * Clase abstracta que representa un movimiento de stock.
 * Puede ser de tipo Ingreso, Egreso o Ajuste.
 * Define los atributos y comportamientos comunes a todos los tipos de movimientos.
 */
public abstract class Movimiento {

    // Fecha y hora en que se registró el movimiento
    protected LocalDateTime fecha;

    // Cantidad de unidades implicadas en el movimiento
    protected int cantidad;

    // Producto sobre el cual se aplica el movimiento
    protected Producto producto;

    // Usuario que realiza el movimiento
    protected Usuario usuario;

    // Justificación (usada en Ajuste; puede ser null en Ingreso o Egreso)
    protected String justificacion;


    /**
     * Constructor del movimiento.
     * Se llama desde las subclases (Ingreso, Egreso, Ajuste).
     *
     * @param cantidad       cantidad implicada en el movimiento
     * @param producto       producto afectado
     * @param usuario        usuario que lo realiza
     * @param justificacion  motivo (solo para ajustes; null en otros casos)
     */
    public Movimiento(int cantidad, Producto producto, Usuario usuario, String justificacion) {
        this.fecha = LocalDateTime.now();      // Registra la fecha y hora actuales al crear el movimiento
        this.cantidad = cantidad;              // Cantidad de unidades que se suman/restan/ajustan
        this.producto = producto;              // Producto al que se le aplica el movimiento
        this.usuario = usuario;                // Usuario que realiza el movimiento
        this.justificacion = justificacion;    // Justificación (solo útil para ajustes)
    }

    /**
     * Método abstracto que debe implementar cada subclase.
     * Define cómo aplicar el movimiento al producto (ingresar, egresar, ajustar).
     *
     * @throws Exception si ocurre un error al aplicar el movimiento
     */
    public abstract void aplicar() throws Exception;

    /**
     * Devuelve una cadena que representa el movimiento en formato de auditoría (Lo guardamos para un futuro).
     * Incluye tipo, fecha, producto, cantidad y usuario.
     * @return String con resumen del movimiento.
     */
    public String auditar() {
        return String.format("[%s] %s - Producto: %s - Cant: %d - Usuario: %s",
                fecha, this.getClass().getSimpleName(), producto.getCodigo(), cantidad, usuario.getNombre());
    }

    /**
     * Permite sobrescribir la fecha del movimiento (por ejemplo al cargar desde BD).
     * @param fecha fecha específica a establecer
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve la fecha y hora del movimiento.
     * @return fecha del movimiento como LocalDateTime
     */
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    /**
     * Devuelve el producto sobre el cual se aplicó el movimiento.
     * @return producto afectado
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Devuelve el usuario que realizó el movimiento.
     * @return usuario responsable
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve la cantidad implicada en el movimiento.
     * @return cantidad positiva o negativa según el tipo de movimiento
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Devuelve la justificación del movimiento (solo se usa en ajustes).
     * @return texto justificativo o null si no aplica
     */
    public String getJustificacion() {
        return justificacion;
    }

    /**
     * Método abstracto que devuelve el tipo de movimiento.
     * Lo implementan las subclases como "INGRESO", "EGRESO" o "AJUSTE".
     * @return tipo de movimiento como texto
     */
    public abstract String getTipo();
}
