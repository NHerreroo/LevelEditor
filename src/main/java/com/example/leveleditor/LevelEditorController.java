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
        double javafxX = event.getX() - mapPane.getWidth() / 2;
        double javafxY = event.getY() - mapPane.getHeight() / 2;

        // Convertir coordenadas de JavaFX a Godot
        double godotX = mapRange(javafxX, -213.50, 207.50, -4.6, 4.6);
        double godotZ = mapRange(javafxY, -124.00, 116.00, 8.3, -8.3); // Nota: Z está invertido

        // Crear y posicionar el árbol visualmente
        Rectangle tree = new Rectangle(50, 50, Color.GREEN);
        tree.setStroke(Color.BLACK);
        tree.setStrokeWidth(1);
        tree.setTranslateX(javafxX);
        tree.setTranslateY(javafxY);

        // Añadir el árbol al exportador
        treeExporter.addTree(godotX, godotZ);

        // Imprimir las coordenadas de Godot
        System.out.printf(Locale.US, "Árbol colocado en Godot: X=%.2f, Y=0.00, Z=%.2f%n",
                godotX, godotZ);

        // Agregar el árbol al mapa
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
     * Función para mapear un valor de un rango a otro
     */
    private double mapRange(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }
}