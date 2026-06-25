package integrado.prog2.ui;

import integrado.prog2.entities.Categoria;
import integrado.prog2.services.CategoriaService;
import java.util.List;

/**
 * Submenu CRUD de Categorias .
 * Solo se encarga de la interaccion con el usuario y de mostrar mensajes;
 * la logica vive en CategoriaService.
 *
 * @author Eugenia
 */
public class MenuCategoria {

    private final CategoriaService categoriaService;

    public MenuCategoria(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n GESTION DE CATEGORIAS ");
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
        List<Categoria> categorias = categoriaService.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        System.out.println("\nCategorias registradas:");
        for (Categoria c : categorias) {
            System.out.println(c);
        }
    }

    private void crear() {
        String nombre = ConsolaUtil.leerTexto("Nombre: ");
        String descripcion = ConsolaUtil.leerTexto("Descripcion: ");
        try {
            Categoria nueva = categoriaService.crear(nombre, descripcion);
            System.out.println("(OK) Categoria creada con id " + nueva.getId() + ".");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id de la categoria a editar: ");
        String nombre = ConsolaUtil.leerTexto("Nuevo nombre (Enter para dejar igual): ");
        String descripcion = ConsolaUtil.leerTexto("Nueva descripcion (Enter para dejar igual): ");
        try {
            categoriaService.editar(id, nombre, descripcion);
            System.out.println("(OK) Categoria actualizada.");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id de la categoria a eliminar: ");
        if (!ConsolaUtil.leerSiNo("Confirma la baja")) {
            System.out.println("Operacion cancelada.");
            return;
        }
        try {
            categoriaService.eliminar(id);
            System.out.println("(OK) Categoria eliminada (baja logica).");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }
}
