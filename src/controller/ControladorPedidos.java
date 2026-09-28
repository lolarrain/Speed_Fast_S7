package controller;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;

import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.TipoPedido;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controlador principal para la gestión de pedidos de SpeedFast.
 *
 * Actúa como intermediario entre las vistas Swing,
 * los objetos del modelo y el acceso a la base de datos.
 *
 * Permite registrar pedidos y repartidores, consultar datos,
 * asignar repartidores e iniciar entregas.
 */
public class ControladorPedidos {

    private final List<Pedido> pedidos;
    private final List<Repartidor> repartidores;
    private final List<Runnable> listeners;

    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;

    private final ExecutorService executor;

    /**
     * Inicializa las colecciones, los objetos DAO
     * y el ExecutorService.
     *
     * Al iniciar el sistema, carga desde MySQL
     * los pedidos y repartidores registrados.
     */
    public ControladorPedidos() {

        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();
        listeners = new ArrayList<>();

        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();

        executor = Executors.newFixedThreadPool(3);

        cargarRepartidores();
        cargarPedidos();
    }

    /**
     * Carga desde la base de datos los repartidores
     * registrados en el sistema.
     */
    private void cargarRepartidores() {

        repartidores.addAll(
                repartidorDAO.listarTodos()
        );

        System.out.println(
                "[BD] Repartidores cargados: "
                        + repartidores.size()
        );
    }

    /**
     * Carga desde la base de datos los pedidos registrados.
     *
     * Cuando un pedido posee una entrega registrada,
     * también recupera el repartidor asociado.
     */
    private void cargarPedidos() {

        List<Pedido> pedidosBD =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidosBD) {

            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {

                Repartidor repartidor =
                        entregaDAO.buscarRepartidorPorPedido(
                                pedido.getId()
                        );

                if (repartidor != null) {
                    pedido.restaurarRepartidor(repartidor);
                }
            }

            pedidos.add(pedido);
        }

        System.out.println(
                "[BD] Pedidos cargados: "
                        + pedidos.size()
        );
    }

    /**
     * Registra una acción que será ejecutada
     * cuando cambie la información de los pedidos.
     *
     * @param listener acción que será ejecutada
     */
    public void agregarListener(Runnable listener) {
        listeners.add(listener);
    }

    /**
     * Notifica a las vistas registradas que la información
     * de los pedidos ha cambiado.
     *
     * La actualización se ejecuta en el hilo de eventos
     * de Swing para mantener segura la interfaz gráfica.
     */
    private void notificarCambios() {

        SwingUtilities.invokeLater(() -> {

            for (Runnable listener : listeners) {
                listener.run();
            }
        });
    }

    /**
     * Registra un nuevo pedido y lo almacena
     * en la base de datos.
     *
     * El identificador es generado automáticamente
     * por MySQL.
     *
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public void registrarPedido(
            String direccion,
            TipoPedido tipo
    ) {

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La dirección es obligatoria."
            );
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de pedido."
            );
        }

        Pedido pedidoTemporal = new Pedido(
                0,
                direccion.trim(),
                tipo
        );

        int idGenerado =
                pedidoDAO.guardar(pedidoTemporal);

        Pedido pedido = new Pedido(
                idGenerado,
                direccion.trim(),
                tipo
        );

        pedidos.add(pedido);

        System.out.println(
                "[BD] Pedido " + idGenerado
                        + " registrado correctamente."
        );

        notificarCambios();
    }

    /**
     * Registra un nuevo repartidor y lo almacena
     * en la base de datos.
     *
     * Después de guardar el repartidor, actualiza
     * la colección utilizada por la aplicación.
     *
     * @param nombre nombre del repartidor
     * @throws IllegalArgumentException si el nombre no es válido
     */
    public void registrarRepartidor(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del repartidor es obligatorio."
            );
        }

        /*
         * El ID es generado automáticamente por MySQL.
         */
        Repartidor repartidorTemporal =
                new Repartidor(
                        0,
                        nombre.trim()
                );

        repartidorDAO.guardar(repartidorTemporal);

        /*
         * Se vuelve a consultar la base de datos
         * para recuperar los repartidores con sus
         * identificadores reales.
         */
        repartidores.clear();

        repartidores.addAll(
                repartidorDAO.listarTodos()
        );

        System.out.println(
                "[BD] Repartidor "
                        + nombre.trim()
                        + " registrado correctamente."
        );
    }

    /**
     * Obtiene los pedidos disponibles en el sistema.
     *
     * @return copia de la lista de pedidos
     */
    public List<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos);
    }

    /**
     * Obtiene los repartidores disponibles en el sistema.
     *
     * @return copia de la lista de repartidores
     */
    public List<Repartidor> listarRepartidores() {
        return new ArrayList<>(repartidores);
    }

    /**
     * Busca un pedido según su identificador.
     *
     * @param id identificador del pedido
     * @return pedido encontrado o null si no existe
     */
    public Pedido buscarPedidoPorId(int id) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }

    /**
     * Asigna un repartidor a un pedido.
     *
     * Cambia el estado del pedido a ASIGNADO,
     * actualiza el estado en MySQL y registra
     * la relación entre pedido y repartidor.
     *
     * @param pedido pedido seleccionado
     * @param repartidor repartidor seleccionado
     * @throws IllegalArgumentException si no se selecciona
     *                                  pedido o repartidor
     */
    public void asignarRepartidor(
            Pedido pedido,
            Repartidor repartidor
    ) {

        if (pedido == null || repartidor == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar pedido y repartidor."
            );
        }

        pedido.asignarRepartidor(repartidor);

        pedidoDAO.actualizarEstado(pedido);

        Entrega entrega = new Entrega(
                0,
                pedido,
                repartidor,
                LocalDate.now(),
                LocalTime.now()
        );

        entregaDAO.guardar(entrega);

        System.out.println(
                "[ASIGNACIÓN] Pedido "
                        + pedido.getId()
                        + " asignado a "
                        + repartidor.getNombre()
        );

        System.out.println(
                "[BD] Estado del pedido "
                        + pedido.getId()
                        + ": "
                        + pedido.getEstado()
        );

        System.out.println(
                "[BD] Relación pedido-repartidor guardada."
        );

        notificarCambios();
    }

    /**
     * Inicia la entrega de un pedido previamente asignado.
     *
     * El estado cambia a EN_ENTREGA y se actualiza
     * inmediatamente en MySQL.
     *
     * La simulación se ejecuta mediante ExecutorService
     * para evitar bloquear la interfaz gráfica.
     * Después de tres segundos, el pedido cambia
     * a ENTREGADO y el estado final se almacena en MySQL.
     *
     * Las vistas registradas son notificadas cuando
     * cambia el estado del pedido.
     *
     * @param pedido pedido cuya entrega se iniciará
     * @throws IllegalArgumentException si no se selecciona un pedido
     * @throws IllegalStateException si el pedido no está asignado
     */
    public void iniciarEntrega(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un pedido."
            );
        }

        if (pedido.getEstado() != EstadoPedido.ASIGNADO) {
            throw new IllegalStateException(
                    "El pedido debe tener un repartidor asignado."
            );
        }

        pedido.iniciarEntrega();

        pedidoDAO.actualizarEstado(pedido);

        /*
         * Actualiza las vistas inmediatamente para mostrar
         * el estado EN_ENTREGA.
         */
        notificarCambios();

        System.out.println(
                "[ENTREGA] Pedido "
                        + pedido.getId()
                        + " inicia su entrega."
        );

        System.out.println(
                "[BD] Estado actualizado a "
                        + pedido.getEstado()
        );

        executor.submit(() -> {

            try {

                System.out.println(
                        "[ENTREGA] Pedido "
                                + pedido.getId()
                                + " en camino con "
                                + pedido.getRepartidor().getNombre()
                                + "..."
                );

                Thread.sleep(3000);

                pedido.completarEntrega();

                pedidoDAO.actualizarEstado(pedido);

                /*
                 * Actualiza nuevamente las vistas cuando
                 * el pedido cambia a ENTREGADO.
                 */
                notificarCambios();

                System.out.println(
                        "[ENTREGA] Pedido "
                                + pedido.getId()
                                + " entregado correctamente."
                );

                System.out.println(
                        "[BD] Estado final del pedido "
                                + pedido.getId()
                                + ": "
                                + pedido.getEstado()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.err.println(
                        "[ERROR] La entrega del pedido "
                                + pedido.getId()
                                + " fue interrumpida."
                );
            }
        });
    }
}