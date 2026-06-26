package integrado.prog2.ui;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.ProductoService;
import java.util.List;

/**
 * Submenu CRUD de Productos (Epica 2).
 *
 * @author Eugenia
 */
public class MenuProducto {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public MenuProducto(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n GESTION DE PRODUCTOS ");
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
        List<Producto> productos = productoService.listar();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        System.out.println("\nProductos registrados:");
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    private void crear() {
        // Primero mostramos las categorias para que el usuario sepa que id elegir.
        List<Categoria> categorias = categoriaService.listar();
        if (categorias.isEmpty()) {
            System.out.println("(!) Primero debe crear al menos una categoria.");
            return;
        }
        System.out.println("\nCategorias disponibles:");
        for (Categoria c : categorias) {
            System.out.println(c);
        }

        String nombre = ConsolaUtil.leerTextoObligatorio("Nombre del producto: ");
        String descripcion = ConsolaUtil.leerTexto("Descripcion: ");
        double precio = ConsolaUtil.leerDouble("Precio: ");
        int stock = ConsolaUtil.leerEntero("Stock: ");
        String imagen = ConsolaUtil.leerTexto("Nombre de imagen (ej: foto.jpg): ");
        boolean disponible = ConsolaUtil.leerSiNo("Esta disponible?");
        Long categoriaId = ConsolaUtil.leerLong("Id de la categoria: ");

        try {
            Producto nuevo = productoService.crear(nombre, precio, descripcion, stock,
                    imagen, disponible, categoriaId);
            System.out.println("(OK) Producto creado con id " + nuevo.getId() + ".");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del producto a editar: ");
        System.out.println("Deje vacio (Enter) o escriba '-' en los numericos para no modificar ese campo.");

        String nombre = ConsolaUtil.leerTexto("Nuevo nombre: ");
        Double precio = leerDoubleOpcional("Nuevo precio: ");
        String descripcion = ConsolaUtil.leerTexto("Nueva descripcion: ");
        Integer stock = leerEnteroOpcional("Nuevo stock: ");
        String imagen = ConsolaUtil.leerTexto("Nueva imagen: ");
        Boolean disponible = leerBooleanOpcional("Disponible? (S/N, Enter para no cambiar): ");
        Long categoriaId = leerLongOpcional("Nuevo id de categoria: ");

        try {
            productoService.editar(id,
                    nombre.isBlank() ? null : nombre,
                    precio, descripcion.isBlank() ? null : descripcion,
                    stock, imagen.isBlank() ? null : imagen,
                    disponible, categoriaId);
            System.out.println("(OK) Producto actualizado.");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del producto a eliminar: ");
        if (!ConsolaUtil.leerSiNo("Confirma la baja")) {
            System.out.println("Operacion cancelada.");
            return;
        }
        try {
            productoService.eliminar(id);
            System.out.println("(OK) Producto eliminado (baja logica).");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    // Helpers para campos opcionales en la edicion

    private Double leerDoubleOpcional(String mensaje) {
        String entrada = ConsolaUtil.leerTexto(mensaje);
        if (entrada.isBlank() || entrada.equals("-")) {
            return null;
        }
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("(aviso) Valor no numerico, se deja sin cambios.");
            return null;
        }
    }

    private Integer leerEnteroOpcional(String mensaje) {
        String entrada = ConsolaUtil.leerTexto(mensaje);
        if (entrada.isBlank() || entrada.equals("-")) {
            return null;
        }
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("(aviso) Valor no numerico, se deja sin cambios.");
            return null;
        }
    }

    private Long leerLongOpcional(String mensaje) {
        String entrada = ConsolaUtil.leerTexto(mensaje);
        if (entrada.isBlank() || entrada.equals("-")) {
            return null;
        }
        try {
            return Long.parseLong(entrada);
        } catch (NumberFormatException e) {
            System.out.println("(aviso) Valor no numerico, se deja sin cambios.");
            return null;
        }
    }

    private Boolean leerBooleanOpcional(String mensaje) {
        String entrada = ConsolaUtil.leerTexto(mensaje);
        if (entrada.isBlank()) {
            return null;
        }
        return entrada.equalsIgnoreCase("S");
    }
}
