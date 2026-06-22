package integrado.prog2.exception;

/**
 * Se lanza cuando un dato ingresado no cumple una regla basica
 * (nombre vacio, cantidad <= 0, etc.).
 */
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
