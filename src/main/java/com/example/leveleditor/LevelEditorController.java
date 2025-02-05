package com.example.leveleditor;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Locale;

public class LevelEditorController {

    @FXML
    private StackPane mapPane;
    @FXML
    private ImageView backgroundImage;

    // Límites del editor (suelo del nivel)
    private final double editorMinX = -1.8888097660223808;  // Esquina superior izquierda X
    private final double editorMaxX = 2.013936927772126;    // Esquina inferior derecha X
    private final double editorMinZ = -1.9721728081321483;  // Esquina inferior derecha Z
    private final double editorMaxZ = 3.047903430749682;    // Esquina superior izquierda Z

    // Límites de Godot (suelo del nivel)
    private final double godotMinX = -4.6;  // Esquina superior izquierda X
    private final double godotMaxX = 4.6;   // Esquina inferior derecha X
    private final double godotMinZ = -8.3;  // Esquina inferior derecha Z
    private final double godotMaxZ = 8.3;   // Esquina superior izquierda Z

    @FXML
    public void initialize() {
        mapPane.setOnMouseClicked(this::placeTree);
    }

    private void placeTree(MouseEvent event) {
        // Área de la imagen
        double imageMinX = backgroundImage.getLayoutX();
        double imageMinY = backgroundImage.getLayoutY();
        double imageMaxX = imageMinX + backgroundImage.getFitWidth();
        double imageMaxY = imageMinY + backgroundImage.getFitHeight();

        // Verificar que el clic está dentro del área de la imagen
        if (event.getX() < imageMinX || event.getX() > imageMaxX ||
                event.getY() < imageMinY || event.getY() > imageMaxY) {
            return; // No colocar el árbol si está fuera del área permitida
        }

        // Crear un rectángulo verde (árbol)
        Rectangle tree = new Rectangle(50, 50, Color.GREEN);
        tree.setStroke(Color.BLACK);
        tree.setStrokeWidth(1);

        // Posicionar el árbol en la posición del clic dentro del StackPane
        double posX = event.getX() - mapPane.getWidth() / 2;
        double posY = event.getY() - mapPane.getHeight() / 2;
        tree.setTranslateX(posX);
        tree.setTranslateY(posY);

        // Convertir coordenadas JavaFX a Godot
        double normalizedX = (event.getX() - imageMinX) / (imageMaxX - imageMinX);
        double normalizedZ = (event.getY() - imageMinY) / (imageMaxY - imageMinY);

        // Mapear las coordenadas del editor a Godot
        double godotX = mapValue(normalizedX, editorMinX, editorMaxX, godotMinX, godotMaxX);
        double godotZ = mapValue(normalizedZ, editorMinZ, editorMaxZ, godotMinZ, godotMaxZ);

        // Imprimir la posición en coordenadas de Godot
        System.out.printf(Locale.US, "Árbol colocado en Godot: X=%.2f, Y=0.0, Z=%.2f%n",
                Math.min(Math.max(godotX, godotMinX), godotMaxX),
                Math.min(Math.max(godotZ, godotMaxZ), godotMinZ));

        // Agregar el árbol al mapa
        mapPane.getChildren().add(tree);
    }

    /**
     * Función para convertir un valor de un rango a otro (Regla de tres)
     */
    private double mapValue(double value, double srcMin, double srcMax, double destMin, double destMax) {
        return destMin + (value - srcMin) * (destMax - destMin) / (srcMax - srcMin);
    }
}