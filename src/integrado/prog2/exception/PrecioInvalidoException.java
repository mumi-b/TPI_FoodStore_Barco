package integrado.prog2.exception;

/**
 * Se lanza cuando el precio de un producto es invalido (negativo).
 */
public class PrecioInvalidoException extends Exception {

    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
