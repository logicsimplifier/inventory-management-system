package com.inventoryapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button logoutButton;
    
    @FXML
    private StackPane dynamicContent;
    
    @FXML
    private VBox inventoryView;

    @FXML
    private void showDashboard(ActionEvent event) {
        showDefaultView();
        System.out.println("Dashboard view displayed");
    }

    @FXML
    private void showInventory(ActionEvent event) {
        showDefaultView();
        System.out.println("Inventory view displayed");
    }

    @FXML
    private void showProducts(ActionEvent event) {
        System.out.println("Products clicked");
        // TODO: Implement products content
        // loadContentView("/fxml/view/productsContent.fxml", "Products");
    }

    @FXML
    private void showCategories(ActionEvent event) {
        System.out.println("Categories clicked");
        // TODO: Implement categories content
        // loadContentView("/fxml/view/categoriesContent.fxml", "Categories");
    }

    @FXML
    private void showSuppliers(ActionEvent event) {
        System.out.println("Suppliers clicked");
        // TODO: Implement suppliers content
        // loadContentView("/fxml/view/suppliersContent.fxml", "Suppliers");
    }

    @FXML
    private void showOrders(ActionEvent event) {
        System.out.println("Orders clicked");
        // TODO: Implement orders content
        // loadContentView("/fxml/view/ordersContent.fxml", "Orders");
    }

    @FXML
    private void showReports(ActionEvent event) {
        System.out.println("Reports clicked");
        // TODO: Implement reports content
        // loadContentView("/fxml/view/reportsContent.fxml", "Reports");
    }

    @FXML
    private void showRoleManagement(ActionEvent event) {
        loadContentView("/fxml/view/roleManagement.fxml", "Role Management");
    }

    @FXML
    private void showHelp(ActionEvent event) {
        System.out.println("Help clicked");
        // TODO: Load help content when created
        // loadContentView("/fxml/view/helpContent.fxml", "Help & Support");
    }
    
    /**
     * Helper method to load content into the dynamic content area
     */
    private void loadContentView(String fxmlPath, String contentName) {
        try {
            // Load the content FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();

            // Clear the dynamic content area and add the new content
            dynamicContent.getChildren().clear();
            dynamicContent.getChildren().add(content);
            
            // Hide the default inventory view
            inventoryView.setVisible(false);
            inventoryView.setManaged(false);
            
            System.out.println(contentName + " content loaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + contentName + " content: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to show the default inventory view
     */
    private void showDefaultView() {
        inventoryView.setVisible(true);
        inventoryView.setManaged(true);
        dynamicContent.getChildren().clear();
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
