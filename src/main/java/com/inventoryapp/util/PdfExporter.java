package com.inventoryapp.util;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PdfExporter {
    public static void export(String filePath, List<Map<String, Object>> data) throws IOException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        document.add(new Paragraph("Inventory Stock Report").setBold());

        // Table
        float[] columnWidths = {50, 150, 100, 100};
        Table table = new Table(columnWidths);

        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Quantity");
        table.addCell("Price");

        for (Map<String, Object> row : data) {
            table.addCell(row.get("id").toString());
            table.addCell(row.get("name").toString());
            table.addCell(row.get("quantity").toString());
            table.addCell(row.get("price").toString());
        }

        document.add(table);
        document.close();
    }
}
