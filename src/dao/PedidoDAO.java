package dao;

import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    /**
     * Guarda un pedido en la base de datos.
     *
     * @param pedido pedido que se desea guardar
     */
    /**
     * Guarda un pedido en la base de datos y obtiene
     * el identificador generado automáticamente por MySQL.
     *
     * @param pedido pedido que se desea guardar
     * @return identificador generado por la base de datos
     */
    public int guardar(Pedido pedido) {

        String sql = """
            INSERT INTO pedido (direccion, tipo, estado)
            VALUES (?, ?, ?)
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, pedido.getDireccion());
            statement.setString(2, pedido.getTipo().name());
            statement.setString(3, pedido.getEstado().name());

            statement.executeUpdate();

            try (ResultSet clavesGeneradas = statement.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {
                    return clavesGeneradas.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al guardar pedido: " + e.getMessage(),
                    e
            );
        }

        throw new RuntimeException(
                "No se pudo obtener el ID generado."
        );
    }

    /**
     * Obtiene todos los pedidos almacenados
     * en la base de datos.
     *
     * @return lista de pedidos registrados
     */
    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
                SELECT id, direccion, tipo, estado
                FROM pedido
                """;


        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String direccion = resultado.getString("direccion");

                TipoPedido tipo = TipoPedido.valueOf(
                        resultado.getString("tipo")
                );

                EstadoPedido estado = EstadoPedido.valueOf(
                        resultado.getString("estado")
                );

                Pedido pedido = new Pedido(
                        id,
                        direccion,
                        tipo,
                        estado
                );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar pedidos: " + e.getMessage()
            );
        }

        return pedidos;
    }
    /**
     * Actualiza en la base de datos el estado de un pedido.
     *
     * @param pedido pedido cuyo estado se desea actualizar
     */
    public void actualizarEstado(Pedido pedido) {

        String sql = """
            UPDATE pedido
            SET estado = ?
            WHERE id = ?
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    pedido.getEstado().name()
            );

            statement.setInt(
                    2,
                    pedido.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al actualizar el estado del pedido: "
                            + e.getMessage(),
                    e
            );
        }
    }
    /**
     * Elimina un pedido de la base de datos.
     *
     * Primero elimina los registros asociados en la tabla entrega
     * y posteriormente elimina el pedido.
     *
     * Ambas operaciones se realizan dentro de una transacción.
     * Si alguna operación falla, se revierten todos los cambios.
     *
     * @param idPedido identificador del pedido que se desea eliminar
     */
    public void eliminar(int idPedido) {

        String sqlEliminarEntrega =
                "DELETE FROM entrega WHERE id_pedido = ?";

        String sqlEliminarPedido =
                "DELETE FROM pedido WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = ConexionBD.conectar();

            /*
             * Desactiva temporalmente el guardado automático
             * para controlar manualmente la transacción.
             */
            conexion.setAutoCommit(false);

            /*
             * Primero elimina las relaciones del pedido
             * almacenadas en la tabla entrega.
             */
            try (PreparedStatement statementEntrega =
                         conexion.prepareStatement(sqlEliminarEntrega)) {

                statementEntrega.setInt(1, idPedido);
                statementEntrega.executeUpdate();
            }

            /*
             * Luego elimina el pedido.
             */
            int filasAfectadas;

            try (PreparedStatement statementPedido =
                         conexion.prepareStatement(sqlEliminarPedido)) {

                statementPedido.setInt(1, idPedido);

                filasAfectadas =
                        statementPedido.executeUpdate();
            }

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se encontró el pedido."
                );
            }

            /*
             * Confirma ambas operaciones.
             */
            conexion.commit();

        } catch (SQLException e) {

            /*
             * Si ocurre un error, revierte la transacción.
             */
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException rollbackException) {
                    System.err.println(
                            "[ERROR] No se pudo revertir la transacción: "
                                    + rollbackException.getMessage()
                    );
                }
            }

            throw new RuntimeException(
                    "Error al eliminar el pedido: "
                            + e.getMessage(),
                    e
            );

        } finally {

            /*
             * Cierra la conexión utilizada por la transacción.
             */
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(
                            "[ERROR] No se pudo cerrar la conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }
}
