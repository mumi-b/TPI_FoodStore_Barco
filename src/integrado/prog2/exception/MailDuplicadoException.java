package integrado.prog2.exception;

/**
 * Se lanza cuando se intenta crear o editar un usuario con un mail
 * que ya esta registrado por otro usuario.
 */
public class MailDuplicadoException extends Exception {

    public MailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
