package com.inventoryapp.controller;

import com.inventoryapp.dao.InventoryDAO;
import com.inventoryapp.model.InventoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StaffDashboardController {

    @FXML
    private BorderPane dashboardPane;

    @FXML
    private VBox inventoryView;

    @FXML
    private StackPane dynamicContent;

    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, Integer> idColumn;
    @FXML
    private TableColumn<InventoryItem, String> productColumn;
    @FXML
    private TableColumn<InventoryItem, String> categoryColumn;
    @FXML
    private TableColumn<InventoryItem, Integer> stockColumn;
    @FXML
    private TableColumn<InventoryItem, Double> priceColumn;
    @FXML
    private TableColumn<InventoryItem, String> statusColumn;

    @FXML
    private ComboBox<String> itemsPerPageCombo;

    @FXML
    private Pagination inventoryPagination;

    @FXML
    private TextField searchField;

    private ObservableList<InventoryItem> inventoryData = FXCollections.observableArrayList();
    private InventoryDAO inventoryDAO = new InventoryDAO();

    // === Navigation Actions ===

    @FXML
    private void showDashboard() {
        showDefaultView();
        System.out.println("Dashboard view displayed");
    }

    @FXML
    private void showInventory() {
        loadContentView("/fxml/view/inventory.fxml", "Inventory");
        System.out.println("Inventory view loaded");
    }

    @FXML
    private void showReports(ActionEvent event) {
        System.out.println("Reports clicked");
        loadContentView("/fxml/view/reportView.fxml", "Reports");
    }

    @FXML
    private void addInventoryItem() {
        System.out.println("Add Inventory Item clicked");
    }

    @FXML
    private void exportInventory() {
        System.out.println("Export Inventory clicked");
    }

    // Logout

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

            // Only remove non-default views (keep inventoryView in memory)
            dynamicContent.getChildren().removeIf(node -> node != inventoryView);

            // Hide default inventory view
            inventoryView.setVisible(false);
            inventoryView.setManaged(false);

            // Add new content if not already added
            if (!dynamicContent.getChildren().contains(content)) {
                dynamicContent.getChildren().add(content);
            }

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
        dynamicContent.getChildren().removeIf(node -> node != inventoryView);
    }

    private void loadInventoryData() {
        List<InventoryItem> items = inventoryDAO.getAllItems();
        inventoryData.setAll(items);
        inventoryTable.setItems(inventoryData);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        statusColumn.setCellValueFactory(cellData -> {
            int quantity = cellData.getValue().getQuantity();
            if (quantity == 0) {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Out of Stock");
            } else if (quantity < 5) {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Low Stock");
            } else {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "In Stock");
            }
        });
    }

    @FXML
    private void initialize() {
        if (dynamicContent == null) {
            System.err.println("dynamicContent is null. Check fx:id in FXML.");
        } else {
            System.out.println("dynamicContent injected correctly.");
            showDefaultView();
        }

        setupTableColumns();
        loadInventoryData();

        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadInventoryData();
            } else {
                List<InventoryItem> filteredItems = inventoryDAO.searchItemsByName(newValue);
                inventoryData.setAll(filteredItems);
            }
        });

        // Set up items per page combo box
        itemsPerPageCombo.getSelectionModel().selectFirst();
    }
}