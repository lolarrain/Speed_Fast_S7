package view;

import controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de SpeedFast.
 *
 * Permite acceder a las principales funciones del sistema:
 * registrar pedidos y repartidores, listar pedidos y
 * gestionar entregas.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorPedidos controlador;

    /**
     * Crea la ventana principal utilizando el controlador
     * compartido por toda la aplicación.
     *
     * @param controlador controlador de pedidos
     */
    public VentanaPrincipal(
            ControladorPedidos controlador
    ) {

        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Configura las características principales
     * de la ventana.
     */
    private void configurarVentana() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(450, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Inicializa los componentes gráficos y configura
     * el acceso a las distintas funciones del sistema.
     */
    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel lblTitulo =
                new JLabel(
                        "Sistema de Gestión de Entregas",
                        SwingConstants.CENTER
                );

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(4, 1, 10, 10)
                );

        JButton btnRegistrarPedido =
                new JButton(
                        "Registrar pedido"
                );

        JButton btnRegistrarRepartidor =
                new JButton(
                        "Registrar repartidor"
                );

        JButton btnListar =
                new JButton(
                        "Listar pedidos"
                );

        JButton btnEntrega =
                new JButton(
                        "Asignar repartidor / Iniciar entrega"
                );


        panelBotones.add(
                btnRegistrarPedido
        );

        panelBotones.add(
                btnRegistrarRepartidor
        );

        panelBotones.add(
                btnListar
        );

        panelBotones.add(
                btnEntrega
        );


        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.CENTER
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

        btnRegistrarPedido.addActionListener(e ->
                new VentanaRegistroPedido(
                        controlador
                ).setVisible(true)
        );

        btnRegistrarRepartidor.addActionListener(e ->
                new VentanaRegistroRepartidor(
                        controlador
                ).setVisible(true)
        );

        btnListar.addActionListener(e ->
                new VentanaListaPedidos(
                        controlador
                ).setVisible(true)
        );

        btnEntrega.addActionListener(e ->
                new VentanaAsignacionEntrega(
                        controlador
                ).setVisible(true)
        );

    }
}