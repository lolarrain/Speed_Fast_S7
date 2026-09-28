package dao;

import model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public void guardar(Repartidor repartidor) {

        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, repartidor.getNombre());

            statement.executeUpdate();

            System.out.println("Repartidor guardado correctamente.");

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar repartidor: " + e.getMessage()
            );
        }
    }

    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");

                Repartidor repartidor = new Repartidor(id, nombre);

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar repartidores: " + e.getMessage()
            );
        }

        return repartidores;
    }

    public static void main(String[] args) {

        RepartidorDAO repartidorDAO = new RepartidorDAO();

        repartidorDAO.guardar(
                new Repartidor(0, "Carlos")
        );

        repartidorDAO.guardar(
                new Repartidor(0, "María")
        );

        repartidorDAO.guardar(
                new Repartidor(0, "Pedro")
        );

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {

            System.out.println(
                    repartidor.getId()
                            + " - "
                            + repartidor.getNombre()
            );
        }
    }

}
