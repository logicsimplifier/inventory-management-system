package com.inventoryapp.service;

import com.inventoryapp.dao.InventoryDAO;
import com.inventoryapp.model.InventoryItem;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public InventoryService(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

   public List<Map<String, Object>> getStockData(String type, LocalDate startDate, LocalDate endDate, String category, boolean includeOutOfStock) {
    List<InventoryItem> items = inventoryDAO.getAllItems();

    // Filter by category
    if (!"All".equalsIgnoreCase(category)) {
        items = items.stream()
                .filter(item -> category.equalsIgnoreCase(item.getCategory()))
                .collect(Collectors.toList());
    }

    // Filter out of stock
    if (!includeOutOfStock) {
        items = items.stream()
                .filter(item -> item.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    // Filter low stock
    if ("Low Stock".equalsIgnoreCase(type)) {
        items = items.stream()
                .filter(item -> item.getQuantity() < 5)
                .collect(Collectors.toList());
    }

    // Filter by date range
    if ("Custom Date Range".equalsIgnoreCase(type) && startDate != null && endDate != null) {
        items = items.stream()
                .filter(item -> item.getAddedDate() != null &&
                        !item.getAddedDate().isBefore(startDate) &&
                        !item.getAddedDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Convert to Map format
    return items.stream()
        .map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("quantity", item.getQuantity());
            map.put("price", item.getPrice());
            map.put("addedDate", item.getAddedDate()); // can be null
            return map;
        })
        .collect(Collectors.toList());
}



    // ================== EXISTING METHODS ==================

    public ServiceResult<InventoryItem> addItem(InventoryItem item) {
        ValidationResult validation = validateItem(item);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }

        if (isNameExists(item.getName(), item.getId())) {
            return ServiceResult.failure("An item with this name already exists.");
        }

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

    public ServiceResult<InventoryItem> updateItem(InventoryItem item) {
        if (inventoryDAO.getItemById(item.getId()) == null) {
            return ServiceResult.failure("Item not found.");
        }

        ValidationResult validation = validateItem(item);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }

        if (isNameExists(item.getName(), item.getId())) {
            return ServiceResult.failure("Another item with this name already exists.");
        }

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

    public ServiceResult<Void> deleteItem(int itemId) {
        InventoryItem existingItem = inventoryDAO.getItemById(itemId);
        if (existingItem == null) {
            return ServiceResult.failure("Item not found.");
        }

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

    public ServiceResult<List<InventoryItem>> getAllItems() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllItems();
            return ServiceResult.success(items, "Retrieved " + items.size() + " items.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

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

    public ServiceResult<List<InventoryItem>> getLowStockItems(int threshold) {
        try {
            List<InventoryItem> allItems = inventoryDAO.getAllItems();
            List<InventoryItem> lowStockItems = allItems.stream()
                    .filter(item -> item.getQuantity() <= threshold)
                    .collect(Collectors.toList());

            return ServiceResult.success(lowStockItems, "Found " + lowStockItems.size() + " items with low stock.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

    public ServiceResult<List<InventoryItem>> getItemsByCategory(String category) {
        try {
            List<InventoryItem> allItems = inventoryDAO.getAllItems();
            List<InventoryItem> categoryItems = allItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .collect(Collectors.toList());

            return ServiceResult.success(categoryItems, "Found " + categoryItems.size() + " items in category: " + category);
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

    public ServiceResult<Double> calculateTotalValue() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllItems();
            double totalValue = items.stream()
                    .mapToDouble(item -> item.getQuantity() * item.getPrice())
                    .sum();

            return ServiceResult.success(totalValue, "Total inventory value calculated.");
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

    // Private helper methods
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

    private boolean isNameExists(String name, int excludeId) {
        List<InventoryItem> allItems = inventoryDAO.getAllItems();
        return allItems.stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(name.trim())
                        && item.getId() != excludeId);
    }

    private void applyBusinessRules(InventoryItem item) {
        item.setName(item.getName().trim());

        if (item.getDescription() != null) {
            item.setDescription(item.getDescription().trim());
        }

        if (item.getSupplier() != null) {
            item.setSupplier(item.getSupplier().trim());
        }

        item.setPrice(Math.round(item.getPrice() * 100.0) / 100.0);

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
