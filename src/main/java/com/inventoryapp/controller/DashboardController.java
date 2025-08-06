package com.inventoryapp.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane dashboardPane;

    // Inventory View and Dynamic Content Pane
    @FXML
    private VBox inventoryView;

    @FXML
    private StackPane dynamicContent;

    // Inventory Table Elements
    @FXML
    private TableView<?> inventoryTable;
    @FXML
    private TableColumn<?, ?> idColumn;
    @FXML
    private TableColumn<?, ?> productColumn;
    @FXML
    private TableColumn<?, ?> categoryColumn;
    @FXML
    private TableColumn<?, ?> stockColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> statusColumn;
    @FXML
    private TableColumn<?, ?> actionsColumn;

    @FXML
    private ComboBox<String> itemsPerPageCombo;

    @FXML
    private Pagination inventoryPagination;

    @FXML
    private TextField searchField;

    // === Page Navigation ===

    @FXML
    private void showDashboard(ActionEvent event) {
        showDefaultView();
        System.out.println("Dashboard view displayed");
    }

    @FXML
    private void showInventory(ActionEvent event) {
        loadContentView("/fxml/view/inventory.fxml", "Inventory");
        System.out.println("Inventory content view displayed");
    }


    @FXML
    private void showReports(ActionEvent event) {
        System.out.println("Reports clicked");
        // TODO: Implement reports content
    }

    @FXML
    private void showRoleManagement(ActionEvent event) {
        loadContentView("/fxml/view/userRoleManagemnet.fxml", "Role Management");
    }

    @FXML
    private void showHelp(ActionEvent event) {
        System.out.println("Help clicked");
        // TODO: Implement help support view
    }

    // === Inventory Actions ===

    @FXML
    private void addInventoryItem(ActionEvent event) {
        System.out.println("Add Inventory Item clicked");
        // TODO: Implement add item logic
    }

    @FXML
    private void exportInventory(ActionEvent event) {
        System.out.println("Export Inventory clicked");
        // TODO: Implement export logic
    }

    // === Logout Button ===
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

    // === Helpers ===

    private void loadContentView(String fxmlPath, String contentName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();

            dynamicContent.getChildren().clear();
            dynamicContent.getChildren().add(content);

            inventoryView.setVisible(false);
            inventoryView.setManaged(false);

            System.out.println(contentName + " content loaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + contentName + " content: " + e.getMessage());
        }
    }

    private void showDefaultView() {
        inventoryView.setVisible(true);
        inventoryView.setManaged(true);
        inventoryView.toFront();
        dynamicContent.getChildren().clear();
    }

    // === Initialize (optional for setting default ComboBox values) ===
    @FXML
    private void initialize() {
        // Load CSS programmatically as backup
        try {
            String cssPath = getClass().getResource("/css/adminDashboard.css").toExternalForm();
            if (dashboardPane != null && dashboardPane.getScene() != null) {
                dashboardPane.getScene().getStylesheets().add(cssPath);
                System.out.println("CSS loaded programmatically: " + cssPath);
            }
        } catch (Exception e) {
            System.err.println("Failed to load CSS programmatically: " + e.getMessage());
        }
        
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList("10", "25", "50", "100"));
            itemsPerPageCombo.getSelectionModel().selectFirst();
        }

        // TODO: Setup inventory table and pagination logic here if needed
    }
}
