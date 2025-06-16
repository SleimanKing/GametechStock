A. GametechStock - Instrucciones de Configuración
-------------------------------------------------
Antes de ejecutar la aplicación Java, seguí estos pasos:

1. CONFIGURAR CONEXIÓN A LA BASE DE DATOS
------------------------------------------
Editá la clase `ConexionBD.java` para ingresar tus credenciales de MySQL.

Ubicación de la clase:  
src/ConexionBD.java

Modificá las siguientes líneas según tu configuración (MySQL Workbench o XAMPP):

    private static final String URL = "jdbc:mysql://localhost:3306/gametech_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

Asegurate de que el nombre de usuario y la contraseña coincidan con tu instalación de MySQL.

2. EJECUTAR EL SCRIPT DE BASE DE DATOS
---------------------------------------
Importá el archivo SQL que se encuentra en la carpeta del proyecto:

    db/gametechstock.sql

Este script creará la base de datos `gametech_db`, sus tablas y datos iniciales.

Podés ejecutarlo desde MySQL Workbench o con la consola de MySQL:

    mysql -u tu_usuario -p < db/gametechstock.sql

*Nota: No olvides cargar el driver que se encuentra en la carpeta GametechStock\mysql-connector-j-9.3.0.jar al ejecutar la app para poder conectar con la base de datos.

B. Gametech Stock - Aplicación JavaFX para gestión de inventario
----------------------------------------------------------------
Requisitos previos:
-----------------------
1. CONFIGURAR LIBRERIA JAVAFX
------------------------------
Para ejecutar correctamente esta aplicación, es necesario tener configurada la librería JavaFX.

1. Descargar JavaFX SDK (versión 24.0.1 o compatible) desde:
   https://gluonhq.com/products/javafx/

2. Colocar la carpeta del SDK descargado en la siguiente ubicación recomendada:
   C:\javafx-sdk-24.0.1

   (Debe existir esta ruta: C:\javafx-sdk-24.0.1\lib)

3. Ejecutar la aplicación con los siguientes parámetros:

--module-path C:\javafx-sdk-24.0.1\lib --add-modules javafx.controls,javafx.fxml

¡Listo! Ahora ya podés ejecutar la aplicación Java sin problemas.

C. Gametech Stock - Estructura del Proyecto
----------------------------------------------------------------

GametechStock/
├── build.xml
├── manifest.mf
├── mysql-connector-j-9.3.0.jar
├── readme.txt
├── build/
├── dist/
├── db/
│   └── gametech_db.sql
├── nbproject/
├── src/
│   ├── ConexionBD/
│   │   ├── ConexionBD.java
│   │   ├── MovimientoDAO.java
│   │   ├── ProductoDAO.java
│   │   └── UsuarioDAO.java
│   ├── gametechstock/
│   │   ├── Ajuste.java
│   │   ├── Deposito.java
│   │   ├── Egreso.java
│   │   ├── Ingreso.java
│   │   ├── Movimiento.java
│   │   ├── Producto.java
│   │   ├── RolUsuario.java
│   │   ├── SistemaStock.java
│   │   └── Usuario.java
│   ├── main/
│   │   └── GametechStock.java
│   └── view/
│       ├── VentanaAjuste.java
│       ├── VentanaEgreso.java
│       ├── VentanaHistorial.java
│       ├── VentanaIngreso.java
│       ├── VentanaLogin.java
│       ├── VentanaMenu.java
│       ├── VentanaNuevoProducto.java
│       └── VentanaStock.java
└── test/