package dao;

import model.Entrega;
import model.Repartidor;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EntregaDAO {

    public void guardar(Entrega entrega) {

        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entrega.getPedido().getId()
            );

            statement.setInt(
                    2,
                    entrega.getRepartidor().getId()
            );

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(entrega.getFecha())
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(entrega.getHora())
            );

            statement.executeUpdate();

            System.out.println("Entrega guardada correctamente.");

        } catch (SQLException e) {

            System.err.println(
                    "Error al guardar entrega: " + e.getMessage()
            );
        }
    }

    /**
     * Busca el repartidor asociado a un pedido
     * registrado en la tabla entrega.
     *
     * @param idPedido identificador del pedido
     * @return repartidor asociado o null si no existe
     */
    public Repartidor buscarRepartidorPorPedido(int idPedido) {

        String sql = """
            SELECT r.id, r.nombre
            FROM entrega e
            INNER JOIN repartidor r
                ON e.id_repartidor = r.id
            WHERE e.id_pedido = ?
            ORDER BY e.id DESC
            LIMIT 1
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, idPedido);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {

                    int id = resultado.getInt("id");
                    String nombre = resultado.getString("nombre");

                    return new Repartidor(
                            id,
                            nombre
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al recuperar el repartidor del pedido: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }

}
