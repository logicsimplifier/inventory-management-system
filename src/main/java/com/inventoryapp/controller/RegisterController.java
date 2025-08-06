package com.inventoryapp.controller;

import com.inventoryapp.util.DBUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField, secretCodeField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label statusLabel;

    // secret code
    private static final String SECRET_CODE = "InventoryApp2025";

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin"));
        roleComboBox.getSelectionModel().select("Admin");
        roleComboBox.setDisable(true);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // String phone = phoneField.getText();
        String role = "Admin"; // Hardcoded to prevent role manipulation
        String secretCode = secretCodeField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || secretCode.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!secretCode.equals(SECRET_CODE)) {
            statusLabel.setText("Invalid secret code.");
            return;
        }

        // Save to database
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); //Consider hashing in future
            // stmt.setString(3, phone);
            stmt.setString(3, role.toUpperCase());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                statusLabel.setText("Admin registered successfully!");
                clearForm();
            } else {
                statusLabel.setText("Failed to register. Try again.");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("DB Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        // phoneField.clear();
        secretCodeField.clear();
        roleComboBox.getSelectionModel().select("Admin");
    }
    @FXML
    private void handleBackToLogin(ActionEvent event) {
    try {
        // Load the login FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/login.fxml"));
        Parent loginRoot = loader.load();

        // Get the current scene and set the new root
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.setScene(new Scene(loginRoot));
        stage.setTitle("Login");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        statusLabel.setText("Error loading login screen.");
    }
}

}
