package view;

import controller.ControladorPedidos;
import model.TipoPedido;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana utilizada para registrar nuevos pedidos.
 *
 * Permite ingresar la dirección y el tipo de pedido.
 * Los datos son enviados al ControladorPedidos para
 * su validación y almacenamiento en la base de datos.
 */
public class VentanaRegistroPedido extends JFrame {

    private final ControladorPedidos controlador;

    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cmbTipo;

    /**
     * Crea la ventana de registro de pedidos.
     *
     * @param controlador controlador principal del sistema
     */
    public VentanaRegistroPedido(ControladorPedidos controlador) {
        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Configura las características principales
     * de la ventana.
     */
    private void configurarVentana() {
        setTitle("SpeedFast - Registrar Pedido");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Inicializa los componentes gráficos
     * utilizados para registrar un pedido.
     */
    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblTipo = new JLabel("Tipo:");

        txtDireccion = new JTextField();

        cmbTipo = new JComboBox<>(TipoPedido.values());

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelFormulario.add(lblDireccion);
        panelFormulario.add(txtDireccion);

        panelFormulario.add(lblTipo);
        panelFormulario.add(cmbTipo);

        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnCancelar);

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        add(panelFormulario);

        btnGuardar.addActionListener(e -> guardarPedido());

        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Obtiene y valida los datos ingresados en el formulario.
     *
     * Si los datos son correctos, solicita al controlador
     * registrar y almacenar el nuevo pedido.
     */
    private void guardarPedido() {

        try {

            String direccion = txtDireccion.getText().trim();

            TipoPedido tipo =
                    (TipoPedido) cmbTipo.getSelectedItem();

            controlador.registrarPedido(
                    direccion,
                    tipo
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Limpia los campos después de registrar un pedido
     * y devuelve el foco al campo de dirección.
     */
    private void limpiarFormulario() {
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);

        txtDireccion.requestFocus();
    }
}
