package com.inventoryapp.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Model class for report data
 * Location: src/main/java/com/inventory/model/ReportData.java
 */
public class ReportData {
    
    private String title;
    private LocalDateTime generatedDate;
    private List<InventoryItem> items;
    private Map<String, Object> summary;
    private String reportType;
    private Map<String, Object> filters;
    
    // Constructors
    public ReportData() {}
    
    public ReportData(String title, LocalDateTime generatedDate, List<InventoryItem> items) {
        this.title = title;
        this.generatedDate = generatedDate;
        this.items = items;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }
    
    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }
    
    public List<InventoryItem> getItems() {
        return items;
    }
    
    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
    
    public Map<String, Object> getSummary() {
        return summary;
    }
    
    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }
    
    public String getReportType() {
        return reportType;
    }
    
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    public Map<String, Object> getFilters() {
        return filters;
    }
    
    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
    
    @Override
    public String toString() {
        return "ReportData{" +
                "title='" + title + '\'' +
                ", generatedDate=" + generatedDate +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                ", reportType='" + reportType + '\'' +
                '}';
    }
}