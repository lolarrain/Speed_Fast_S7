package model;
/**
 * Representa un pedido gestionado por SpeedFast.
 *
 * Un pedido posee un identificador, dirección, tipo,
 * estado y, opcionalmente, un repartidor asignado.
 *
 * La clase también controla las principales transiciones
 * de estado del pedido.
 */
public class Pedido {

    private final int id;
    private final String direccion;
    private final TipoPedido tipo;

    /**
     * volatile permite que los cambios realizados desde
     * distintos hilos sean visibles inmediatamente.
     */
    private volatile EstadoPedido estado;
    private Repartidor repartidor;

    /**
     * Crea un nuevo pedido.
     *
     * Todo pedido nuevo comienza automáticamente
     * en estado PENDIENTE.
     *
     * @param id identificador único del pedido
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     */
    public Pedido(int id, String direccion, TipoPedido tipo) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = EstadoPedido.PENDIENTE;
    }

    /**
     * Reconstruye un pedido a partir de los datos almacenados
     * en la base de datos.
     *
     * A diferencia del constructor principal, permite establecer
     * directamente el estado previamente guardado del pedido.
     *
     * @param id identificador único del pedido
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     * @param estado estado actual del pedido
     */
    public Pedido(
            int id,
            String direccion,
            TipoPedido tipo,
            EstadoPedido estado
    ) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }
    /**
     * Obtiene los detalles del pedido (ID, Dirección, Tipo,
     * Estado y Repartidor Asignado)
     * @return identificador del pedido
     */
    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }
    /**
     * Asigna un repartidor a un pedido pendiente.
     *
     * Al realizar la asignación, el estado cambia
     * automáticamente de PENDIENTE a ASIGNADO.
     *
     * @param repartidor repartidor que realizará la entrega
     * @throws IllegalStateException si el pedido no está pendiente
     */
    public void asignarRepartidor(Repartidor repartidor) {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden asignar pedidos pendientes."
            );
        }

        this.repartidor = repartidor;
        this.estado = EstadoPedido.ASIGNADO;
    }
    /**
     * Inicia la entrega de un pedido previamente asignado.
     *
     * Cambia el estado de ASIGNADO a EN_ENTREGA.
     *
     * @throws IllegalStateException si el pedido no está asignado
     */
    public void iniciarEntrega() {
        if (estado != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException(
                    "El pedido debe tener un repartidor asignado."
            );
        }

        estado = EstadoPedido.EN_ENTREGA;
    }
    /**
     * Finaliza una entrega que se encuentra en ejecución.
     *
     * Cambia el estado de EN_ENTREGA a ENTREGADO.
     *
     * @throws IllegalStateException si el pedido no está en entrega
     */
    public void completarEntrega() {
        if (estado != EstadoPedido.EN_ENTREGA) {
            throw new IllegalStateException(
                    "El pedido debe estar en entrega."
            );
        }

        estado = EstadoPedido.ENTREGADO;
    }

    /**
     * Restaura el repartidor asociado a un pedido
     * recuperado desde la base de datos.
     *
     * Este método no modifica el estado del pedido,
     * ya que dicho estado también se recupera desde MySQL.
     *
     * @param repartidor repartidor asociado al pedido
     */
    public void restaurarRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    /**
     * Devuelve una representación resumida del pedido.
     *
     * @return descripción del pedido
     */
    @Override
    public String toString() {
        return "Pedido " + id + " - " + direccion;
    }
}
