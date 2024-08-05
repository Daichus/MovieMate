package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class MovieQueryGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String userLocation;
    private List<Map<String, String>> movieData;

    public MovieQueryGUI() {
        setTitle("Movie Query Module");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

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
        tableModel.addColumn("Show Time");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Separate search fields
        JTextField nameField = new JTextField(15);
        nameField.setToolTipText("Search by Movie Name");
        searchPanel.add(new JLabel("Movie Name:"));
        searchPanel.add(nameField);

        JTextField genreField = new JTextField(15);
        genreField.setToolTipText("Search by Genre");
        searchPanel.add(new JLabel("Genre:"));
        searchPanel.add(genreField);

        JTextField castField = new JTextField(15);
        castField.setToolTipText("Search by Cast");
        searchPanel.add(new JLabel("Cast:"));
        searchPanel.add(castField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        // 添加切换到影厅与场次查询模块的按钮
        JButton switchModuleButton = new JButton("Switch to Theater and Showtime Query Module");
        searchPanel.add(switchModuleButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // 添加主面板到窗口
        add(mainPanel);

        // 随机生成用户位置
        userLocation = generateRandomLocation();

        // Load data from CSV and sort by proximity to user
        loadAndSortCSVData("untitled/movieInfoFile/expanded_movie_data_test.csv");  // Replace with the actual file path

        // Add search functionality
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameQuery = nameField.getText().trim().toLowerCase();
                String genreQuery = genreField.getText().trim().toLowerCase();
                String castQuery = castField.getText().trim().toLowerCase();
                searchMovies(nameQuery, genreQuery, castQuery);
            }
        });

        // 添加切换模块的功能
        switchModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToTheaterShowtimeQueryModule();
            }
        });

        setVisible(true);
    }

    // 随机生成用户位置 (从台北到屏东)
    private String generateRandomLocation() {
        List<String> locations = Arrays.asList("台北", "新北", "桃園", "台中", "台南", "高雄", "基隆", "新竹", "嘉義", "屏東");
        Random random = new Random();
        String location = locations.get(random.nextInt(locations.size()));
        System.out.println("User Location: " + location); // 输出用户位置，供调试使用
        return location;
    }

    // 根据用户位置，加载并排序CSV数据
    private void loadAndSortCSVData(String filePath) {
        try {
            // 解析CSV文件
            List<CSVRecord> records = CSVParser.parse(Files.newBufferedReader(Paths.get(filePath)),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader()).getRecords();

            // 将CSV记录转换为带影厅地址的列表
            movieData = new ArrayList<>();
            for (CSVRecord record : records) {
                Map<String, String> movie = new HashMap<>();
                movie.put("Movie Name", record.get("Movie Name"));
                movie.put("Genre", record.get("Genre"));
                movie.put("Plot Summary", record.get("Plot Summary"));
                movie.put("Release Date", record.get("Release Date"));
                movie.put("Cast", record.get("Cast"));
                movie.put("Director", record.get("Director"));
                movie.put("Duration", record.get("Duration"));
                movie.put("Language", record.get("Language"));
                movie.put("Theater Address", record.get("Theater Address"));
                movie.put("Show Time", record.get("Show Time"));
                movieData.add(movie);
            }

            // 根据用户位置对电影数据进行排序
            sortMovieData(movieData);

            // 将排序后的数据添加到表格模型中
            updateTable(movieData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更新表格数据
    private void updateTable(List<Map<String, String>> data) {
        tableModel.setRowCount(0); // 清空表格
        for (Map<String, String> movie : data) {
            tableModel.addRow(new Object[]{
                    movie.get("Movie Name"),
                    movie.get("Genre"),
                    movie.get("Plot Summary"),
                    movie.get("Release Date"),
                    movie.get("Cast"),
                    movie.get("Director"),
                    movie.get("Duration"),
                    movie.get("Language"),
                    movie.get("Theater Address"),
                    movie.get("Show Time")
            });
        }
    }

    // 搜索电影
    private void searchMovies(String nameQuery, String genreQuery, String castQuery) {
        List<Map<String, String>> filteredData = new ArrayList<>();
        for (Map<String, String> movie : movieData) {
            boolean matches = (nameQuery.isEmpty() || movie.get("Movie Name").toLowerCase().contains(nameQuery)) &&
                    (genreQuery.isEmpty() || movie.get("Genre").toLowerCase().contains(genreQuery)) &&
                    (castQuery.isEmpty() || movie.get("Cast").toLowerCase().contains(castQuery));
            if (matches) {
                filteredData.add(movie);
            }
        }
        sortMovieData(filteredData);
        updateTable(filteredData); // 显示过滤后的数据
    }

    // 根据影厅地址和时间排序电影数据
    private void sortMovieData(List<Map<String, String>> data) {
        data.sort((m1, m2) -> {
            String address1 = m1.get("Theater Address");
            String address2 = m2.get("Theater Address");
            int addressComparison = getLocationPriority(address1) - getLocationPriority(address2);
            if (addressComparison != 0) {
                return addressComparison;
            } else {
                // 如果地址相同，则根据时间排序
                String time1 = m1.get("Show Time");
                String time2 = m2.get("Show Time");
                return time1.compareTo(time2);
            }
        });
    }

    // 根据影厅地址获取与用户位置的距离优先级（优先级值越小表示越接近用户）
    private int getLocationPriority(String theaterAddress) {
        Map<String, Integer> locationPriority = new HashMap<>();
        locationPriority.put("台北", 1);
        locationPriority.put("新北", 2);
        locationPriority.put("桃園", 3);
        locationPriority.put("台中", 4);
        locationPriority.put("台南", 5);
        locationPriority.put("高雄", 6);
        locationPriority.put("基隆", 7);
        locationPriority.put("新竹", 8);
        locationPriority.put("嘉義", 9);
        locationPriority.put("屏東", 10);

        for (String location : locationPriority.keySet()) {
            if (theaterAddress.contains(location)) {
                return Math.abs(locationPriority.get(location) - locationPriority.get(userLocation));
            }
        }
        // 如果地址不匹配任何已知位置，默认返回最大优先级
        return Integer.MAX_VALUE;
    }

    // 切换到影厅与场次查询模块
    private void switchToTheaterShowtimeQueryModule() {
        getContentPane().removeAll();
        add(new TheaterShowtimeQueryModule(this));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieQueryGUI());
    }
}
