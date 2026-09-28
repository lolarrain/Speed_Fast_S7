# SpeedFast S6

Proyecto desarrollado en Java que representa un sistema de gestión de pedidos para una empresa de reparto a domicilio.

Esta versión incorpora una interfaz gráfica de escritorio desarrollada con Java Swing, permitiendo registrar pedidos, visualizar los pedidos existentes, asignar repartidores e iniciar entregas desde distintas ventanas conectadas a un controlador común.

Además, se mantiene la simulación concurrente de entregas mediante `ExecutorService`, evitando bloquear la interfaz gráfica mientras los pedidos son procesados.

## Conceptos aplicados

- Programación orientada a objetos.
- Encapsulamiento.
- Enumeraciones (`enum`).
- Arquitectura Modelo-Vista-Controlador (MVC).
- Java Swing.
- Colecciones genéricas.
- Manejo de excepciones.
- Programación concurrente.
- `ExecutorService`.
- Expresiones lambda.
- `volatile`.

## Funcionamiento

La clase `Pedido` representa los pedidos registrados en el sistema y administra su ciclo de estados:

`PENDIENTE - ASIGNADO - EN_ENTREGA - ENTREGADO`

Cada pedido contiene un ID, dirección, tipo, estado y un repartidor opcionalmente asignado.

Los tipos de pedido disponibles se definen mediante el enum `TipoPedido`:

- `COMIDA`
- `ENCOMIENDA`
- `EXPRESS`

La clase `ControladorPedidos` centraliza la gestión de los pedidos y repartidores. Permite registrar, buscar y listar pedidos, asignar repartidores e iniciar entregas.

La aplicación incluye datos precargados para facilitar las pruebas de pedidos en distintos estados.

## Interfaz gráfica

La aplicación se inicia desde `Main`, que crea una única instancia de `ControladorPedidos` y la comparte entre las diferentes vistas.

`VentanaPrincipal` permite acceder a tres módulos:

- Registrar pedido.
- Listar pedidos.
- Asignar repartidor / Iniciar entrega.

`VentanaRegistroPedido` permite ingresar un nuevo pedido mediante campos de ID, dirección y tipo, incluyendo validación de datos y mensajes mediante `JOptionPane`.

`VentanaListaPedidos` utiliza `JTable` y `DefaultTableModel` para visualizar y refrescar los pedidos registrados.

`VentanaAsignacionEntrega` permite seleccionar un pedido y un repartidor, realizar la asignación e iniciar la simulación de entrega.

## Concurrencia

Las entregas se ejecutan mediante un `ExecutorService` con un pool fijo de hilos.

Al iniciar una entrega:

1. El pedido cambia a `EN_ENTREGA`.
2. La simulación se ejecuta en un hilo independiente.
3. Después del tiempo simulado, el pedido cambia a `ENTREGADO`.

Esto permite que la interfaz Swing continúe respondiendo mientras se procesan las entregas.

## Estructura

```text
src/main/java
├── controller
│   └── ControladorPedidos.java
├── main
│   └── Main.java
├── model
│   ├── EstadoPedido.java
│   ├── Pedido.java
│   ├── Repartidor.java
│   └── TipoPedido.java
└── view
    ├── VentanaAsignacionEntrega.java
    ├── VentanaListaPedidos.java
    ├── VentanaPrincipal.java
    └── VentanaRegistroPedido.java