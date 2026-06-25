package integrado.prog2.exception;

/**
 * Se lanza cuando un valor de stock es invalido (negativo)
 * o cuando no hay stock suficiente para un detalle de pedido.
 */
public class StockInvalidoException extends Exception {

    public StockInvalidoException(String mensaje) {
        super(mensaje);
    }
}
