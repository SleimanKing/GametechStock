package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/gametech_db";
    private static final String USER = "root";//usuario del server SQL
    private static final String PASSWORD = "";//colocar clave propia

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
