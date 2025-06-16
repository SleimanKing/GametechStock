# GametechStock

**GametechStock** es una aplicación de escritorio desarrollada en JavaFX para gestionar el stock de productos tecnológicos. Permite registrar ingresos, egresos, ajustes y consultar historial de movimientos con persistencia en una base de datos MySQL.

## 📦 Características

- Registro de productos nuevos
- Gestión de ingresos y egresos de stock
- Ajustes manuales de inventario
- Consulta de stock en tiempo real
- Historial completo de movimientos
- Generación de listas de productos y movimientos

## 🛠️ Tecnologías usadas

- Java 21
- JavaFX 24
- MySQL
- MVC Pattern

## 🧱 Estructura del proyecto

Ver el archivo `/readme.txt` para más detalles.

## 🚀 Cómo ejecutar

1. Cloná el proyecto:

git clone https://github.com/usuario/GametechStock.git
cd GametechStock

2. Asegurate de tener Java y JavaFX configurados.

3. Compilá y ejecutá:

javac --module-path "path/a/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -d out $(find src -name "*.java")
java --module-path "path/a/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp out main.GametechStock

4. Asegurate de tener corriendo MySQL con el archivo db/gametech_db.sql cargado.