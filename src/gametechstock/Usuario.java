package gametechstock; // Paquete principal del sistema

/**
 * Clase que representa un usuario del sistema.
 * Cada usuario tiene un nombre, una contraseña (en formato hash), un identificador único y un rol asignado.
 */
public class Usuario {

    // Nombre real o nombre completo del usuario
    private String nombre;

    // Contraseña en formato hash (por seguridad)
    private String passwordHash;

    // Nombre de usuario (login), puede diferir del nombre completo
    private String usuario;

    // ID único del usuario en la base de datos
    private int id;
    
    // Rol del usuario: ADMINISTRADOR, ENCARGADO o LOGISTICA
    private RolUsuario rol;

    /**
     * Constructor completo con todos los datos provenientes de la base de datos.
     * @param id ID único del usuario
     * @param nombre nombre completo o visible del usuario
     * @param usuario nombre de usuario usado para iniciar sesión
     * @param hash contraseña ya encriptada (hash)
     * @param rol rol asignado (enum)
     */
    public Usuario(int id, String nombre, String usuario, String hash, RolUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.passwordHash = hash;
        this.rol = rol;
    }

    /**
     * Constructor simplificado para pruebas o creación rápida de usuarios sin ID ni contraseña.
     * @param nombre nombre visible
     * @param usuario login
     * @param rol rol asignado
     */
    public Usuario(String nombre, String usuario, RolUsuario rol) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.rol = rol;
    }

    /**
     * Devuelve el nombre completo del usuario.
     * @return nombre como String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el rol del usuario.
     * @return enum RolUsuario
     */
    public RolUsuario getRol() {
        return this.rol;
    }

    /**
     * Verifica si la contraseña ingresada coincide con el hash almacenado.
     * En esta versión, compara directamente el hash (simulado).
     * @param claveIngresada contraseña ingresada por el usuario
     * @return true si coincide, false si no
     */
    public boolean validarClave(String claveIngresada) {
        return this.passwordHash.equals(claveIngresada); // En versiones reales, se debe usar hashing seguro (ej: BCrypt)
    }

    /**
     * Devuelve el identificador único del usuario.
     * @return ID entero
     */
    public int getId() {
        return id;
    }
}
