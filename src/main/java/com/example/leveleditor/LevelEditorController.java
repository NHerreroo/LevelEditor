package com.example.leveleditor;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelEditorController {

    @FXML
    private Pane mapPane;


    // Método para inicializar los eventos
    @FXML
    public void initialize() {
        mapPane.setOnMouseClicked(event -> {
            System.out.println("Mapa clickeado en: " + event.getX() + ", " + event.getY());
        });
    }


    // Método para colocar un cuadrado verde en el lugar del clic
    private void placeGreenSquare(MouseEvent event) {
        // Crear un nuevo rectángulo verde
        Rectangle square = new Rectangle(50, 50); // Tamaño 50x50
        square.setFill(Color.GREEN);
        square.setStroke(Color.BLACK); // Bordes para mejor visibilidad
        square.setStrokeWidth(1);

        // Establecer la posición del cuadrado en el lugar del clic
        square.setX(event.getX() - square.getWidth() / 2); // Centrar el cuadrado
        square.setY(event.getY() - square.getHeight() / 2);

        // Añadir el cuadrado al Pane
        mapPane.getChildren().add(square);
    }
}
