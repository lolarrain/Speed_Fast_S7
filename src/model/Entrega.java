package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrega {

    private final int id;
    private final Pedido pedido;
    private final Repartidor repartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    public Entrega(
            int id,
            Pedido pedido,
            Repartidor repartidor,
            LocalDate fecha,
            LocalTime hora) {

        this.id = id;
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}