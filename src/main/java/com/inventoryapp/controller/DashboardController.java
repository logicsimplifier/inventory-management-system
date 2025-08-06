package com.inventoryapp.controller;

import com.inventoryapp.dao.InventoryDAO;
import com.inventoryapp.model.InventoryItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    // Navigation Buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button inventoryButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button roleManagementButton;
    @FXML
    private Button helpButton;

    // Stats Cards Labels
    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label lowStockLabel;
    @FXML
    private Label totalValueLabel;
    @FXML
    private Label outOfStockLabel;

    private InventoryDAO inventoryDAO;

    public DashboardController() {
        this.inventoryDAO = new InventoryDAO();
    }

    // === Page Navigation ===
    @FXML
    private void showDashboard(ActionEvent event) {
        showDefaultView();
        setActiveButton(dashboardButton);
        System.out.println("Dashboard view displayed");
    }

    @FXML
    private void showInventory(ActionEvent event) {
        loadContentView("/fxml/view/inventory.fxml", "Inventory");
        setActiveButton(inventoryButton);
        System.out.println("Inventory content view displayed");
    }

    @FXML
    private void showReports(ActionEvent event) {
        System.out.println("Reports clicked");
        setActiveButton(reportsButton);
        // TODO: Implement reports content
        loadContentView("/fxml/view/reportView.fxml", "Reports");
    }

    @FXML
    private void showRoleManagement(ActionEvent event) {
        loadContentView("/fxml/view/userRoleManagemnet.fxml", "Role Management");
        setActiveButton(roleManagementButton);
    }

    @FXML
    private void showHelp(ActionEvent event) {
        System.out.println("Help clicked");
        setActiveButton(helpButton);
    }

    // === Inventory Actions ===
    @FXML
    private void addInventoryItem(ActionEvent event) {
        System.out.println("Add Inventory Item clicked");
        loadContentView("/fxml/view/inventory.fxml", "Inventory");

    }

    @FXML
    private void exportInventory(ActionEvent event) {
        System.out.println("Export Inventory clicked");
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

    private void setActiveButton(Button activeButton) {
        dashboardButton.getStyleClass().remove("active");
        inventoryButton.getStyleClass().remove("active");
        reportsButton.getStyleClass().remove("active");
        roleManagementButton.getStyleClass().remove("active");
        helpButton.getStyleClass().remove("active");

        if (!activeButton.getStyleClass().contains("active")) {
            activeButton.getStyleClass().add("active");
        }
    }

    // === Initialize ===
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

        // Set up items per page combo box
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList("10", "25", "50", "100"));
            itemsPerPageCombo.getSelectionModel().selectFirst();
        }

        // Initialize table columns
        initializeTableColumns();

        // Load inventory data
        loadInventoryData();

        // Setup search functionality
        setupSearch();

        showDefaultView();
        setActiveButton(dashboardButton);
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        statusColumn.setCellValueFactory(cellData -> {
            int quantity = cellData.getValue().getQuantity();
            String status;
            if (quantity == 0) {
                status = "Out of Stock";
            } else if (quantity < 5) {
                status = "Low Stock";
            } else {
                status = "In Stock";
            }
            return new SimpleStringProperty(status);
        });
    }

    private void loadInventoryData() {
        List<InventoryItem> items = inventoryDAO.getAllItems();
        inventoryTable.setItems(FXCollections.observableArrayList(items));
        updateStatsCards(items);
    }

    private void updateStatsCards(List<InventoryItem> items) {
        int totalItems = items.size();
        int lowStock = (int) items.stream().filter(item -> item.getQuantity() < 5 && item.getQuantity() > 0).count();
        int outOfStock = (int) items.stream().filter(item -> item.getQuantity() == 0).count();
        double totalValue = items.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

        totalItemsLabel.setText(String.valueOf(totalItems));
        lowStockLabel.setText(String.valueOf(lowStock));
        outOfStockLabel.setText(String.valueOf(outOfStock));
        totalValueLabel.setText(String.format("Rs%.2f", totalValue));
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadInventoryData();
            } else {
                List<InventoryItem> filteredItems = inventoryDAO.searchItemsByName(newValue);
                inventoryTable.setItems(FXCollections.observableArrayList(filteredItems));
            }
        });
    }
}