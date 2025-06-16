package gametechstock;

import ConexionBD.MovimientoDAO;

/**
 * Representa un movimiento de tipo Ajuste (positivo o negativo) sobre el stock.
 */
public class Ajuste extends Movimiento {

    /**
     * Constructor del ajuste.
     * @param cantidad cantidad a ajustar (puede ser positiva o negativa)
     * @param producto producto afectado
     * @param usuario  usuario responsable
     * @param justificacion motivo del ajuste
     */
    public Ajuste(int cantidad, Producto producto, Usuario usuario, String justificacion) {
        super(cantidad, producto, usuario, justificacion);
    }

    /**
     * Aplica el ajuste al stock del producto y lo guarda en la base de datos.
     */
    @Override
    public void aplicar() throws Exception {
        producto.ajustarStock(cantidad);
        MovimientoDAO.guardarMovimiento(this);
    }

    @Override
    public String getTipo() {
        return "AJUSTE";
    }

    public String getJustificacion() {
        return justificacion;
    }
}
