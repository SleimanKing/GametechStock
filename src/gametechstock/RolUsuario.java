package gametechstock; // Paquete principal del sistema

/**
 * Enumeración que define los diferentes roles que puede tener un usuario dentro del sistema.
 * Cada rol determina el nivel de acceso y las operaciones permitidas.
 */
public enum RolUsuario {

    // Rol encargado de supervisar y gestionar productos y movimientos comunes
    ENCARGADO,

    // Rol orientado a logística; acceso más limitado
    LOGISTICA,

    // Rol con acceso total al sistema, incluyendo ajustes y administración
    ADMINISTRADOR
}