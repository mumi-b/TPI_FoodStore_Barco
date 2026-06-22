package integrado.prog2.entities;

/**
 * Linea de un pedido: un producto con su cantidad y su subtotal.
 * Relacion N..1 con Producto.
 *
 * @author Eugenia
 */
public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        calcularSubtotal();
    }

    // subtotal = cantidad * precio del producto
    public void calcularSubtotal() {
        this.subtotal = cantidad * producto.getPrecio();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal(); // vuelve a calcular al cambiar la cantidad
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return String.format("   - %d x %s = $%.2f",
                cantidad, producto.getNombre(), subtotal);
    }
}
