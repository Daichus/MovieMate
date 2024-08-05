package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class MovieTicketBooking extends JPanel {
  private JComboBox<String> theaterComboBox;
  private JComboBox<String> movieComboBox;
  private JComboBox<String> dateComboBox;
  private JComboBox<String> timeComboBox;
  private JComboBox<Integer> seatComboBox;
  private JButton confirmButton;
  private List<Map<String, String>> showtimeData;
  private String filePath = "expanded_movie_data_test.csv";

  public MovieTicketBooking(JFrame parentFrame) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // 读取放映数据
    loadShowtimeData();

    // 影厅选择
    theaterComboBox = new JComboBox<>(getUniqueValues("Theater Address").toArray(new String[0]));
    add(createLabeledComponent("Select Theater:", theaterComboBox));

    // 电影选择
    movieComboBox = new JComboBox<>(getUniqueValues("Movie Name").toArray(new String[0]));
    add(createLabeledComponent("Select Movie:", movieComboBox));

    // 日期选择
    dateComboBox = new JComboBox<>(getUniqueValues("Date").toArray(new String[0]));
    add(createLabeledComponent("Select Date:", dateComboBox));

    // 场次选择
    timeComboBox = new JComboBox<>(getUniqueValues("Show Time").toArray(new String[0]));
    add(createLabeledComponent("Select Show Time:", timeComboBox));

    // 座位选择
    seatComboBox = new JComboBox<>(generateSeatNumbers());
    add(createLabeledComponent("Select Seat:", seatComboBox));

    // 确认按钮
    confirmButton = new JButton("Confirm Booking");
    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        confirmBooking(parentFrame);
      }
    });
    add(confirmButton);

    // 设置布局间距
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 获取唯一值的列表（去重）
  private Set<String> getUniqueValues(String key) {
    Set<String> uniqueValues = new HashSet<>();
    for (Map<String, String> showtime : showtimeData) {
      uniqueValues.add(showtime.get(key));
    }
    return uniqueValues;
  }

  // 生成座位编号（1~100）
  private Integer[] generateSeatNumbers() {
    Integer[] seats = new Integer[100];
    for (int i = 0; i < seats.length; i++) {
      seats[i] = i + 1;
    }
    return seats;
  }

  // 创建带标签的组件
  private JPanel createLabeledComponent(String labelText, JComponent component) {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel(labelText);
    panel.add(label);
    panel.add(component);
    return panel;
  }

  // 确认订单并跳转到付款页面
  private void confirmBooking(JFrame parentFrame) {
    String selectedTheater = (String) theaterComboBox.getSelectedItem();
    String selectedMovie = (String) movieComboBox.getSelectedItem();
    String selectedDate = (String) dateComboBox.getSelectedItem();
    String selectedTime = (String) timeComboBox.getSelectedItem();
    Integer selectedSeat = (Integer) seatComboBox.getSelectedItem();

    // 生成訂票資訊
    String bookingInfo = "電影名稱: " + selectedMovie + "\n" +
            "影廳地址: " + selectedTheater + "\n" +
            "放映時間: " + selectedTime + "\n" +
            "座位: " + selectedSeat + "\n" +
            "日期: " + selectedDate;

    JOptionPane.showMessageDialog(this, "Booking Confirmed:\n" + bookingInfo);

    // 跳转到付款页面，傳遞訂票資訊
    parentFrame.getContentPane().removeAll();
    parentFrame.add(new PaymentPage(parentFrame, bookingInfo));
    parentFrame.revalidate();
    parentFrame.repaint();
  }
}
