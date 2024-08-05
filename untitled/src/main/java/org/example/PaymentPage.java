package org.example;

import javax.swing.*;

public class PaymentPage extends JPanel {

    public PaymentPage(JFrame parentFrame) {
        // 创建一个简单的标签，表示这是付款页面
        JLabel label = new JLabel("This is the Payment Page");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(label);
    }
}
