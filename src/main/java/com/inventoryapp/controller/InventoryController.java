package com.inventoryapp.controller;

import com.inventoryapp.dao.InventoryDAO;
import com.inventoryapp.model.InventoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for Inventory Management UI
 * Handles all UI interactions for Add, Update, Delete operations
 */
public class InventoryController implements Initializable {
    
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField supplierField;
    @FXML private TextField searchField;
    
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button searchButton;
    
    @FXML private TableView<InventoryItem> itemsTable;
    @FXML private TableColumn<InventoryItem, Integer> idColumn;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, String> categoryColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItem, Double> priceColumn;
    @FXML private TableColumn<InventoryItem, String> supplierColumn;
    
    @FXML private Label statusLabel;
    
    private InventoryDAO inventoryDAO;
    private ObservableList<InventoryItem> itemsList;
    private InventoryItem selectedItem;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryDAO = new InventoryDAO();
        itemsList = FXCollections.observableArrayList();
        
        setupTableColumns();
        setupComboBox();
        setupEventHandlers();
        loadAllItems();
        
        // Initially disable update and delete buttons
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        
        itemsTable.setItems(itemsList);
    }
    
    private void setupComboBox() {
        categoryCombo.setItems(FXCollections.observableArrayList(
            "Electronics", "Clothing", "Food & Beverage", "Books", 
            "Home & Garden", "Sports", "Automotive", "Other"
        ));
    }
    
    private void setupEventHandlers() {
        // Table selection handler
        itemsTable.setOnMouseClicked(this::handleTableSelection);
        
        // Button handlers
        addButton.setOnAction(e -> handleAddItem());
        updateButton.setOnAction(e -> handleUpdateItem());
        deleteButton.setOnAction(e -> handleDeleteItem());
        clearButton.setOnAction(e -> handleClearForm());
        searchButton.setOnAction(e -> handleSearchItems());
        
        // Search field handler
        searchField.setOnAction(e -> handleSearchItems());
    }
    
    @FXML
    private void handleTableSelection(MouseEvent event) {
        selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            populateForm(selectedItem);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
    
    @FXML
    private void handleAddItem() {
        if (!validateForm()) {
            return;
        }
        
        InventoryItem newItem = createItemFromForm();
        
        if (inventoryDAO.addItem(newItem)) {
            showStatus("Item added successfully!", "success");
            loadAllItems();
            handleClearForm();
        } else {
            showStatus("Failed to add item!", "error");
        }
    }
    
    @FXML
    private void handleUpdateItem() {
        if (selectedItem == null) {
            showStatus("Please select an item to update!", "error");
            return;
        }
        
        if (!validateForm()) {
            return;
        }
        
        // Update the selected item with new values
        updateItemFromForm(selectedItem);
        
        if (inventoryDAO.updateItem(selectedItem)) {
            showStatus("Item updated successfully!", "success");
            loadAllItems();
            handleClearForm();
        } else {
            showStatus("Failed to update item!", "error");
        }
    }
    
    @FXML
    private void handleDeleteItem() {
        if (selectedItem == null) {
            showStatus("Please select an item to delete!", "error");
            return;
        }
        
        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Item: " + selectedItem.getName());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (inventoryDAO.deleteItem(selectedItem.getId())) {
                showStatus("Item deleted successfully!", "success");
                loadAllItems();
                handleClearForm();
            } else {
                showStatus("Failed to delete item!", "error");
            }
        }
    }
    
    @FXML
    private void handleClearForm() {
        nameField.clear();
        descriptionArea.clear();
        categoryCombo.setValue(null);
        quantityField.clear();
        priceField.clear();
        supplierField.clear();
        
        selectedItem = null;
        itemsTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        
        showStatus("Form cleared.", "info");
    }
    
    @FXML
    private void handleSearchItems() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadAllItems();
        } else {
            itemsList.clear();
            itemsList.addAll(inventoryDAO.searchItemsByName(searchTerm));
            showStatus("Search completed. Found " + itemsList.size() + " items.", "info");
        }
    }
    
    private void loadAllItems() {
        itemsList.clear();
        itemsList.addAll(inventoryDAO.getAllItems());
        showStatus("Loaded " + itemsList.size() + " items.", "info");
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (nameField.getText().trim().isEmpty()) {
            errors.append("Name is required.\n");
        }
        
        if (categoryCombo.getValue() == null) {
            errors.append("Category is required.\n");
        }
        
        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity < 0) {
                errors.append("Quantity must be non-negative.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid quantity format.\n");
        }
        
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                errors.append("Price must be non-negative.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid price format.\n");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please fix the following errors:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    private InventoryItem createItemFromForm() {
        InventoryItem item = new InventoryItem();
        item.setName(nameField.getText().trim());
        item.setDescription(descriptionArea.getText().trim());
        item.setCategory(categoryCombo.getValue());
        item.setQuantity(Integer.parseInt(quantityField.getText().trim()));
        item.setPrice(Double.parseDouble(priceField.getText().trim()));
        item.setSupplier(supplierField.getText().trim());
        return item;
    }
    
    private void updateItemFromForm(InventoryItem item) {
        item.setName(nameField.getText().trim());
        item.setDescription(descriptionArea.getText().trim());
        item.setCategory(categoryCombo.getValue());
        item.setQuantity(Integer.parseInt(quantityField.getText().trim()));
        item.setPrice(Double.parseDouble(priceField.getText().trim()));
        item.setSupplier(supplierField.getText().trim());
    }
    
    private void populateForm(InventoryItem item) {
        nameField.setText(item.getName());
        descriptionArea.setText(item.getDescription());
        categoryCombo.setValue(item.getCategory());
        quantityField.setText(String.valueOf(item.getQuantity()));
        priceField.setText(String.valueOf(item.getPrice()));
        supplierField.setText(item.getSupplier());
    }
    
    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        
        // Remove existing style classes
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        
        // Add appropriate style class
        switch (type) {
            case "success" -> statusLabel.getStyleClass().add("status-success");
            case "error" -> statusLabel.getStyleClass().add("status-error");
            default -> statusLabel.getStyleClass().add("status-info");
        }
        
        // Clear status after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> statusLabel.setText(""));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}