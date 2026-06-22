package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de un usuario. Implementa Calculable (sabe calcular su total)
 * y tiene una composicion 1..N con DetallePedido.
 *
 * @author Eugenia
 */
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private FormaPago formaPago;
    private Double total;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    public Pedido(Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = LocalDate.now();
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    // Recorre los detalles y suma sus subtotales (cumple la interfaz Calculable).
    @Override
    public void calcularTotal() {
        double suma = 0;
        for (DetallePedido detalle : detalles) {
            suma += detalle.getSubtotal();
        }
        this.total = suma;
    }

    // Crea el detalle, lo agrega a la lista y recalcula el total. Asi el total queda siempre al dia.
    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido detalle = new DetallePedido(cantidad, producto);
        detalles.add(detalle);
        calcularTotal();
    }

    // Busca un detalle por el producto (compara por id con equals). Devuelve null si no esta.
    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }
        return null;
    }

    // Elimina un detalle por su producto y recalcula el total.
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            calcularTotal();
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        String nombreUsuario = (usuario != null)
                ? usuario.getNombre() + " " + usuario.getApellido()
                : "sin usuario";
        return String.format("Pedido #%d - %s - Usuario: %s - Estado: %s - Pago: %s - Total: $%.2f",
                getId(), fecha, nombreUsuario, estado, formaPago, total);
    }
}
