package view;

import ConexionBD.UsuarioDAO;
import gametechstock.SistemaStock;
import gametechstock.Usuario;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Clase que representa la ventana de inicio de sesión del sistema.
 * Permite al usuario ingresar sus credenciales para acceder al sistema.
 */
public class VentanaLogin {

    // Referencia al sistema de stock para validar usuarios y continuar la sesión
    private final SistemaStock sistema;

    /**
     * Constructor que recibe el sistema activo.
     * @param sistema instancia del sistema de stock actual
     */
    public VentanaLogin(SistemaStock sistema) {
        this.sistema = sistema;
    }

    /**
     * Muestra la ventana de login en pantalla.
     * @param stage ventana principal proporcionada por JavaFX
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Login - GametechStock");

        // --- Campos de entrada para usuario y contraseña ---
        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();

        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();

        // --- Etiqueta para mostrar errores de autenticación ---
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // --- Botón para ingresar al sistema ---
        Button loginBtn = new Button("Ingresar");

        // Acción al hacer clic en el botón
        loginBtn.setOnAction(e -> {
            String usuario = userField.getText().trim();
            String clave = passField.getText().trim();

            // Valida credenciales
            Usuario u = UsuarioDAO.validarUsuario(usuario, clave);

            if (u != null) {
                // Si es válido, guarda el usuario y abre el menú principal
                sistema.setUsuarioActual(u);
                new VentanaMenu(sistema).mostrar(new Stage()); // abre nueva ventana
                stage.close(); // cierra la ventana de login
            } else {
                // Si es inválido, muestra mensaje de error
                errorLabel.setText("Credenciales incorrectas. Intente nuevamente.");
            }
        });

        // --- Layout principal: VBox con campos y botón ---
        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, errorLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Crea y muestra la escena
        stage.setScene(new Scene(layout, 300, 250));
        stage.show();
    }
}

