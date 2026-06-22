# Food Store - Trabajo Práctico Integrador (Programación 2)

Sistema de consola para la gestión de pedidos de comida (Food Store), desarrollado
en Java aplicando Programación Orientada a Objetos. Permite administrar **categorías,
productos, usuarios y pedidos** mediante un menú interactivo con operaciones CRUD
completas. Toda la información se almacena **en memoria** usando Colecciones (no usa
base de datos).

**Autora:** Eugenia Abril Barco
**Materia:** Programación 2 - Tecnicatura Universitaria en Programación a Distancia (UTN)

---

## Cómo ejecutar el proyecto

### Opción A — Desde NetBeans (recomendada)
1. Abrir NetBeans.
2. `File > Open Project` y seleccionar la carpeta `TPI_FoodStore`.
3. Click derecho en el proyecto > **Run** (o tecla **F6**).

### Opción B — Desde la terminal
```bash
# Posicionarse en la carpeta del proyecto
cd TPI_FoodStore

# Compilar (genera los .class en /tmp/out o la carpeta que elijas)
javac -d build/classes $(find src -name "*.java")

# Ejecutar
java -cp build/classes integrado.prog2.Main
```

> Requiere **JDK 11 o superior** (el proyecto está pensado para Java 21).

---

## Cómo se usa

Al iniciar, el sistema carga unos datos de ejemplo y muestra el menú principal:

```
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Categorias
2. Productos
3. Usuarios
4. Pedidos
0. Salir
Seleccione:
```

Cada opción abre un submenú con las operaciones **Listar / Crear / Editar / Eliminar**.
Para crear un producto o un pedido, el sistema lista primero las entidades disponibles
para que sepas qué `id` elegir.

---

## Arquitectura (paquetes)

```
src/integrado/prog2/
├── Main.java              # Punto de entrada: arma servicios y menús, muestra el menú principal
├── entities/              # Modelo de dominio (clases del UML)
│   ├── Base.java          # Clase abstracta: id, eliminado, createdAt, equals, toString abstracto
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java        # implements Calculable
│   └── DetallePedido.java
├── enums/                 # Rol, Estado, FormaPago
├── interfaces/
│   └── Calculable.java    # Contrato: calcularTotal()
├── exception/             # Excepciones propias
│   ├── EntidadNoEncontradaException.java
│   ├── StockInvalidoException.java
│   ├── DatoInvalidoException.java
│   └── MailDuplicadoException.java
├── services/              # Lógica de negocio + colecciones en memoria
│   ├── CategoriaService.java
│   ├── ProductoService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
└── ui/                    # Interacción por consola (Scanner)
    ├── ConsolaUtil.java   # Lectura segura de datos
    ├── MenuCategoria.java
    ├── MenuProducto.java
    ├── MenuUsuario.java
    └── MenuPedido.java
```

**Separación de responsabilidades (3 capas):**
- **Entities:** representan el modelo, con sus atributos y `toString()`.
- **Services:** contienen la lógica de negocio, las validaciones y las colecciones.
- **UI (Menús):** solo leen datos del usuario y muestran resultados; delegan todo en los services.

---

## Conceptos de POO aplicados

| Concepto | Dónde se ve |
|----------|-------------|
| **Herencia** | Todas las entidades heredan de la clase abstracta `Base`. |
| **Clase abstracta** | `Base` no se puede instanciar; obliga a implementar `toString()`. |
| **Interfaces** | `Calculable` define `calcularTotal()`, implementado por `Pedido`. |
| **Encapsulamiento** | Atributos `private` con getters/setters. |
| **Polimorfismo** | Cada entidad sobreescribe `toString()`; `equals()` redefinido en `Base`. |
| **Colecciones** | `List<>`/`ArrayList<>` en los services para guardar entidades en memoria. |
| **Manejo de excepciones** | Excepciones propias lanzadas en services y capturadas (try/catch) en los menús. |

---

## Reglas de negocio implementadas

- No se permite crear Producto con **precio < 0** o **stock < 0** (lanza `StockInvalidoException`).
- No se permite crear Pedido sin usuario, ni confirmarlo sin detalles.
- La **cantidad** de un detalle debe ser **> 0** (`DatoInvalidoException`).
- El **mail** de Usuario debe ser **único** (`MailDuplicadoException`).
- El **nombre** de Categoría debe ser único.
- **Baja lógica (soft delete):** eliminar marca `eliminado = true`, no borra el objeto de la colección (se conserva el historial).
- **Stock:** al cargar un detalle se descuenta el stock; si no alcanza, se lanza excepción y **se cancela el pedido completo** reponiendo el stock (no quedan datos inconsistentes).
- El **total** del pedido se calcula obligatoriamente con `calcularTotal()` (interfaz `Calculable`).
