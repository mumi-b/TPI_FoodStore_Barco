package integrado.prog2.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase abstracta de la que heredan todas las entidades del sistema.
 * Aporta los atributos comunes (id, eliminado, createdAt), la generacion
 * automatica del id y obliga a las hijas a implementar toString().
 *
 * @author Eugenia
 */
public abstract class Base {

    // Contador compartido por TODAS las instancias (static): genera ids unicos.
    private static Long contador = 0L;

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        contador++;                       // suma 1 cada vez que se crea un objeto
        this.id = contador;               // se asigna ese numero como id unico
        this.eliminado = false;           // por defecto NO esta eliminado (soft delete)
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Dos entidades son "iguales" si son de la misma clase y tienen el mismo id.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Base otra = (Base) obj;
        return Objects.equals(this.id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Cada clase hija esta obligada a definir su propio toString().
    @Override
    public abstract String toString();
}
