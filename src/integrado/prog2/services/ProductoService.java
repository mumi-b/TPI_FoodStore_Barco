package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de negocio de Producto. Necesita el CategoriaService para
 * asociar cada producto a una categoria valida.
 *
 * @author Eugenia
 */
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private final CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // CREATE
    public Producto crear(String nombre, Double precio, String descripcion, int stock,
                          String imagen, boolean disponible, Long categoriaId)
            throws DatoInvalidoException, StockInvalidoException, EntidadNoEncontradaException {

        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre del producto no puede estar vacio.");
        }
        if (precio == null || precio < 0) {
            throw new StockInvalidoException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo.");
        }
        // Si la categoria no existe o esta eliminada, buscarPorId lanza la excepcion.
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        Producto producto = new Producto(nombre.trim(), precio, descripcion, stock, imagen, disponible, categoria);
        productos.add(producto);
        categoria.agregarProducto(producto); // relacion bidireccional
        return producto;
    }

    // READ (solo no eliminados)
    public List<Producto> listar() {
        List<Producto> activos = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    // READ por categoria
    public List<Producto> listarPorCategoria(Long categoriaId) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado() && p.getCategoria() != null
                    && p.getCategoria().getId().equals(categoriaId)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // READ por id
    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto p : productos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No existe un producto activo con id: " + id);
    }

    // UPDATE
    public void editar(Long id, String nombre, Double precio, String descripcion,
                       Integer stock, String imagen, Boolean disponible, Long categoriaId)
            throws EntidadNoEncontradaException, StockInvalidoException {

        Producto p = buscarPorId(id);

        if (nombre != null && !nombre.isBlank()) {
            p.setNombre(nombre.trim());
        }
        if (precio != null) {
            if (precio < 0) {
                throw new StockInvalidoException("El precio no puede ser negativo.");
            }
            p.setPrecio(precio);
        }
        if (descripcion != null && !descripcion.isBlank()) {
            p.setDescripcion(descripcion);
        }
        if (stock != null) {
            if (stock < 0) {
                throw new StockInvalidoException("El stock no puede ser negativo.");
            }
            p.setStock(stock);
        }
        if (imagen != null && !imagen.isBlank()) {
            p.setImagen(imagen);
        }
        if (disponible != null) {
            p.setDisponible(disponible);
        }
        if (categoriaId != null) {
            Categoria nueva = categoriaService.buscarPorId(categoriaId);
            p.setCategoria(nueva);
            nueva.agregarProducto(p);
        }
    }

    // DELETE (baja logica)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Producto p = buscarPorId(id);
        p.setEliminado(true);
    }

    // Descuenta stock cuando se vende. Lanza excepcion si no alcanza.
    public void descontarStock(Producto producto, int cantidad) throws StockInvalidoException {
        if (cantidad > producto.getStock()) {
            throw new StockInvalidoException(
                    "Stock insuficiente de '" + producto.getNombre()
                    + "'. Disponible: " + producto.getStock() + ", pedido: " + cantidad);
        }
        producto.setStock(producto.getStock() - cantidad);
    }

    // Repone stock (se usa para revertir si se cancela la carga de un pedido).
    public void reponerStock(Producto producto, int cantidad) {
        producto.setStock(producto.getStock() + cantidad);
    }
}
