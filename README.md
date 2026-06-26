# Food Store - Trabajo Práctico Integrador (Programación 2)

Sistema de consola para la gestión de pedidos de comida (Food Store), desarrollado
en Java aplicando Programación Orientada a Objetos. Permite administrar **categorías,
productos, usuarios y pedidos** mediante un menú interactivo con operaciones CRUD
completas. Toda la información se almacena **en memoria** usando Colecciones (no usa
base de datos).

Estudiante: Eugenia Abril Barco
Materia:Programación 2 - Tecnicatura Universitaria en Programación a Distancia (UTN)

---

  Entregables

-  Video demostrativo (en 5 partes cortas):
 1. Sistema Foodstore en consola con Java https://www.loom.com/share/a6209edec40647b281d63b54841017a5
  2. Arquitectura por capas y herencia en Java https://www.loom.com/share/4037226db33a44159a42e75c448acb25
  3. Encapsulamiento y excepciones en entidades https://www.loom.com/share/c4136ff46cae462080ae9c390901a939
  4. Comprobacion de funcionamiento correcto https://www.loom.com/share/b0ac0bdb4ed14d7fb3a3fff3e209253f
  5. EXCEPCIONES: https://www.loom.com/share/77d9137689e14abe8d1225034f32dffe

- Documentación (PDF):** ver el archivo [`Documentacion_TPI.pdf`](Documentacion_TPI.pdf) en la raíz de este repositorio.
- Código fuente completo:** en la carpeta [`src/`](src/) de este repositorio.

---

Cómo ejecutar el proyecto desde NetBeans 
1. Abrir NetBeans.
2. File > Open Project y seleccionar la carpeta TPI_FoodStore.
3. Click derecho en el proyecto > Run (o tecla F6).

 Desde la terminal
    bash
Posicionarse en la carpeta del proyecto

    cd TPI_FoodStore

Compilar 
    javac -d build/classes $(find src -name "*.java")

Ejecutar
java -cp build/classes integrado.prog2.Main


El proyecto está pensado para Java 21

---

Cómo se usa

Al iniciar, el sistema carga unos datos de ejemplo y muestra el menú principal:

 SISTEMA DE PEDIDOS (FOOD STORE) 
1. Categorias
2. Productos
3. Usuarios
4. Pedidos
0. Salir
Seleccione:

Cada opción abre un submenú con las operaciones: 
**Listar / Crear / Editar / Eliminar** (CRUD)
Para crear un producto o un pedido, el sistema lista primero las entidades disponibles
para que sepas qué id elegir.

---

Arquitectura (paquetes)


src/integrado/prog2/
├── Main.java                 Es el punto de entrada: arma servicios y menús, muestra el menú principal
├── entities/                 Modelo de dominio (clases del UML)
│   ├── Base.java             Esta es la clase abstracta: id, eliminado, createdAt, equals, toString abstracto
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java           implementa Calculable
│   └── DetallePedido.java
├── enums                      Rol, Estado, FormaPago
├── interfaces
│   └── Calculable.java       Contrato: calcularTotal()
├── exception                Excepciones propias
│   ├── EntidadNoEncontradaException.java
│   ├── PrecioInvalidoException.java
│   ├── StockInvalidoException.java
│   ├── DatoInvalidoException.java
│   └── MailDuplicadoException.java
├── services/               Lógica de negocio + colecciones en memoria
│   ├── CategoriaService.java
│   ├── ProductoService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
└── ui/                     Interacción por consola (Scanner)
    ├── ConsolaUtil.java    Lectura segura de datos
    ├── MenuCategoria.java
    ├── MenuProducto.java
    ├── MenuUsuario.java
    └── MenuPedido.java

Separación de responsabilidades en 3 capas:
- Entities: representan el modelo, con sus atributos y `toString()`.
- Services: contienen la lógica de negocio, las validaciones y las colecciones.
- UI (Menús): solo leen datos del usuario y muestran resultados; delegan todo en los 'services'.

---

Conceptos de POO aplicados

 Concepto y dónde se ve 

Herencia : Todas las entidades heredan de la clase abstracta Base. 
Clase abstracta: Base no se puede instanciar; obliga a implementar un toString(). 
Interfaces:Calculable define calcularTotal(), implementado por Pedido. 
Encapsulamiento: Atributos private con getters/setters. 
Polimorfismo:Cada entidad sobreescribe `toString()`; `equals()` redefinido en Base 
Colecciones:List<>/ArrayList<> en los services para guardar entidades en memoria. 
Manejo de excepciones: Excepciones propias lanzadas en services y capturadas (try/catch) en los menús. 

---

Reglas de negocio implementadas

- No se permite crear Producto con precio < 0 o stock < 0 (lanza StockInvalidoException`).
- No se permite crear Pedido sin usuario, ni confirmarlo sin detalles.
- La cantidad de un detalle debe ser > 0 (DatoInvalidoException).
- El mail de Usuario debe ser único (MailDuplicadoException).
- El nombre de Categoría debe ser único.
- Baja lógica (soft delete): eliminar marca eliminado = true, para que no se borre el objeto de la colección (se conserva el historial).
- Stock: al cargar un detalle se descuenta el stock; si no alcanza, se lanza excepción y **se cancela el pedido completo** reponiendo el stock (no quedan datos inconsistentes).
- El total del pedido se calcula obligatoriamente con calcularTotal() (interfaz `Calculable`).
