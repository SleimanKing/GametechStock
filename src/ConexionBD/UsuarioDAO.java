package ConexionBD;

import gametechstock.Usuario;
import gametechstock.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar acceso a datos de usuarios.
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales de un usuario usando hash SHA-256.
     * @param usuario nombre de usuario
     * @param password contraseña en texto plano
     * @return objeto Usuario si las credenciales son correctas, null si no lo son
     */
    public static Usuario validarUsuario(String usuario, String password) {
        try (Connection conn = ConexionBD.obtenerConexion()) {

            // Consulta para validar nombre de usuario y contraseña encriptada
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password_hash = SHA2(?, 256)";
            var stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, password);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                RolUsuario rol = RolUsuario.valueOf(rs.getString("rol")); // ADMIN, OPERADOR, etc.
                return new Usuario(id, nombre, usuario, rs.getString("password_hash"), rol);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Loguea si hay error (por ejemplo, problema de conexión)
        }

        return null; // Si no se encuentra el usuario o hay error
    }

    /**
     * Recupera todos los usuarios registrados en la base de datos.
     * @return lista de usuarios
     */
    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("usuario");
                String passwordHash = rs.getString("password_hash");
                RolUsuario rol = RolUsuario.valueOf(rs.getString("rol"));

                usuarios.add(new Usuario(id, nombre, usuario, passwordHash, rol));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log de error en caso de fallo
        }

        return usuarios;
    }
}
