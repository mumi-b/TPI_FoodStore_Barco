package integrado.prog2.ui;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.services.UsuarioService;
import java.util.List;

/**
 * Submenu CRUD de Usuarios (Epica 3).
 *
 * @author Eugenia
 */
public class MenuUsuario {

    private final UsuarioService usuarioService;

    public MenuUsuario(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = ConsolaUtil.leerEntero("Seleccione: ");

            switch (opcion) {
                case 1: listar(); break;
                case 2: crear(); break;
                case 3: editar(); break;
                case 4: eliminar(); break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("(!) Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        System.out.println("\nUsuarios registrados:");
        for (Usuario u : usuarios) {
            System.out.println(u);
        }
    }

    private void crear() {
        String nombre = ConsolaUtil.leerTexto("Nombre: ");
        String apellido = ConsolaUtil.leerTexto("Apellido: ");
        String mail = ConsolaUtil.leerTexto("Mail: ");
        String celular = ConsolaUtil.leerTexto("Celular: ");
        String contrasenia = ConsolaUtil.leerTexto("Contrasenia: ");
        Rol rol = elegirRol();

        try {
            Usuario nuevo = usuarioService.crear(nombre, apellido, mail, celular, contrasenia, rol);
            System.out.println("(OK) Usuario creado con id " + nuevo.getId() + ".");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del usuario a editar: ");
        String nombre = ConsolaUtil.leerTexto("Nuevo nombre (Enter para dejar igual): ");
        String apellido = ConsolaUtil.leerTexto("Nuevo apellido (Enter para dejar igual): ");
        String mail = ConsolaUtil.leerTexto("Nuevo mail (Enter para dejar igual): ");
        String celular = ConsolaUtil.leerTexto("Nuevo celular (Enter para dejar igual): ");
        Rol rol = null;
        if (ConsolaUtil.leerSiNo("Desea cambiar el rol?")) {
            rol = elegirRol();
        }

        try {
            usuarioService.editar(id,
                    nombre.isBlank() ? null : nombre,
                    apellido.isBlank() ? null : apellido,
                    mail.isBlank() ? null : mail,
                    celular.isBlank() ? null : celular,
                    rol);
            System.out.println("(OK) Usuario actualizado.");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del usuario a eliminar: ");
        if (!ConsolaUtil.leerSiNo("Confirma la baja")) {
            System.out.println("Operacion cancelada.");
            return;
        }
        try {
            usuarioService.eliminar(id);
            System.out.println("(OK) Usuario eliminado (baja logica).");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    // Pide el rol mostrando las opciones del enum.
    private Rol elegirRol() {
        System.out.println("Rol: 1) ADMIN  2) USUARIO");
        int op = ConsolaUtil.leerEntero("Seleccione rol: ");
        return (op == 1) ? Rol.ADMIN : Rol.USUARIO;
    }
}
