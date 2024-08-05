package org.example;

import javax.swing.*;

//public class PaymentPage extends JPanel {
//
//    public PaymentPage(JFrame parentFrame) {
//        // 创建一个简单的标签，表示这是付款页面
//        JLabel label = new JLabel("This is the Payment Page");
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        add(label);
//    }
//}

//package org.example;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentPage extends JPanel {
  private JComboBox<String> paymentMethodComboBox;
  private JButton payButton;
  private JLabel statusLabel;
  private JTextArea bookingInfoTextArea;
  private String bookingInfo;

  private static final String CSV_FILE = "bookings.csv";

  public PaymentPage(JFrame parentFrame, String bookingInfo) {
    this.bookingInfo = bookingInfo;

    // 設置面板佈局
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // 支付標題
    JLabel titleLabel = new JLabel("支付");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(titleLabel);

    add(Box.createRigidArea(new Dimension(0, 20)));

    // 支付方式選擇
    JLabel paymentMethodLabel = new JLabel("選擇支付方式:");
    paymentMethodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    add(paymentMethodLabel);

    paymentMethodComboBox = new JComboBox<>(new String[]{"信用卡", "PayPal", "Apple Pay"});
    paymentMethodComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, paymentMethodComboBox.getPreferredSize().height));
    add(paymentMethodComboBox);

    add(Box.createRigidArea(new Dimension(0, 20)));

    // 支付按鈕
    payButton = new JButton("支付");
    payButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    payButton.addActionListener(new PayAction());
    add(payButton);

    add(Box.createRigidArea(new Dimension(0, 20)));

    // 狀態標籤
    statusLabel = new JLabel("");
    statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(statusLabel);

    add(Box.createRigidArea(new Dimension(0, 20)));

    // 訂票資訊顯示
    bookingInfoTextArea = new JTextArea(10, 30);
    bookingInfoTextArea.setEditable(false);
    bookingInfoTextArea.setText(bookingInfo);
    JScrollPane scrollPane = new JScrollPane(bookingInfoTextArea);
    add(scrollPane);
  }

  private class PayAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // 獲取選擇的支付方式
      String selectedPaymentMethod = (String) paymentMethodComboBox.getSelectedItem();

      // 模擬支付過程
      boolean paymentSuccess = processPayment(selectedPaymentMethod);

      // 更新狀態標籤並顯示訂票資訊
      if (paymentSuccess) {
        statusLabel.setText("支付成功!");
        saveBookingInfoToCSV();
      } else {
        statusLabel.setText("支付失敗，請重試。");
      }
    }

    private boolean processPayment(String paymentMethod) {
      // 模擬支付處理邏輯，這裡可以加入實際的支付接口調用
      // 為了簡單起見，這裡假設支付總是成功
      return true;
    }

    private void saveBookingInfoToCSV() {
      try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
        writer.append(bookingInfo + "\n");
        bookingInfoTextArea.append("\n訂票資訊已保存到 " + CSV_FILE);
      } catch (IOException e) {
        e.printStackTrace();
        bookingInfoTextArea.append("\n訂票資訊保存失敗。");
      }
    }
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("電影票支付");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 600);

    // 模擬訂票資訊
    String bookingInfo = "電影名稱: Look store prepare.\n" +
            "影廳地址: 桃園市中壢區中華路二段123號\n" +
            "放映時間: 18:00\n" +
            "座位: 5\n" +
            "日期: 2023-08-09";

    frame.setContentPane(new PaymentPage(frame, bookingInfo));
    frame.setVisible(true);
  }
}
