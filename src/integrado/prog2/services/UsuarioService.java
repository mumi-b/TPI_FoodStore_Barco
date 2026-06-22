package integrado.prog2.services;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.MailDuplicadoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de negocio de Usuario. Valida que el mail sea unico.
 *
 * @author Eugenia
 */
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();

    // CREATE
    public Usuario crear(String nombre, String apellido, String mail, String celular,
                         String contrasenia, Rol rol)
            throws DatoInvalidoException, MailDuplicadoException {

        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre no puede estar vacio.");
        }
        if (mail == null || mail.isBlank()) {
            throw new DatoInvalidoException("El mail no puede estar vacio.");
        }
        if (existeMail(mail)) {
            throw new MailDuplicadoException("Ya existe un usuario con el mail: " + mail);
        }
        Usuario usuario = new Usuario(nombre.trim(), apellido, mail.trim(), celular, contrasenia, rol);
        usuarios.add(usuario);
        return usuario;
    }

    // READ (solo no eliminados)
    public List<Usuario> listar() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.isEliminado()) {
                activos.add(u);
            }
        }
        return activos;
    }

    // READ por id
    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id) && !u.isEliminado()) {
                return u;
            }
        }
        throw new EntidadNoEncontradaException("No existe un usuario activo con id: " + id);
    }

    // UPDATE
    public void editar(Long id, String nombre, String apellido, String mail,
                       String celular, Rol rol)
            throws EntidadNoEncontradaException, MailDuplicadoException {

        Usuario u = buscarPorId(id);

        if (nombre != null && !nombre.isBlank()) {
            u.setNombre(nombre.trim());
        }
        if (apellido != null && !apellido.isBlank()) {
            u.setApellido(apellido);
        }
        if (mail != null && !mail.isBlank()) {
            // Si cambia el mail, validar que no lo tenga otro usuario.
            if (!mail.equalsIgnoreCase(u.getMail()) && existeMail(mail)) {
                throw new MailDuplicadoException("Ya existe un usuario con el mail: " + mail);
            }
            u.setMail(mail.trim());
        }
        if (celular != null && !celular.isBlank()) {
            u.setCelular(celular);
        }
        if (rol != null) {
            u.setRol(rol);
        }
    }

    // DELETE (baja logica)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Usuario u = buscarPorId(id);
        u.setEliminado(true);
    }

    // Auxiliar: indica si ya hay un usuario activo con ese mail.
    private boolean existeMail(String mail) {
        for (Usuario u : usuarios) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail.trim())) {
                return true;
            }
        }
        return false;
    }
}
