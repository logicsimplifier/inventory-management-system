package com.inventoryapp.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StaffDashboardController {

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/login.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Inventory Management - Login");
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login page: " + e.getMessage());
        }
    }

    @FXML
    private void showInventory() {
        // Logic for showing inventory goes here
        System.out.println("Inventory button clicked");
    }

    // Optional: stub methods if you're using other onAction handlers like these in FXML
    @FXML
    private void showDashboard() {
        System.out.println("Dashboard button clicked");
    }

    
    @FXML
    private void showReports() {
        System.out.println("Reports button clicked");
    }

    @FXML
    private void addInventoryItem() {
        System.out.println("Add Item button clicked");
    }

    @FXML
    private void exportInventory() {
        System.out.println("Export button clicked");
    }
}
