package integrado.prog2.exception;

/**
 * Se lanza cuando se busca una entidad por id y no existe (o esta eliminada).
 */
public class EntidadNoEncontradaException extends Exception {

    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
