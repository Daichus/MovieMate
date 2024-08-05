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

public class TheaterShowtimeQueryModule extends JPanel {
  private DefaultTableModel tableModel;
  private JTable table;
  private List<Map<String, String>> showtimeData;
  private String filePath = "expanded_movie_data_test.csv";

  public TheaterShowtimeQueryModule(JFrame parentFrame) {
    setLayout(new BorderLayout());

    // 添加表格显示电影放映场次
    tableModel = new DefaultTableModel();
    tableModel.addColumn("Movie Name");
    tableModel.addColumn("Theater Address");
    tableModel.addColumn("Show Time");
    tableModel.addColumn("Price");
    tableModel.addColumn("Date");

    table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    // 添加筛选面板
    JPanel filterPanel = new JPanel(new FlowLayout());

    JTextField movieNameField = new JTextField(10);
    movieNameField.setToolTipText("Enter Movie Name");
    filterPanel.add(new JLabel("Movie Name:"));
    filterPanel.add(movieNameField);

    JTextField dateField = new JTextField(10);
    dateField.setToolTipText("Enter Date (YYYY-MM-DD)");
    filterPanel.add(new JLabel("Date:"));
    filterPanel.add(dateField);

    JTextField timeField = new JTextField(10);
    timeField.setToolTipText("Enter Time (HH:MM)");
    filterPanel.add(new JLabel("Time:"));
    filterPanel.add(timeField);

    JTextField priceField = new JTextField(10);
    priceField.setToolTipText("Enter Max Price");
    filterPanel.add(new JLabel("Max Price:"));
    filterPanel.add(priceField);

    JTextField locationField = new JTextField(10);
    locationField.setToolTipText("Enter Location");
    filterPanel.add(new JLabel("Location:"));
    filterPanel.add(locationField);

    JButton filterButton = new JButton("Filter");
    filterPanel.add(filterButton);

    // 添加前往订票的按钮
    JButton bookingButton = new JButton("Book Ticket");
    filterPanel.add(bookingButton);

    add(filterPanel, BorderLayout.NORTH);

    // 添加返回按钮
    JButton backButton = new JButton("Back to Movie Query Module");
    filterPanel.add(backButton);

    // 读取放映数据
    loadShowtimeData();

    // 添加筛选功能
    filterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String movieName = movieNameField.getText().trim().toLowerCase();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String price = priceField.getText().trim();
        String location = locationField.getText().trim().toLowerCase();

        filterShowtimes(movieName, date, time, price, location);
      }
    });

    // 返回按钮点击事件：切换回电影查询模块
    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new MovieQueryGUI());
        parentFrame.revalidate();
        parentFrame.repaint();
      }
    });

    // 前往订票按钮点击事件：切换到订票页面
    bookingButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new MovieTicketBooking(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
      }
    });
  }

  // 读取放映数据
  private void loadShowtimeData() {
    showtimeData = new ArrayList<>();
    try {
      List<CSVRecord> records = CSVParser.parse(Files.newBufferedReader(Paths.get(filePath)),
              CSVFormat.DEFAULT.withFirstRecordAsHeader()).getRecords();

      for (CSVRecord record : records) {
        Map<String, String> showtime = new HashMap<>();
        showtime.put("Movie Name", record.get("Movie Name"));
        showtime.put("Theater Address", record.get("Theater Address"));
        showtime.put("Show Time", record.get("Show Time"));
        showtime.put("Price", String.format("%.2f", 100 + Math.random() * 50)); // 随机生成一个价格
        showtime.put("Date", record.get("Release Date"));

        showtimeData.add(showtime);
      }

      updateTable(showtimeData);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 更新表格数据
  private void updateTable(List<Map<String, String>> data) {
    tableModel.setRowCount(0); // 清空表格
    for (Map<String, String> showtime : data) {
      tableModel.addRow(new Object[]{
              showtime.get("Movie Name"),
              showtime.get("Theater Address"),
              showtime.get("Show Time"),
              showtime.get("Price"),
              showtime.get("Date")
      });
    }
  }

  // 根据筛选条件过滤放映场次
  private void filterShowtimes(String movieName, String date, String time, String price, String location) {
    List<Map<String, String>> filteredData = new ArrayList<>();
    for (Map<String, String> showtime : showtimeData) {
      boolean matches = (movieName.isEmpty() || showtime.get("Movie Name").toLowerCase().contains(movieName)) &&
              (date.isEmpty() || showtime.get("Date").equals(date)) &&
              (time.isEmpty() || showtime.get("Show Time").startsWith(time)) &&
              (price.isEmpty() || Double.parseDouble(showtime.get("Price")) <= Double.parseDouble(price)) &&
              (location.isEmpty() || showtime.get("Theater Address").toLowerCase().contains(location));

      if (matches) {
        filteredData.add(showtime);
      }
    }
    updateTable(filteredData); // 显示过滤后的数据
  }
}
