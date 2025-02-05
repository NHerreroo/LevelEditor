module com.example.leveleditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.leveleditor to javafx.fxml;
    exports com.example.leveleditor;
}