package com.inventoryapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private void showDashboard(ActionEvent event) {
        System.out.println("Dashboard clicked");
    }

    @FXML
    private void showInventory(ActionEvent event) {
        System.out.println("Inventory clicked");
    }

    @FXML
    private void showProducts(ActionEvent event) {
        System.out.println("Products clicked");
    }

    @FXML
    private void showCategories(ActionEvent event) {
        System.out.println("Categories clicked");
    }

    @FXML
    private void showSuppliers(ActionEvent event) {
        System.out.println("Suppliers clicked");
    }

    @FXML
    private void showOrders(ActionEvent event) {
        System.out.println("Orders clicked");
    }

    @FXML
    private void showReports(ActionEvent event) {
        System.out.println("Reports clicked");
    }

    @FXML
    private void showSettings(ActionEvent event) {
        System.out.println("Settings clicked");
    }

    @FXML
    private void showHelp(ActionEvent event) {
        System.out.println("Help clicked");
    }

    @FXML
    private void addInventoryItem(ActionEvent event) {
        System.out.println("Add Inventory clicked");
    }

    @FXML
    private void exportInventory(ActionEvent event) {
        System.out.println("Export Inventory clicked");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/login.fxml"));
            Parent loginRoot = loader.load();

            // Create new scene
            Scene loginScene = new Scene(loginRoot);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene and update window title
            currentStage.setScene(loginScene);
            currentStage.setTitle("Inventory Management - Login");
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login page: " + e.getMessage());
        }
    }
}
