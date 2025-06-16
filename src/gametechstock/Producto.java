package gametechstock; // Paquete principal del sistema

/**
 * Clase que representa un producto del stock.
 * Contiene información como código, nombre, categoría, stock mínimo, stock actual y el depósito asignado.
 */
public class Producto {

    // Código único del producto (ej: P001)
    private String codigo;

    // Nombre descriptivo del producto
    private String nombre;

    // Categoría a la que pertenece (Ej: Hardware, Periférico)
    private String categoria;

    // Cantidad mínima recomendada para mantener en stock
    private int stockMinimo;

    // Cantidad actual en stock
    private int stockActual;

    // Depósito donde se encuentra almacenado el producto
    private Deposito deposito;

    /**
     * Constructor de producto.
     *
     * @param codigo        identificador único del producto
     * @param nombre        nombre descriptivo
     * @param categoria     categoría del producto
     * @param stockMinimo   cantidad mínima sugerida
     * @param stockActual   cantidad actual disponible
     * @param deposito      depósito donde está almacenado
     */
    public Producto(String codigo, String nombre, String categoria, int stockMinimo, int stockActual, Deposito deposito) {
        this.codigo = codigo;           // Asigna el código del producto
        this.nombre = nombre;           // Asigna el nombre
        this.categoria = categoria;     // Asigna la categoría
        this.stockMinimo = stockMinimo; // Asigna el stock mínimo
        this.stockActual = stockActual; // Asigna el stock actual
        this.deposito = deposito;       // Asigna el depósito al que pertenece
    }

    /**
     * Registra un ingreso de stock (entrada de unidades).
     * @param cantidad cantidad a sumar al stock actual
     */
    public void registrarIngreso(int cantidad) {
        stockActual += cantidad; // Suma la cantidad ingresada al stock actual
    }

    /**
     * Registra un egreso de stock (salida de unidades).
     * @param cantidad cantidad a restar del stock actual
     * @throws Exception si no hay suficiente stock disponible
     */
    public void registrarEgreso(int cantidad) throws Exception {
        if (cantidad > stockActual) throw new Exception("Stock insuficiente."); // Valida que haya stock
        stockActual -= cantidad; // Resta del stock actual
    }

    /**
     * Ajusta el stock del producto sumando o restando la cantidad indicada.
     * @param ajuste la cantidad a ajustar (puede ser negativa)
     * @throws Exception si el ajuste genera stock negativo
     */
    public void ajustarStock(int ajuste) throws Exception {
        if (stockActual + ajuste < 0) {
            throw new Exception("Stock insuficiente para realizar el ajuste."); // Evita stock negativo
        }
        stockActual += ajuste; // Aplica el ajuste
    }

    /**
     * Indica si el stock actual está por debajo del mínimo recomendado.
     * @return true si es crítico, false si está en nivel normal
     */
    public boolean esCritico() {
        return stockActual < stockMinimo; // Retorna true si el stock está por debajo del mínimo
    }

    // Métodos setters y getters para acceder a los atributos encapsulados

    public String getCodigo() { return codigo; } // Devuelve el código del producto

    public String getNombre() { return nombre; } // Devuelve el nombre del producto

    public int getStockActual() { return stockActual; } // Devuelve el stock actual

    public String getCategoria() { return categoria; } // Devuelve la categoría del producto

    public int getStockMinimo() { return stockMinimo; } // Devuelve el stock mínimo

    public Deposito getDeposito() { return deposito; } // Devuelve el depósito asignado

    public void setCodigo(String codigo) {
        this.codigo = codigo; // Establece un nuevo código
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    } // Establece el ajuste de nuevo ingreso de mercadería a memoria para que se refleje en busquedas
}
