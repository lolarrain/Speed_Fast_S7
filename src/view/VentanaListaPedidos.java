package view;

import controller.ControladorPedidos;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana encargada de visualizar los pedidos registrados.
 *
 * Utiliza JTable y DefaultTableModel para representar
 * la información de manera tabular.
 */
public class VentanaListaPedidos extends JFrame {

    private final ControladorPedidos controlador;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    /**
     * Crea la ventana de listado y carga inmediatamente
     * los pedidos existentes.
     *
     * @param controlador controlador compartido del sistema
     */
    public VentanaListaPedidos(ControladorPedidos controlador) {
        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
        cargarPedidos();
    }

    private void configurarVentana() {
        setTitle("SpeedFast - Lista de Pedidos");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(10, 10)
        );

        JLabel lblTitulo = new JLabel(
                "Pedidos registrados",
                SwingConstants.CENTER
        );

        String[] columnas = {
                "ID",
                "Dirección",
                "Tipo",
                "Estado",
                "Repartidor"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollTabla =
                new JScrollPane(tablaPedidos);

        JButton btnRefrescar =
                new JButton("Refrescar");

        JButton btnCerrar =
                new JButton("Cerrar");

        JPanel panelBotones =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                scrollTabla,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        add(panelPrincipal);

        btnRefrescar.addActionListener(
                e -> cargarPedidos()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );
    }
    /**
     * Actualiza la tabla utilizando los pedidos
     * almacenados en el controlador.
     *
     * Primero elimina las filas existentes para evitar
     * duplicados y luego vuelve a cargar la información.
     */
    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        for (Pedido pedido : controlador.listarPedidos()) {

            String repartidor = "Sin asignar";

            if (pedido.getRepartidor() != null) {
                repartidor =
                        pedido.getRepartidor().getNombre();
            }

            modeloTabla.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getDireccion(),
                    pedido.getTipo(),
                    pedido.getEstado(),
                    repartidor
            });
        }
    }
}