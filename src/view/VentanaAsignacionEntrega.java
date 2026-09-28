package view;

import controller.ControladorPedidos;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana encargada de asignar repartidores
 * e iniciar entregas.
 *
 * Permite seleccionar un pedido y un repartidor,
 * consultar su estado actual e iniciar la simulación.
 *
 * La información se actualiza automáticamente
 * cuando cambia el estado de los pedidos.
 */
public class VentanaAsignacionEntrega extends JFrame {

    private final ControladorPedidos controlador;

    private JComboBox<Pedido> cmbPedidos;
    private JComboBox<Repartidor> cmbRepartidores;

    private JLabel lblEstado;
    private JLabel lblRepartidor;

    /**
     * Crea la ventana de gestión de entregas
     * y la registra para recibir cambios
     * desde el controlador.
     *
     * @param controlador controlador compartido del sistema
     */
    public VentanaAsignacionEntrega(
            ControladorPedidos controlador
    ) {

        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();

        controlador.agregarListener(
                this::cargarDatos
        );

        cargarDatos();
    }

    /**
     * Configura las características principales
     * de la ventana.
     */
    private void configurarVentana() {

        setTitle("SpeedFast - Gestión de Entregas");

        setSize(500, 350);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);
    }

    /**
     * Inicializa los componentes utilizados
     * para gestionar las entregas.
     */
    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel lblTitulo = new JLabel(
                "Asignación e inicio de entregas",
                SwingConstants.CENTER
        );

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        cmbPedidos = new JComboBox<>();

        cmbRepartidores = new JComboBox<>();

        lblEstado = new JLabel("-");

        lblRepartidor = new JLabel("-");

        panelFormulario.add(
                new JLabel("Pedido:")
        );

        panelFormulario.add(cmbPedidos);

        panelFormulario.add(
                new JLabel("Repartidor:")
        );

        panelFormulario.add(cmbRepartidores);

        panelFormulario.add(
                new JLabel("Estado:")
        );

        panelFormulario.add(lblEstado);

        panelFormulario.add(
                new JLabel("Asignado a:")
        );

        panelFormulario.add(lblRepartidor);

        JButton btnAsignar =
                new JButton(
                        "Asignar repartidor"
                );

        JButton btnIniciar =
                new JButton(
                        "Iniciar entrega"
                );

        JButton btnCerrar =
                new JButton(
                        "Cerrar"
                );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout()
                );

        panelBotones.add(btnAsignar);
        panelBotones.add(btnIniciar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        add(panelPrincipal);

        cmbPedidos.addActionListener(
                e -> actualizarInformacionPedido()
        );

        btnAsignar.addActionListener(
                e -> asignarRepartidor()
        );

        btnIniciar.addActionListener(
                e -> iniciarEntrega()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );
    }

    /**
     * Carga en los JComboBox los pedidos
     * y repartidores disponibles.
     *
     * También conserva seleccionado el pedido
     * que estaba activo antes de la actualización.
     */
    private void cargarDatos() {

        Pedido pedidoSeleccionado =
                (Pedido) cmbPedidos.getSelectedItem();

        cmbPedidos.removeAllItems();

        for (Pedido pedido :
                controlador.listarPedidos()) {

            cmbPedidos.addItem(pedido);
        }

        cmbRepartidores.removeAllItems();

        for (Repartidor repartidor :
                controlador.listarRepartidores()) {

            cmbRepartidores.addItem(repartidor);
        }

        if (pedidoSeleccionado != null) {

            for (int i = 0;
                 i < cmbPedidos.getItemCount();
                 i++) {

                Pedido pedido =
                        cmbPedidos.getItemAt(i);

                if (pedido.getId()
                        == pedidoSeleccionado.getId()) {

                    cmbPedidos.setSelectedIndex(i);
                    break;
                }
            }
        }

        actualizarInformacionPedido();
    }

    /**
     * Actualiza la información visual correspondiente
     * al pedido seleccionado.
     */
    private void actualizarInformacionPedido() {

        Pedido pedido =
                (Pedido) cmbPedidos.getSelectedItem();

        if (pedido == null) {

            lblEstado.setText("-");
            lblRepartidor.setText("-");

            return;
        }

        lblEstado.setText(
                pedido.getEstado().toString()
        );

        if (pedido.getRepartidor() == null) {

            lblRepartidor.setText(
                    "Sin asignar"
            );

        } else {

            lblRepartidor.setText(
                    pedido
                            .getRepartidor()
                            .getNombre()
            );
        }
    }

    /**
     * Asigna el repartidor seleccionado
     * al pedido activo.
     *
     * Los errores de validación son mostrados
     * mediante JOptionPane.
     */
    private void asignarRepartidor() {

        try {

            Pedido pedido =
                    (Pedido) cmbPedidos
                            .getSelectedItem();

            Repartidor repartidor =
                    (Repartidor) cmbRepartidores
                            .getSelectedItem();

            controlador.asignarRepartidor(
                    pedido,
                    repartidor
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor asignado correctamente."
            );

        } catch (
                IllegalArgumentException |
                IllegalStateException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "No se pudo asignar",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Solicita al controlador iniciar la entrega
     * del pedido seleccionado.
     *
     * La ejecución posterior se realiza de forma
     * concurrente mediante ExecutorService.
     */
    private void iniciarEntrega() {

        try {

            Pedido pedido =
                    (Pedido) cmbPedidos
                            .getSelectedItem();

            controlador.iniciarEntrega(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega iniciada."
            );

        } catch (
                IllegalArgumentException |
                IllegalStateException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "No se pudo iniciar la entrega",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
