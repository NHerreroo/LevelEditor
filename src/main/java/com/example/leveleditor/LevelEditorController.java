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
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class LevelEditorController {

    @FXML
    private StackPane mapPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private MenuItem exportLevelMenuItem, newLevelMenuItem, exitMenuItem;

    @FXML
    private Button treeButton_1, treeButton_2, treeButton_3;
    @FXML
    private Button rockButton_1, rockButton_2, rockButton_3;
    @FXML
    private Button miscButton_1, miscButton_2, miscButton_3;

    private final TreeExporter treeExporter = new TreeExporter();
    private String selectedObjectType = null;

    private final double javafxXMin = -216.50, javafxYMin = -128.00, javafxXMax = 216.50, javafxYMax = 128.00;
    private final double godotXMin = -4.6, godotZMin = 8.5, godotXMax = 4.6, godotZMax = -8.5;

    // Mapeo de colores para objetos
    private static final Map<String, Color> colorMap = Map.of(
            "tree_1", Color.GREEN, "tree_2", Color.DARKGREEN, "tree_3", Color.FORESTGREEN,
            "rock_1", Color.GRAY, "rock_2", Color.DARKGRAY, "rock_3", Color.SLATEGRAY,
            "misc_1", Color.BROWN, "misc_2", Color.SANDYBROWN, "misc_3", Color.LIGHTGREEN
    );

    // Lambda para mostrar alertas de información
    private final Consumer<String> infoAlert = msg -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    };

    @FXML
    public void initialize() {
        mapPane.setOnMouseClicked(this::placeObject);

        // Asignar eventos a los botones usando Streams
        List.of(treeButton_1, treeButton_2, treeButton_3).forEach(button ->
                button.setOnAction(event -> selectObject("tree_" + (List.of(treeButton_1, treeButton_2, treeButton_3).indexOf(button) + 1)))
        );

        List.of(rockButton_1, rockButton_2, rockButton_3).forEach(button ->
                button.setOnAction(event -> selectObject("rock_" + (List.of(rockButton_1, rockButton_2, rockButton_3).indexOf(button) + 1)))
        );

        List.of(miscButton_1, miscButton_2, miscButton_3).forEach(button ->
                button.setOnAction(event -> selectObject("misc_" + (List.of(miscButton_1, miscButton_2, miscButton_3).indexOf(button) + 1)))
        );

        // Eventos para los menús
        newLevelMenuItem.setOnAction(event -> clearMap());
        exitMenuItem.setOnAction(event -> closeApplication());
        exportLevelMenuItem.setOnAction(event -> {
            try {
                exportLevel();
            } catch (java.io.IOException e) {
                infoAlert.accept("Error al exportar el nivel: " + e.getMessage());
            }
        });
    }

    private void selectObject(String objectType) {
        selectedObjectType = objectType;
        System.out.println("Objeto seleccionado: " + objectType);
    }

    private void placeObject(MouseEvent event) {
        if (selectedObjectType == null) {
            infoAlert.accept("Selecciona un objeto primero");
            return;
        }

        double javafxX = event.getX() - mapPane.getWidth() / 2;
        double javafxY = event.getY() - mapPane.getHeight() / 2;

        double[] godotCoords = convertirJavaFXaGodot(javafxX, javafxY);
        double godotX = godotCoords[0], godotZ = godotCoords[2];

        treeExporter.addObject(selectedObjectType, godotX, godotZ);

        Rectangle object = createObjectRectangle(selectedObjectType);
        object.setTranslateX(javafxX);
        object.setTranslateY(javafxY);
        mapPane.getChildren().add(object);

        System.out.printf(Locale.US, "Objeto colocado en JavaFX: X=%.2f, Y=%.2f%n", javafxX, javafxY);
        System.out.printf(Locale.US, "Coordenadas en Godot: X=%.2f, Z=%.2f%n", godotX, godotZ);
    }

    private Rectangle createObjectRectangle(String objectType) {
        Rectangle object = new Rectangle(25, 25);
        object.setFill(colorMap.getOrDefault(objectType, Color.BLACK));
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
            infoAlert.accept("Nivel exportado correctamente");
        }
    }

    private void clearMap() {
        // Usa Stream para eliminar solo los objetos sin borrar la imagen de fondo
        mapPane.getChildren().removeIf(node -> node instanceof Rectangle);
        treeExporter.clearObjects();
        infoAlert.accept("Mapa reiniciado.");
    }

    private void closeApplication() {
        Stage stage = (Stage) mapPane.getScene().getWindow();
        stage.close();
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
