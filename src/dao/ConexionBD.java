package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión con la base de datos MySQL
 * utilizada por SpeedFast.
 *
 * Los datos de conexión se obtienen desde un archivo .env,
 * evitando almacenar credenciales directamente
 * en el código fuente.
 */
public class ConexionBD {

    private static final Properties CONFIG =
            cargarConfiguracion();

    /**
     * Carga los datos de conexión almacenados
     * en el archivo .env ubicado en la raíz del proyecto.
     *
     * @return propiedades con la configuración de la base de datos
     * @throws RuntimeException si el archivo .env no puede ser leído
     */
    private static Properties cargarConfiguracion() {

        Properties propiedades = new Properties();

        try (FileInputStream archivo =
                     new FileInputStream(".env")) {

            propiedades.load(archivo);

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo cargar el archivo .env.",
                    e
            );
        }

        return propiedades;
    }

    /**
     * Establece una conexión con la base de datos MySQL
     * utilizando los datos definidos en el archivo .env.
     *
     * @return conexión activa con MySQL
     * @throws SQLException si ocurre un error al establecer la conexión
     */
    public static Connection conectar() throws SQLException {

        String url =
                CONFIG.getProperty("DB_URL");

        String usuario =
                CONFIG.getProperty("DB_USER");

        String password =
                CONFIG.getProperty("DB_PASSWORD");

        return DriverManager.getConnection(
                url,
                usuario,
                password
        );
    }
}