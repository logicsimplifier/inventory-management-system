package com.inventoryapp;

import com.inventoryapp.util.DBUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main Application class for Inventory Management System
 * Entry point of the JavaFX application
 */
public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage = new Stage();
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/login.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Inventory Management System");
        stage.show();
    }


    /**
     * Switches scene dynamically
     * @param fxmlFile the FXML file name inside /fxml/view/
     * @param title the window title
     */
    public static void changeScene(String fxmlFile, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/view/" + fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
