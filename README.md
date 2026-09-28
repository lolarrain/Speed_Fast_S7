# SpeedFast S7

Proyecto desarrollado en Java que representa un sistema de gestión de pedidos para una empresa de reparto a domicilio.

Esta versión incorpora persistencia de datos mediante JDBC y MySQL, permitiendo registrar y consultar información directamente desde una base de datos.

Se mantiene la interfaz gráfica desarrollada con Java Swing y la simulación concurrente de entregas mediante `ExecutorService`, evitando bloquear la interfaz gráfica mientras los pedidos son procesados.

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
- JDBC.
- MySQL.
- Patrón DAO.
- `DriverManager`.
- `PreparedStatement`.
- `ResultSet`.
- Persistencia de datos.

## Funcionamiento

La clase `Pedido` representa los pedidos registrados en el sistema y administra su ciclo de estados:

`PENDIENTE - ASIGNADO - EN_ENTREGA - ENTREGADO`

Cada pedido contiene un ID, dirección, tipo, estado y un repartidor opcionalmente asignado.

Los tipos de pedido disponibles se definen mediante el enum `TipoPedido`:

- `COMIDA`
- `ENCOMIENDA`
- `EXPRESS`

La clase `ControladorPedidos` centraliza la gestión de pedidos y repartidores y coordina la comunicación entre la interfaz gráfica y la capa de acceso a datos.

Las clases `PedidoDAO`, `RepartidorDAO` y `EntregaDAO` permiten registrar y consultar información almacenada en MySQL.

La clase `ConexionBD` gestiona la conexión con la base de datos `speedfast_db` mediante JDBC.

## Interfaz gráfica

La aplicación se inicia desde `Main`, que crea una única instancia de `ControladorPedidos` y la comparte entre las diferentes vistas.

`VentanaPrincipal` permite acceder a cuatro módulos:

- Registrar pedido.
- Registrar repartidor.
- Listar pedidos.
- Asignar repartidor / Iniciar entrega.

`VentanaRegistroPedido` permite ingresar nuevos pedidos y almacenarlos en la base de datos.

`VentanaRegistroRepartidor` permite registrar nuevos repartidores en la base de datos.

`VentanaListaPedidos` utiliza `JTable` y `DefaultTableModel` para visualizar los pedidos almacenados.

`VentanaAsignacionEntrega` permite seleccionar un pedido y un repartidor, realizar la asignación e iniciar la simulación de entrega.

## Concurrencia

Las entregas se ejecutan mediante un `ExecutorService` con un pool fijo de hilos.

Al iniciar una entrega:

1. El pedido cambia a `EN_ENTREGA`.
2. La simulación se ejecuta en un hilo independiente.
3. Después del tiempo simulado, el pedido cambia a `ENTREGADO`.
4. El nuevo estado se actualiza en la base de datos.

Esto permite que la interfaz Swing continúe respondiendo mientras se procesan las entregas.

## Persistencia de datos

La aplicación utiliza JDBC para conectarse a una base de datos MySQL.

La base de datos `speedfast_db` contiene las tablas:

- `pedido`
- `repartidor`
- `entrega`

La capa DAO utiliza `PreparedStatement` para realizar operaciones sobre la base de datos y `ResultSet` para recuperar información almacenada.

Las credenciales de conexión se almacenan localmente en un archivo `.env`, excluido del repositorio mediante `.gitignore`.

El archivo `.env.example` proporciona la estructura necesaria para configurar la conexión:

```properties
DB_URL=jdbc:mysql://localhost:3306/speedfast_db
DB_USER=root
DB_PASSWORD=TU_CONTRASEÑA
```

## Estructura

```text
src
├── controller
│   └── ControladorPedidos.java
├── dao
│   ├── ConexionBD.java
│   ├── EntregaDAO.java
│   ├── PedidoDAO.java
│   └── RepartidorDAO.java
├── main
│   └── Main.java
├── model
│   ├── Entrega.java
│   ├── EstadoPedido.java
│   ├── Pedido.java
│   ├── Repartidor.java
│   └── TipoPedido.java
└── view
    ├── VentanaAsignacionEntrega.java
    ├── VentanaListaPedidos.java
    ├── VentanaPrincipal.java
    ├── VentanaRegistroPedido.java
    └── VentanaRegistroRepartidor.java
```
