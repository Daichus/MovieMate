package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MovieQueryGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public MovieQueryGUI() {
        setTitle("Movie Query Module");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Movie Name");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Plot Summary");
        tableModel.addColumn("Release Date");
        tableModel.addColumn("Cast");
        tableModel.addColumn("Director");
        tableModel.addColumn("Duration");
        tableModel.addColumn("Language");
        tableModel.addColumn("Theater Address");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from CSV
        loadCSVData("untitled/movieInfoFile/updated_movie_data_test.csv");  // Replace with the actual file path

        setVisible(true);
    }

    private void loadCSVData(String filePath) {
        try {
            List<CSVRecord> records = CSVParser.parse(Files.newBufferedReader(Paths.get(filePath)),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader()).getRecords();

            for (CSVRecord record : records) {
                tableModel.addRow(new Object[]{
                        record.get("Movie Name"),
                        record.get("Genre"),
                        record.get("Plot Summary"),
                        record.get("Release Date"),
                        record.get("Cast"),
                        record.get("Director"),
                        record.get("Duration"),
                        record.get("Language"),
                        record.get("Theater Address")
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieQueryGUI());
    }
}
