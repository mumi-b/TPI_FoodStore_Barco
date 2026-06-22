package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de negocio de Categoria. Guarda las categorias en una coleccion
 * en memoria y ofrece las operaciones CRUD con sus validaciones.
 *
 * @author Eugenia
 */
public class CategoriaService {

    private final List<Categoria> categorias = new ArrayList<>();

    // CREATE
    public Categoria crear(String nombre, String descripcion) throws DatoInvalidoException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre de la categoria no puede estar vacio.");
        }
        if (existeNombre(nombre)) {
            throw new DatoInvalidoException("Ya existe una categoria con el nombre: " + nombre);
        }
        Categoria categoria = new Categoria(nombre.trim(), descripcion);
        categorias.add(categoria);
        return categoria;
    }

    // READ (solo las no eliminadas)
    public List<Categoria> listar() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria c : categorias) {
            if (!c.isEliminado()) {
                activas.add(c);
            }
        }
        return activas;
    }

    // READ por id (debe existir y no estar eliminada)
    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria c : categorias) {
            if (c.getId().equals(id) && !c.isEliminado()) {
                return c;
            }
        }
        throw new EntidadNoEncontradaException("No existe una categoria activa con id: " + id);
    }

    // UPDATE
    public void editar(Long id, String nuevoNombre, String nuevaDescripcion)
            throws EntidadNoEncontradaException, DatoInvalidoException {
        Categoria categoria = buscarPorId(id);
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            // Si cambia el nombre, controlar que no choque con otra categoria
            if (!nuevoNombre.equalsIgnoreCase(categoria.getNombre()) && existeNombre(nuevoNombre)) {
                throw new DatoInvalidoException("Ya existe una categoria con el nombre: " + nuevoNombre);
            }
            categoria.setNombre(nuevoNombre.trim());
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            categoria.setDescripcion(nuevaDescripcion);
        }
    }

    // DELETE (baja logica)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Categoria categoria = buscarPorId(id);
        categoria.setEliminado(true);
    }

    // Auxiliar: indica si ya hay una categoria activa con ese nombre.
    private boolean existeNombre(String nombre) {
        for (Categoria c : categorias) {
            if (!c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }
}
