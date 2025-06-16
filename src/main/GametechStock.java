package main; // Paquete principal del sistema

import gametechstock.SistemaStock;
import view.VentanaLogin;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 * Inicia el sistema de gestión de stock y lanza la interfaz gráfica.
 */
public class GametechStock extends Application {

    // Modelo central del sistema: gestiona productos, usuarios y movimientos
    private SistemaStock sistema;

    /**
     * Punto de inicio de la interfaz gráfica JavaFX.
     * Este método se invoca automáticamente cuando se lanza la aplicación.
     *
     * @param primaryStage la ventana principal de JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        sistema = new SistemaStock();             // Se crea el modelo principal del sistema
        sistema.cargarDatosDesdeBD();             // Se cargan productos, usuarios y movimientos desde MySQL

        // Muestra la primera pantalla: login de usuario
        VentanaLogin login = new VentanaLogin(sistema);
        login.mostrar(primaryStage);
    }

    /**
     * Método main: arranque tradicional para cualquier aplicación Java.
     * Llama al método launch() que inicializa JavaFX.
     *
     * @param args argumentos opcionales desde línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
