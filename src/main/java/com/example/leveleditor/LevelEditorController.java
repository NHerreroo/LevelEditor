package com.example.leveleditor;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelEditorController {

    @FXML
    private StackPane mapPane; // StackPane principal
    @FXML
    private ImageView backgroundImage; // Imagen del mapa (floor2.png)

    // Margen extra alrededor de la imagen
    private final double extraMargin = 100;

    // Límites de la imagen en Godot
    private final double godotMinX = -4.5; // Esquina superior izquierda X
    private final double godotMaxX = 4.7;  // Esquina inferior derecha X
    private final double godotMinZ = 8.3;  // Esquina superior izquierda Z
    private final double godotMaxZ = -8.3; // Esquina inferior derecha Z

    @FXML
    public void initialize() {
        // Manejar clics en el mapa
        mapPane.setOnMouseClicked(this::placeTree);
    }

    private void placeTree(MouseEvent event) {
        // Obtener la posición de la imagen dentro del StackPane con el margen extra
        double imageMinX = backgroundImage.getLayoutX() - extraMargin;
        double imageMinY = backgroundImage.getLayoutY() - extraMargin;
        double imageMaxX = imageMinX + backgroundImage.getFitWidth() + (extraMargin * 2);
        double imageMaxY = imageMinY + backgroundImage.getFitHeight() + (extraMargin * 2);

        // Verificar que el clic está dentro del área expandida de la imagen
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
        double godotX = mapValue(event.getX(), imageMinX, imageMaxX, godotMinX, godotMaxX);
        double godotZ = mapValue(event.getY(), imageMinY, imageMaxY, godotMinZ, godotMaxZ);

        // Imprimir la posición en coordenadas de Godot
        System.out.println("Árbol colocado en Godot: X=" + godotX + ", Z=" + godotZ);

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
