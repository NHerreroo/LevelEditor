package com.example.leveleditor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    // Botones de objetos
    @FXML
    private Button treeButton_1, treeButton_2, treeButton_3;
    @FXML
    private Button rockButton_1, rockButton_2, rockButton_3;
    @FXML
    private Button miscButton_1, miscButton_2, miscButton_3;

    private final TreeExporter treeExporter = new TreeExporter();

    // Estado para controlar qué objeto se está colocando
    private String selectedObjectType = null; // Puede ser "tree", "rock", "misc", etc.

    // Coordenadas de referencia para la conversión
    private final double javafxXMin = -216.50;
    private final double javafxYMin = -128.00;
    private final double javafxXMax = 216.50;
    private final double javafxYMax = 128.00;

    private final double godotXMin = -4.6;
    private final double godotZMin = 8.5;
    private final double godotXMax = 4.6;
    private final double godotZMax = -8.5;

    @FXML
    public void initialize() {
        // Configurar el evento de clic en el mapa
        mapPane.setOnMouseClicked(this::placeObject);

        // Configurar los botones de objetos
        treeButton_1.setOnAction(event -> selectObject("tree_1"));
        treeButton_2.setOnAction(event -> selectObject("tree_2"));
        treeButton_3.setOnAction(event -> selectObject("tree_3"));

        rockButton_1.setOnAction(event -> selectObject("rock_1"));
        rockButton_2.setOnAction(event -> selectObject("rock_2"));
        rockButton_3.setOnAction(event -> selectObject("rock_3"));

        miscButton_1.setOnAction(event -> selectObject("misc_1"));
        miscButton_2.setOnAction(event -> selectObject("misc_2"));
        miscButton_3.setOnAction(event -> selectObject("misc_3"));

        // Configurar el menú de exportación
        exportLevelMenuItem.setOnAction(event -> {
            try {
                exportLevel();
            } catch (java.io.IOException e) {
                showAlert("Error", "Error al exportar el nivel: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void selectObject(String objectType) {
        // Seleccionar el tipo de objeto
        selectedObjectType = objectType;
        System.out.println("Objeto seleccionado: " + objectType);
    }

    private void placeObject(MouseEvent event) {
        if (selectedObjectType == null) {
            showAlert("Advertencia", "Selecciona un objeto primero", Alert.AlertType.WARNING);
            return;
        }

        // Obtener las coordenadas del clic
        double javafxX = event.getX() - mapPane.getWidth() / 2;
        double javafxY = event.getY() - mapPane.getHeight() / 2;

        // Convertir las coordenadas a Godot
        double[] godotCoords = convertirJavaFXaGodot(javafxX, javafxY);
        double godotX = godotCoords[0];
        double godotZ = godotCoords[2];

        // Añadir el objeto al exportador
        treeExporter.addObject(selectedObjectType, godotX, godotZ);

        // Dibujar el objeto en el mapa
        Rectangle object = createObjectRectangle(selectedObjectType);
        object.setTranslateX(javafxX);
        object.setTranslateY(javafxY);
        mapPane.getChildren().add(object);

        // Imprimir las coordenadas para depuración
        System.out.printf(Locale.US, "Objeto colocado en JavaFX: X=%.2f, Y=%.2f%n", javafxX, javafxY);
        System.out.printf(Locale.US, "Coordenadas en Godot: X=%.2f, Z=%.2f%n", godotX, godotZ);
    }

    private Rectangle createObjectRectangle(String objectType) {
        // Crear un rectángulo con un color o estilo diferente según el tipo de objeto
        Rectangle object = new Rectangle(25, 25);

        switch (objectType) {
            case "tree_1":
                object.setFill(Color.GREEN);
                break;
            case "tree_2":
                object.setFill(Color.DARKGREEN);
                break;
            case "tree_3":
                object.setFill(Color.FORESTGREEN);
                break;
            case "rock_1":
                object.setFill(Color.GRAY);
                break;
            case "rock_2":
                object.setFill(Color.DARKGRAY);
                break;
            case "rock_3":
                object.setFill(Color.SLATEGRAY);
                break;
            case "misc_1":
                object.setFill(Color.BROWN);
                break;
            case "misc_2":
                object.setFill(Color.SANDYBROWN);
                break;
            case "misc_3":
                object.setFill(Color.LIGHTGREEN);
                break;
            default:
                object.setFill(Color.BLACK);
                break;
        }

        object.setStroke(Color.BLACK);
        object.setStrokeWidth(1);
        return object;
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

    private double[] convertirJavaFXaGodot(double javafxX, double javafxY) {
        double godotX = mapRange(javafxY, javafxYMin, javafxYMax, godotXMin, godotXMax);
        double godotY = 0.0;
        double godotZ = mapRange(javafxX, javafxXMin, javafxXMax, godotZMin, godotZMax);
        return new double[]{godotX, godotY, godotZ};
    }

    private double mapRange(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }
}