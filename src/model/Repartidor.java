package model;
/**
 * Representa a un repartidor disponible en SpeedFast.
 *
 * Cada repartidor posee un identificador único y un nombre.
 */
public class Repartidor {

    private final int id;
    private final String nombre;

    /**
     * Crea un nuevo repartidor.
     *
     * @param id identificador del repartidor
     * @param nombre nombre del repartidor
     */
    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    /**
     * Obtiene el identificador del repartidor.
     *
     * @return ID del repartidor
     */
    public int getId() {
        return id;
    }
    /**
     * Obtiene el nombre del repartidor.
     *
     * @return nombre del repartidor
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Devuelve una representación simple del repartidor.
     *
     * Este método permite mostrar directamente el nombre
     * del repartidor en componentes como JComboBox.
     *
     * @return nombre del repartidor
     */
    @Override
    public String toString() {
        return nombre;
    }
}

