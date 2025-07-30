package com.inventoryapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage = new Stage();
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Inventory System - Login");
        stage.show();
    }

    public static void changeScene(String fxmlFile, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + fxmlFile));
        Scene scene = new Scene(loader.load());
        mainStage.setScene(scene);
        mainStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch();
    }
}
