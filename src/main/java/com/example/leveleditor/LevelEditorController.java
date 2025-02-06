package com.example.leveleditor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Locale;

public class LevelEditorController {

    @FXML
    private StackPane mapPane;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private MenuItem exportLevelMenuItem;

    private final TreeExporter treeExporter = new TreeExporter();

    // Coordenadas de referencia para la conversión
    private final double javafxXMin = -216.50;
    private final double javafxYMin = -128.00;
    private final double javafxXMax = 216.50; // Ancho de la imagen
    private final double javafxYMax = 128.00; // Altura de la imagen

    private final double godotXMin = -4.6;
    private final double godotZMin = 8.5;
    private final double godotXMax = 4.6;
    private final double godotZMax = -8.5;

    @FXML
    public void initialize() {
        mapPane.setOnMouseClicked(this::placeTree);

        exportLevelMenuItem.setOnAction(event -> {
            try {
                exportLevel();
            } catch (java.io.IOException e) {
                showAlert("Error", "Error al exportar el nivel: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void placeTree(MouseEvent event) {
        // Obtener las coordenadas del clic relativas al StackPane
        double javafxX = event.getX() - mapPane.getWidth() / 2; // Eje horizontal en JavaFX
        double javafxY = event.getY() - mapPane.getHeight() / 2; // Eje vertical en JavaFX

        // Crear y posicionar el árbol visualmente en JavaFX
        Rectangle tree = new Rectangle(25, 25, Color.GREEN);
        tree.setStroke(Color.BLACK);
        tree.setStrokeWidth(1);
        tree.setTranslateX(javafxX);
        tree.setTranslateY(javafxY);

        // Convertir las coordenadas de JavaFX a Godot
        double[] godotCoords = convertirJavaFXaGodot(javafxX, javafxY);
        double godotX = godotCoords[0];
        double godotY = godotCoords[1];
        double godotZ = godotCoords[2];

        // Imprimir las coordenadas en ambos sistemas para depuración
        System.out.printf(Locale.US, "Árbol colocado en JavaFX: X=%.2f, Y=%.2f%n", javafxX, javafxY);
        System.out.printf(Locale.US, "Coordenadas en Godot: X=%.2f, Y=%.2f, Z=%.2f%n", godotX, godotY, godotZ);

        // Añadir el árbol al exportador usando las coordenadas de Godot
        treeExporter.addTree(godotX, godotZ);

        // Agregar el árbol al mapa en JavaFX
        mapPane.getChildren().add(tree);
    }

    private void exportLevel() throws java.io.IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar nivel");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Godot Scene", "*.tscn")
        );

        File file = fileChooser.showSaveDialog(mapPane.getScene().getWindow());
        if (file != null) {
            treeExporter.exportToFile(file.getAbsolutePath());
            showAlert("Éxito", "Nivel exportado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Convertir coordenadas de JavaFX a Godot.
     */

    private double[] convertirJavaFXaGodot(double javafxX, double javafxY) {
        // Mapear las coordenadas de JavaFX a las coordenadas de Godot
        double godotX = mapRange(javafxY, javafxYMin, javafxYMax, godotXMin, godotXMax); // Y en JavaFX -> X en Godot
        double godotY = 0.0; // Y en Godot es siempre 0
        double godotZ = mapRange(javafxX, javafxXMin, javafxXMax, godotZMin, godotZMax); // X en JavaFX -> Z en Godot

        // Invertir los valores
        return new double[]{godotX, godotY, godotZ};
    }
    /**
     * Función para mapear un valor de un rango a otro.
     */
    private double mapRange(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }
}
