package main.java.com.inventoryapp.service;

import com.inventoryapp.dao.InventoryDAO;
import com.inventoryapp.model.InventoryItem;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Inventory Management
 * Contains business logic and validation rules
 * Acts as an intermediary between Controller and DAO layers
 */
public class InventoryService {
    
    private final InventoryDAO inventoryDAO;
    
    public InventoryService() {
        this.inventoryDAO = new InventoryDAO();
    }
    
    // Constructor for dependency injection (useful for testing)
    public InventoryService(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }
    
    /**
     * Add a new inventory item with business validation
     */
    public ServiceResult<InventoryItem> addItem(InventoryItem item) {
        // Business validation
        ValidationResult validation = validateItem(item);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }
        
        // Check for duplicate names
        if (isNameExists(item.getName(), item.getId())) {
            return ServiceResult.failure("An item with this name already exists.");
        }
        
        // Business logic - apply automatic categorization if needed
        applyBusinessRules(item);
        
        try {
            boolean success = inventoryDAO.addItem(item);
            if (success) {
                return ServiceResult.success(item, "Item added successfully.");
            } else {
                return ServiceResult.failure("Failed to add item to database.");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing inventory item
     */
    public ServiceResult<InventoryItem> updateItem(InventoryItem item) {
        // Validate item exists
        if (inventoryDAO.getItemById(item.getId()) == null) {
            return ServiceResult.failure("Item not found.");
        }
        
        // Business validation
        ValidationResult validation = validateItem(item);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }
        
        // Check for duplicate names (excluding current item)
        if (isNameExists(item.getName(), item.getId())) {
            return ServiceResult.failure("Another item with this name already exists.");
        }
        
        // Apply business rules
        applyBusinessRules(item);
        
        try {
            boolean success = inventoryDAO.updateItem(item);
            if (success) {
                return ServiceResult.success(item, "Item updated successfully.");
            } else {
                return ServiceResult.failure("Failed to update item in database.");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Delete an inventory item
     */
    public ServiceResult<Void> deleteItem(int itemId) {
        // Validate item exists
        InventoryItem existingItem = inventoryDAO.getItemById(itemId);
        if (existingItem == null) {
            return ServiceResult.failure("Item not found.");
        }
        
        // Business rule: Check if item can be deleted
        if (existingItem.getQuantity() > 0) {
            return ServiceResult.failure("Cannot delete item with remaining quantity. Please reduce quantity to 0 first.");
        }
        
        try {
            boolean success = inventoryDAO.deleteItem(itemId);
            if (success) {
                return ServiceResult.success(null, "Item deleted successfully.");
            } else {
                return ServiceResult.failure("Failed to delete item from database.");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Get item by ID
     */
    public ServiceResult<InventoryItem> getItemById(int id) {
        try {
            InventoryItem item = inventoryDAO.getItemById(id);
            if (item != null) {
                return ServiceResult.success(item, "Item retrieved successfully.");
            } else {
                return ServiceResult.failure("Item not found.");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Get all inventory items
     */
    public ServiceResult<List<InventoryItem>> getAllItems() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllItems();
            return ServiceResult.success(items, "Retrieved " + items.size() + " items.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Search items by name
     */
    public ServiceResult<List<InventoryItem>> searchItems(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllItems();
        }
        
        try {
            List<InventoryItem> items = inventoryDAO.searchItemsByName(searchTerm.trim());
            return ServiceResult.success(items, "Found " + items.size() + " matching items.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Get low stock items (quantity below threshold)
     */
    public ServiceResult<List<InventoryItem>> getLowStockItems(int threshold) {
        try {
            List<InventoryItem> allItems = inventoryDAO.getAllItems();
            List<InventoryItem> lowStockItems = allItems.stream()
                .filter(item -> item.getQuantity() <= threshold)
                .collect(Collectors.toList());
            
            return ServiceResult.success(lowStockItems, 
                "Found " + lowStockItems.size() + " items with low stock.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Get items by category
     */
    public ServiceResult<List<InventoryItem>> getItemsByCategory(String category) {
        try {
            List<InventoryItem> allItems = inventoryDAO.getAllItems();
            List<InventoryItem> categoryItems = allItems.stream()
                .filter(item -> category.equals(item.getCategory()))
                .collect(Collectors.toList());
            
            return ServiceResult.success(categoryItems, 
                "Found " + categoryItems.size() + " items in category: " + category);
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    /**
     * Calculate total inventory value
     */
    public ServiceResult<Double> calculateTotalValue() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllItems();
            double totalValue = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
            
            return ServiceResult.success(totalValue, 
                "Total inventory value calculated.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }
    
    // Private helper methods
    
    /**
     * Validate inventory item according to business rules
     */
    private ValidationResult validateItem(InventoryItem item) {
        if (item == null) {
            return ValidationResult.invalid("Item cannot be null.");
        }
        
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return ValidationResult.invalid("Item name is required.");
        }
        
        if (item.getName().trim().length() > 100) {
            return ValidationResult.invalid("Item name cannot exceed 100 characters.");
        }
        
        if (item.getCategory() == null || item.getCategory().trim().isEmpty()) {
            return ValidationResult.invalid("Category is required.");
        }
        
        if (item.getQuantity() < 0) {
            return ValidationResult.invalid("Quantity cannot be negative.");
        }
        
        if (item.getPrice() < 0) {
            return ValidationResult.invalid("Price cannot be negative.");
        }
        
        if (item.getPrice() > 999999.99) {
            return ValidationResult.invalid("Price cannot exceed $999,999.99.");
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Check if an item name already exists (excluding the specified item ID)
     */
    private boolean isNameExists(String name, int excludeId) {
        List<InventoryItem> allItems = inventoryDAO.getAllItems();
        return allItems.stream()
            .anyMatch(item -> item.getName().equalsIgnoreCase(name.trim()) 
                            && item.getId() != excludeId);
    }
    
    /**
     * Apply business rules to item (e.g., automatic categorization, price formatting)
     */
    private void applyBusinessRules(InventoryItem item) {
        // Trim and format name
        item.setName(item.getName().trim());
        
        // Trim description
        if (item.getDescription() != null) {
            item.setDescription(item.getDescription().trim());
        }
        
        // Trim supplier
        if (item.getSupplier() != null) {
            item.setSupplier(item.getSupplier().trim());
        }
        
        // Round price to 2 decimal places
        item.setPrice(Math.round(item.getPrice() * 100.0) / 100.0);
        
        // Auto-categorize based on name keywords (simple example)
        if (item.getCategory().equals("Other")) {
            String nameLower = item.getName().toLowerCase();
            if (nameLower.contains("laptop") || nameLower.contains("computer") || 
                nameLower.contains("phone") || nameLower.contains("tablet")) {
                item.setCategory("Electronics");
            } else if (nameLower.contains("book") || nameLower.contains("magazine")) {
                item.setCategory("Books");
            } else if (nameLower.contains("shirt") || nameLower.contains("pants") || 
                      nameLower.contains("dress")) {
                item.setCategory("Clothing");
            }
        }
    }
    
    // Inner classes for service results and validation
    
    /**
     * Represents the result of a service operation
     */
    public static class ServiceResult<T> {
        private final boolean success;
        private final T data;
        private final String message;
        
        private ServiceResult(boolean success, T data, String message) {
            this.success = success;
            this.data = data;
            this.message = message;
        }
        
        public static <T> ServiceResult<T> success(T data, String message) {
            return new ServiceResult<>(true, data, message);
        }
        
        public static <T> ServiceResult<T> failure(String message) {
            return new ServiceResult<>(false, null, message);
        }
        
        public boolean isSuccess() { return success; }
        public T getData() { return data; }
        public String getMessage() { return message; }
    }
    
    /**
     * Represents the result of validation
     */
    private static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
}