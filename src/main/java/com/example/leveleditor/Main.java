package com.example.leveleditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LevelEditor.fxml"));

        // Crear la escena
        Scene scene = new Scene(fxmlLoader.load()); // Tamaño inicial: 1200x800
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Configurar el Stage
        primaryStage.setTitle("LevelEditor");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Desactivar redimensión
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
