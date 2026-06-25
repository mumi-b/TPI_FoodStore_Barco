package integrado.prog2;

import integrado.prog2.enums.Rol;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.PedidoService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;
import integrado.prog2.ui.ConsolaUtil;
import integrado.prog2.ui.MenuCategoria;
import integrado.prog2.ui.MenuPedido;
import integrado.prog2.ui.MenuProducto;
import integrado.prog2.ui.MenuUsuario;

/**
 * Punto de entrada del sistema Food Store (TPI).
 * Crea los servicios, los conecta entre si, carga datos de ejemplo
 * y muestra el menu principal en bucle.
 *
 * @author Eugenia
 */
public class Main {

    public static void main(String[] args) {

        // 1) Se crean los servicios y se conectan (inyeccion de dependencias simple).
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService(categoriaService);
        UsuarioService usuarioService = new UsuarioService();
        PedidoService pedidoService = new PedidoService(usuarioService, productoService);

        // 2) Se crean los menus, cada uno con los servicios que necesita.
        MenuCategoria menuCategoria = new MenuCategoria(categoriaService);
        MenuProducto menuProducto = new MenuProducto(productoService, categoriaService);
        MenuUsuario menuUsuario = new MenuUsuario(usuarioService);
        MenuPedido menuPedido = new MenuPedido(pedidoService, usuarioService, productoService);

        // 3) Datos de ejemplo para que el sistema no arranque vacio (facilita la demo).
        cargarDatosEjemplo(categoriaService, productoService, usuarioService);

        // 4) Menu principal en bucle hasta que el usuario elija Salir.
        int opcion;
        do {
            System.out.println("\n SISTEMA DE PEDIDOS (FOOD STORE) ");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            opcion = ConsolaUtil.leerEntero("Seleccione: ");

            switch (opcion) {
                case 1: menuCategoria.mostrar(); break;
                case 2: menuProducto.mostrar(); break;
                case 3: menuUsuario.mostrar(); break;
                case 4: menuPedido.mostrar(); break;
                case 0: System.out.println("Saliendo del sistema. Hasta luego!"); break;
                default: System.out.println("(!) Opcion invalida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // Carga unas pocas categorias, productos y usuarios de ejemplo.
    private static void cargarDatosEjemplo(CategoriaService categoriaService,
                                           ProductoService productoService,
                                           UsuarioService usuarioService) {
        try {
            categoriaService.crear("Bebidas", "Gaseosas y jugos");
            categoriaService.crear("Lacteos", "Leche, queso, yogur");
            categoriaService.crear("Snacks", "Papas, galletitas");

            // Ids 1, 2, 3 corresponden a las categorias recien creadas.
            productoService.crear("Coca Cola", 5000.0, "Botella 1.5L", 100, "coca.jpg", true, 1L);
            productoService.crear("Agua Mineral", 2000.0, "Botella 500ml", 80, "agua.jpg", true, 1L);
            productoService.crear("Queso Cremoso", 11000.0, "Paquete 450g", 15, "queso.jpg", true, 2L);
            productoService.crear("Yogur Griego", 7000.0, "Pote 200ml", 10, "yogur.jpg", true, 2L);
            productoService.crear("Papas Lays", 10000.0, "Bolsa 90g", 20, "lays.jpg", true, 3L);
            productoService.crear("Pipas", 3550.0, "Bolsa 30g", 35, "pipas.jpg", true, 3L);

            usuarioService.crear("Ana", "Perez", "ana@mail.com", "37000111", "1234", Rol.ADMIN);
            usuarioService.crear("Luis", "Gomez", "luis@mail.com", "37000222", "5678", Rol.USUARIO);

            System.out.println("(info) Datos de ejemplo cargados correctamente.");
        } catch (Exception e) {
            System.out.println("(!) Error cargando datos de ejemplo: " + e.getMessage());
        }
    }
}
