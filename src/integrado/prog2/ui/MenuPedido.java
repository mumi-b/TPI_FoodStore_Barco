package integrado.prog2.ui;

import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.services.PedidoService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;
import java.util.List;

/**
 * Submenu CRUD de Pedidos (Epica 4). Es el mas completo porque la creacion
 * de un pedido carga varios detalles, descuenta stock y, si algo falla,
 * cancela todo el pedido para no dejar datos inconsistentes.
 *
 * @author Eugenia
 */
public class MenuPedido {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public MenuPedido(PedidoService pedidoService, UsuarioService usuarioService,
                      ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear pedido con detalles");
            System.out.println("3. Actualizar estado / forma de pago");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = ConsolaUtil.leerEntero("Seleccione: ");

            switch (opcion) {
                case 1: listar(); break;
                case 2: crear(); break;
                case 3: actualizar(); break;
                case 4: eliminar(); break;
                case 0: System.out.println("Volviendo al menu principal..."); break;
                default: System.out.println("(!) Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
            return;
        }
        System.out.println("\nPedidos registrados:");
        for (Pedido p : pedidos) {
            System.out.println(p);
            for (DetallePedido d : p.getDetalles()) {
                System.out.println(d);
            }
        }
    }

    private void crear() {
        // 1) Elegir usuario
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            System.out.println("(!) Primero debe crear al menos un usuario.");
            return;
        }
        System.out.println("\nUsuarios disponibles:");
        for (Usuario u : usuarios) {
            System.out.println(u);
        }
        Long usuarioId = ConsolaUtil.leerLong("Id del usuario: ");

        // 2) Elegir estado y forma de pago
        Estado estado = elegirEstado();
        FormaPago formaPago = elegirFormaPago();

        // 3) Iniciar el pedido (todavia no se guarda)
        Pedido pedido;
        try {
            pedido = pedidoService.iniciarPedido(usuarioId, estado, formaPago);
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
            return;
        }

        // 4) Cargar detalles. Si uno falla, se cancela TODO el pedido (rollback de stock).
        boolean seguir = true;
        while (seguir) {
            List<Producto> productos = productoService.listar();
            if (productos.isEmpty()) {
                System.out.println("(!) No hay productos disponibles. Se cancela el pedido.");
                pedidoService.cancelarPedidoEnCreacion(pedido);
                return;
            }
            System.out.println("\nProductos disponibles:");
            for (Producto p : productos) {
                System.out.println(p);
            }
            Long productoId = ConsolaUtil.leerLong("Id del producto: ");
            int cantidad = ConsolaUtil.leerEntero("Cantidad: ");

            try {
                pedidoService.agregarDetalle(pedido, productoId, cantidad);
                System.out.println("(OK) Detalle agregado. Total parcial: $" + pedido.getTotal());
            } catch (Exception e) {
                System.out.println("(!) " + e.getMessage());
                System.out.println("(!) Se cancela el pedido completo para no dejar datos inconsistentes.");
                pedidoService.cancelarPedidoEnCreacion(pedido);
                return;
            }

            seguir = ConsolaUtil.leerSiNo("Desea agregar otro detalle?");
        }

        // 5) Confirmar el pedido (total final via Calculable)
        try {
            pedidoService.confirmarPedido(pedido);
            System.out.println("(OK) Pedido #" + pedido.getId()
                    + " confirmado. Total: $" + pedido.getTotal());
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
            pedidoService.cancelarPedidoEnCreacion(pedido);
        }
    }

    private void actualizar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del pedido a actualizar: ");
        Estado estado = null;
        FormaPago formaPago = null;
        if (ConsolaUtil.leerSiNo("Desea cambiar el estado?")) {
            estado = elegirEstado();
        }
        if (ConsolaUtil.leerSiNo("Desea cambiar la forma de pago?")) {
            formaPago = elegirFormaPago();
        }
        try {
            pedidoService.actualizarEstadoYFormaPago(id, estado, formaPago);
            System.out.println("(OK) Pedido actualizado.");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        Long id = ConsolaUtil.leerLong("Id del pedido a eliminar: ");
        if (!ConsolaUtil.leerSiNo("Confirma la baja")) {
            System.out.println("Operacion cancelada.");
            return;
        }
        try {
            pedidoService.eliminar(id);
            System.out.println("(OK) Pedido eliminado (baja logica).");
        } catch (Exception e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private Estado elegirEstado() {
        System.out.println("Estado: 1) PENDIENTE  2) CONFIRMADO  3) TERMINADO  4) CANCELADO");
        int op = ConsolaUtil.leerEntero("Seleccione estado: ");
        switch (op) {
            case 2: return Estado.CONFIRMADO;
            case 3: return Estado.TERMINADO;
            case 4: return Estado.CANCELADO;
            default: return Estado.PENDIENTE;
        }
    }

    private FormaPago elegirFormaPago() {
        System.out.println("Forma de pago: 1) TARJETA  2) TRANSFERENCIA  3) EFECTIVO");
        int op = ConsolaUtil.leerEntero("Seleccione forma de pago: ");
        switch (op) {
            case 2: return FormaPago.TRANSFERENCIA;
            case 3: return FormaPago.EFECTIVO;
            default: return FormaPago.TARJETA;
        }
    }
}
