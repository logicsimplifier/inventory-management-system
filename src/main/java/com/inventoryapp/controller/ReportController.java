package com.inventoryapp.controller;

import com.inventoryapp.service.ReportExportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class ReportController {

    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private CheckBox includeOutOfStockCheck;
    @FXML private Button generateReportBtn;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;
    @FXML private TextArea reportPreviewArea;
    @FXML private Button exportCSVBtn;
    @FXML private Button exportPDFBtn;
    @FXML private Label exportPathLabel;

    private final ReportExportService reportService = new ReportExportService();
    private String generatedReportPreview = "";

    @FXML
    public void initialize() {
        reportTypeCombo.getItems().addAll("Full Inventory", "Low Stock", "Custom Date Range");
        categoryCombo.getItems().addAll("All", "Electronics", "Groceries", "Clothing", "Other");

        progressIndicator.setVisible(false);

        generateReportBtn.setOnAction(e -> generateReport());
        exportCSVBtn.setOnAction(e -> exportReport("CSV"));
        exportPDFBtn.setOnAction(e -> exportReport("PDF"));
    }

    private void generateReport() {
        progressIndicator.setVisible(true);
        statusLabel.setText("Generating report...");

        String type = reportTypeCombo.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String category = categoryCombo.getValue();
        boolean includeOutOfStock = includeOutOfStockCheck.isSelected();

        generatedReportPreview = reportService.generatePreview(type, startDate, endDate, category, includeOutOfStock);
        reportPreviewArea.setText(generatedReportPreview);

        progressIndicator.setVisible(false);
        statusLabel.setText("Report generated successfully.");
    }

    private void exportReport(String format) {
        if (generatedReportPreview.isEmpty()) {
            statusLabel.setText("Please generate a report first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(format + " Files", "*." + format.toLowerCase())
        );
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                if ("PDF".equals(format)) {
                     reportService.exportAsPdf(file.getAbsolutePath());
                } else {
                    reportService.exportAsPdf(file.getAbsolutePath());
                }
                exportPathLabel.setText("Exported: " + file.getAbsolutePath());
                statusLabel.setText(format + " report exported successfully.");
            } catch (IOException e) {
                statusLabel.setText("Failed to export " + format + " report.");
                e.printStackTrace();
            }
        }
    }
}
