package com.inventoryapp.dao;

import com.inventoryapp.util.DBUtils;
import com.inventoryapp.model.InventoryItem;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Inventory Items
 * Handles all database operations for inventory items
 */
public class InventoryDAO {
    
    // Add new inventory item
    public boolean addItem(InventoryItem item) {
        String sql = """
            INSERT INTO inventory_items (name, description, category, quantity, price, supplier, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setDouble(5, item.getPrice());
            pstmt.setString(6, item.getSupplier());
            pstmt.setString(7, DBUtils.formatDateTime(item.getCreatedAt()));
            pstmt.setString(8, DBUtils.formatDateTime(item.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
        }
        
        return false;
    }
    
    // Update existing inventory item
    public boolean updateItem(InventoryItem item) {
        String sql = """
            UPDATE inventory_items 
            SET name = ?, description = ?, category = ?, quantity = ?, price = ?, supplier = ?, updated_at = ?
            WHERE id = ?
        """;
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            item.setUpdatedAt(LocalDateTime.now());
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setDouble(5, item.getPrice());
            pstmt.setString(6, item.getSupplier());
            pstmt.setString(7, DBUtils.formatDateTime(item.getUpdatedAt()));
            pstmt.setInt(8, item.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
        }
        
        return false;
    }
    
    // Delete inventory item
    public boolean deleteItem(int id) {
        String sql = "DELETE FROM inventory_items WHERE id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting item: " + e.getMessage());
        }
        
        return false;
    }
    
    // Get item by ID
    public InventoryItem getItemById(int id) {
        String sql = "SELECT * FROM inventory_items WHERE id = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToItem(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting item by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all inventory items
    public List<InventoryItem> getAllItems() {
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM inventory_items ORDER BY name";
        
        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all items: " + e.getMessage());
        }
        
        return items;
    }
    
    // Search items by name
    public List<InventoryItem> searchItemsByName(String name) {
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM inventory_items WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching items: " + e.getMessage());
        }
        
        return items;
    }
    
    // Helper method to map ResultSet to InventoryItem
    private InventoryItem mapResultSetToItem(ResultSet rs) throws SQLException {
        InventoryItem item = new InventoryItem();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setCategory(rs.getString("category"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getDouble("price"));
        item.setSupplier(rs.getString("supplier"));
        item.setCreatedAt(DBUtils.parseDateTime(rs.getString("created_at")));
        item.setUpdatedAt(DBUtils.parseDateTime(rs.getString("updated_at")));
        return item;
    }
}