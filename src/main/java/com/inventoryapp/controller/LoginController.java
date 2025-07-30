package com.inventoryapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginStatusLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // TODO: Implement actual authentication logic here
        if (username.isEmpty() || password.isEmpty()) {
            loginStatusLabel.setText("Please enter username and password.");
        } else {
            loginStatusLabel.setText(""); // Clear error
            // Example: loginStatusLabel.setText("Login successful!");
        }
    }
}
