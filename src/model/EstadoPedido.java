package model;

/**
 * Representa los posibles estados de un pedido durante
 * su ciclo de entrega.
 */
public enum EstadoPedido {
    PENDIENTE,
    ASIGNADO,
    EN_ENTREGA,
    ENTREGADO,
    CANCELADO
}
