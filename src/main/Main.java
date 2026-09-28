package main;

import controller.ControladorPedidos;
import view.VentanaPrincipal;

import javax.swing.*;
/**
 * Punto de entrada de la aplicación SpeedFast.
 *
 * Inicializa el controlador compartido y abre
 * la ventana principal mediante Swing.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ControladorPedidos controlador =
                    new ControladorPedidos();

            VentanaPrincipal ventana =
                    new VentanaPrincipal(controlador);

            ventana.setVisible(true);
        });
    }
}