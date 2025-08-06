package com.inventoryapp.service;

// import com.inventoryapp.util.CsvExporter;
import com.inventoryapp.util.PdfExporter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportExportService {

    private final InventoryService dbService = new InventoryService();

    public String generatePreview(String type, LocalDate startDate, LocalDate endDate, String category, boolean includeOutOfStock) {
        List<Map<String, Object>> data = dbService.getStockData(type, startDate, endDate, category, includeOutOfStock);

        StringBuilder preview = new StringBuilder();
        preview.append(String.format("%-5s %-20s %-10s %-10s\n", "ID", "Name", "Qty", "Price"));
        preview.append("---------------------------------------------------\n");

        for (Map<String, Object> row : data) {
            preview.append(String.format("%-5s %-20s %-10s %-10s\n",
                    row.get("id"), row.get("name"), row.get("quantity"), row.get("price")));
        }
        return preview.toString();
    }

    // public void exportAsCsv(String filePath) throws IOException {
    //     List<Map<String, Object>> data = dbService.getStockData("Full Inventory", null, null, "All", true);
    //     CsvExporter.export(filePath, data);
    // }

    public void exportAsPdf(String filePath) throws IOException {
        List<Map<String, Object>> data = dbService.getStockData("Full Inventory", null, null, "All", true);
        PdfExporter.export(filePath, data);
    }
}
