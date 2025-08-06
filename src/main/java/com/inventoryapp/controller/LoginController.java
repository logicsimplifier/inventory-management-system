package com.inventoryapp.controller;

import com.inventoryapp.util.DBUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label loginStatusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the role ComboBox with available roles
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Staff"));
        roleComboBox.getSelectionModel().selectFirst(); // Default to "Admin"
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
    
        if (username.isEmpty() || password.isEmpty()) {
            loginStatusLabel.setText("Please enter username and password.");
            return;
        }
        
        if (selectedRole == null || selectedRole.isEmpty()) {
            loginStatusLabel.setText("Please select a role.");
            return;
        } else {
            loginStatusLabel.setText(""); // Clear error
        }

        try {
            Connection conn = DBUtils.getConnection(); //connect to SQLite DB
            String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, selectedRole.toUpperCase()); 

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                String userRole = rs.getString("role");
                loginStatusLabel.setText("Login successful!");
                
                // Navigate to appropriate dashboard based on role
                try {
                    String fxmlPath;
                    String windowTitle;
                    
                    if ("ADMIN".equals(userRole)) {
                        fxmlPath = "/fxml/view/dashboard_admin.fxml";
                        windowTitle = "Inventory Management - Admin Dashboard";
                    } else {
                        fxmlPath = "/fxml/view/dashboard_staff.fxml";
                        windowTitle = "Inventory Management - Staff Dashboard";
                    }
                    
                    // Load dashboard FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                    Parent dashboardRoot = loader.load();
                    
                    // Create new scene
                    Scene dashboardScene = new Scene(dashboardRoot);
                    
                    // Get current stage
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    
                    // Set new scene and update window title
                    currentStage.setScene(dashboardScene);
                    currentStage.setTitle(windowTitle);
                    currentStage.centerOnScreen();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    loginStatusLabel.setText("Error loading dashboard: " + e.getMessage());
                }
            } else {
                loginStatusLabel.setText("Invalid username, password, or role.");
            }

            rs.close();
            stmt.close();
            conn.close();  
            
        } catch (Exception e) {
            e.printStackTrace();
            loginStatusLabel.setText("DB Error:"+ e.getMessage());
        }
     }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load register FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view/register.fxml"));
            Parent registerRoot = loader.load();
            
            // Create new scene
            Scene registerScene = new Scene(registerRoot);
            
            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set new scene and update window title
            currentStage.setScene(registerScene);
            currentStage.setTitle("Inventory Management - Register");
            currentStage.centerOnScreen();
            
        } catch (IOException e) {
            e.printStackTrace();
            loginStatusLabel.setText("Error loading registration page: " + e.getMessage());
        }
    }
}
