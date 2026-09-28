package view;

import controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana utilizada para registrar nuevos repartidores.
 *
 * Permite ingresar el nombre de un repartidor
 * y almacenarlo en la base de datos mediante
 * el controlador principal.
 */
public class VentanaRegistroRepartidor extends JFrame {

    private final ControladorPedidos controlador;

    private JTextField txtNombre;

    /**
     * Crea la ventana de registro de repartidores.
     *
     * @param controlador controlador principal del sistema
     */
    public VentanaRegistroRepartidor(
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

        setTitle("SpeedFast - Registrar Repartidor");
        setSize(400, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Inicializa los componentes gráficos utilizados
     * para registrar un repartidor.
     */
    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(2, 2, 10, 10)
                );

        JLabel lblNombre =
                new JLabel("Nombre:");

        txtNombre =
                new JTextField();

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnCancelar =
                new JButton("Cancelar");

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);

        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnCancelar);

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        add(panelFormulario);

        btnGuardar.addActionListener(
                e -> guardarRepartidor()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    /**
     * Obtiene el nombre ingresado y solicita
     * al controlador registrar el repartidor.
     *
     * Si ocurre un error de validación o de base
     * de datos, muestra el mensaje correspondiente.
     */
    private void guardarRepartidor() {

        try {

            String nombre =
                    txtNombre.getText().trim();

            controlador.registrarRepartidor(
                    nombre
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente.",
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
     * Limpia el formulario después de registrar
     * correctamente un repartidor.
     */
    private void limpiarFormulario() {

        txtNombre.setText("");

        txtNombre.requestFocus();
    }
}