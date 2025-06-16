package gametechstock; // Paquete principal donde se encuentra la clase

import ConexionBD.ConexionBD; // Importa la clase para manejar conexión con la base de datos
import java.sql.*; // Importa librerías necesarias para trabajar con SQL
import java.util.ArrayList; // Lista dinámica
import java.util.List; // Interfaz de lista

public class Deposito {
    private int id; // Identificador único del depósito
    private String ubicacion; // Ubicación física del depósito
    private int capacidad; // Capacidad total del depósito

    // Constructor que inicializa todos los campos del depósito
    public Deposito(int id, String ubicacion, int capacidad) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    // Constructor alternativo por si no se conoce la capacidad (se asume 0)
    public Deposito(int id, String ubicacion) {
        this(id, ubicacion, 0);
    }

    // Métodos de acceso (getters) para obtener los valores de los atributos

    public int getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    // Lista estática usada como caché para almacenar depósitos ya cargados desde la base de datos
    private static List<Deposito> listaDepositos = null;

    /**
     * Método estático que obtiene todos los depósitos desde la base de datos.
     * Si ya se han cargado anteriormente, devuelve la lista cacheada.
     * @return lista de objetos Deposito
     */
    public static List<Deposito> obtenerDepositos() {
        // Si la lista está vacía (primera vez), se conecta a la BD y la carga
        if (listaDepositos == null) {
            listaDepositos = new ArrayList<>();
            String sql = "SELECT id, ubicacion, capacidad FROM depositos"; // Consulta SQL para traer los datos

            try (Connection conn = ConexionBD.obtenerConexion(); // Abre conexión
                 PreparedStatement stmt = conn.prepareStatement(sql); // Prepara la consulta
                 ResultSet rs = stmt.executeQuery()) { // Ejecuta y obtiene los resultados

                // Itera sobre cada fila del resultado y crea objetos Deposito
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("ubicacion");
                    int capacidad = rs.getInt("capacidad");
                    listaDepositos.add(new Deposito(id, nombre, capacidad)); // Agrega a la lista cacheada
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Imprime errores en caso de fallo al consultar la base de datos
            }
        }
        return listaDepositos; // Devuelve la lista (cacheada o recién cargada)
    }

    /**
     * Devuelve el depósito considerado por defecto (de ID 2).
     * Si no se encuentra, retorna null.
     * @return el depósito con ID 2 o null si no existe
     */
    public static Deposito obtenerDepositoPorDefecto() {
        for (Deposito d : obtenerDepositos()) {
            if (d.getId() == 2) {
                return d;
            }
        }
        // Si no encuentra el depósito por defecto, retorna null
        return null;
    }
}



