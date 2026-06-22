package integrado.prog2.services;

import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de negocio de Pedido. Coordina usuarios y productos.
 * La creacion del pedido es en dos pasos para poder cancelarla por
 * completo si falla la carga de un detalle (sin dejar datos inconsistentes).
 *
 * @author Eugenia
 */
public class PedidoService {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // Paso 1: arma el pedido en memoria (todavia NO lo guarda en la coleccion).
    public Pedido iniciarPedido(Long usuarioId, Estado estado, FormaPago formaPago)
            throws EntidadNoEncontradaException {
        Usuario usuario = usuarioService.buscarPorId(usuarioId); // lanza si no existe
        return new Pedido(estado, formaPago, usuario);
    }

    // Paso 2: agrega un detalle validando cantidad y stock. Descuenta stock.
    public void agregarDetalle(Pedido pedido, Long productoId, int cantidad)
            throws DatoInvalidoException, EntidadNoEncontradaException, StockInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor a 0.");
        }
        Producto producto = productoService.buscarPorId(productoId); // lanza si no existe
        productoService.descontarStock(producto, cantidad);          // lanza si no hay stock
        pedido.addDetallePedido(cantidad, producto);                 // crea detalle y recalcula total
    }

    // Paso 3a: confirma el pedido, lo guarda y lo asocia al usuario (relacion bidireccional).
    public void confirmarPedido(Pedido pedido) throws DatoInvalidoException {
        if (pedido.getDetalles().isEmpty()) {
            throw new DatoInvalidoException("No se puede confirmar un pedido sin detalles.");
        }
        pedido.calcularTotal(); // total final via interfaz Calculable
        pedidos.add(pedido);
        pedido.getUsuario().agregarPedido(pedido);
    }

    // Paso 3b: cancela un pedido en creacion devolviendo el stock descontado.
    public void cancelarPedidoEnCreacion(Pedido pedido) {
        for (DetallePedido d : pedido.getDetalles()) {
            productoService.reponerStock(d.getProducto(), d.getCantidad());
        }
    }

    // READ (solo no eliminados)
    public List<Pedido> listar() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    // READ por usuario
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado() && p.getUsuario() != null
                    && p.getUsuario().getId().equals(usuarioId)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // READ por id
    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No existe un pedido activo con id: " + id);
    }

    // UPDATE estado y/o forma de pago
    public void actualizarEstadoYFormaPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago)
            throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        if (nuevoEstado != null) {
            p.setEstado(nuevoEstado);
        }
        if (nuevaFormaPago != null) {
            p.setFormaPago(nuevaFormaPago);
        }
    }

    // DELETE (baja logica). Tambien marca sus detalles para no dejar datos sueltos.
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
        for (DetallePedido d : p.getDetalles()) {
            d.setEliminado(true);
        }
    }
}
