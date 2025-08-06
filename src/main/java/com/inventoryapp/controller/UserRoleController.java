package com.inventoryapp.controller;

//import required classes

import com.inventoryapp.model.UserRole;
import com.inventoryapp.service.UserRoleService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserRoleController {

    // Field Definitions (Connected to FXML)
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TableView<UserRole> userTable;

    @FXML
    private TableColumn<UserRole, String> usernameColumn;

    @FXML
    private TableColumn<UserRole, String> passwordColumn;

    @FXML
    private TableColumn<UserRole, String> roleColumn;

    private final UserRoleService userService = new UserRoleService();

    @FXML

//  initialize() method — Runs Automatically When Page Loads
    public void initialize() {
        // Initialize role options
        roleComboBox.getItems().addAll("Admin", "Staff");

        // Setup table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        //Loads the list of users from the database and displays them in the table.
        loadUsersFromDatabase();
    }

    //Runs When Add Button Clicked
    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();


        //Reads input values from the UI fields.
        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Validation Error", "Fields are empty! All fields must be filled.");
            return;
        }

        UserRole newUser = new UserRole(username, password, role);
        userService.addUser(newUser);
        showAlert("Success", "User added successfully.");
        clearFields();
        loadUsersFromDatabase();
    }

    @FXML
    private void handleDeleteUser() {
        UserRole selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.deleteUser(selectedUser);
            showAlert("Deleted", "User deleted successfully.");
            loadUsersFromDatabase();

        } else {
            showAlert("Selection Error", "Please select a user to delete.");
        }
    }

     private void loadUsersFromDatabase() {
        userTable.setItems(userService.getUsers());
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

//showAlert method    
   private void showAlert(String title, String message) {
    //Creates a new JavaFX Alert dialog.
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }
}