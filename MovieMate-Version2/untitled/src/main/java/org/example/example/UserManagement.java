package org.example.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private JFrame frame;
    private JTextField emailOrPhoneField, emailField, phoneField, usernameField, nameField, contactInfoField, preferencesField;
    private JPasswordField passwordField, registerPasswordField;
    private JButton loginButton, registerButton, updateButton, viewHistoryButton;

    private List<User> users = new ArrayList<>();
    private User currentUser;  // 保存当前登录用户
    private static final String CSV_FILE = "users.csv";

    public UserManagement() {
        loadUsersFromCSV();

        frame = new JFrame("MovieMate User Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailOrPhoneField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        loginPanel.add(loginTitle);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(createLabeledField("Email or Phone:", emailOrPhoneField));
        loginPanel.add(createLabeledField("Password:", passwordField));
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(loginButton);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabbedPane.addTab("Login", loginPanel);

        // Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel registerTitle = new JLabel("Register");
        registerTitle.setFont(new Font("Arial", Font.BOLD, 24));
        registerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        usernameField = new JTextField(20);
        registerPasswordField = new JPasswordField(20);
        nameField = new JTextField(20);
        contactInfoField = new JTextField(20);
        preferencesField = new JTextField(20);
        registerButton = new JButton("Register");

        registerPanel.add(registerTitle);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(createLabeledField("Email:", emailField));
        registerPanel.add(createLabeledField("Phone:", phoneField));
        registerPanel.add(createLabeledField("Username:", usernameField));
        registerPanel.add(createLabeledField("Password:", registerPasswordField));
        registerPanel.add(createLabeledField("Name:", nameField));
        registerPanel.add(createLabeledField("Contact Info:", contactInfoField));
        registerPanel.add(createLabeledField("Preferences:", preferencesField));
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(registerButton);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabbedPane.addTab("Register", registerPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);

        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());

        frame.setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private void loadUsersFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                User user = new User(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        fields[2],
                        fields[3],
                        fields[4],
                        fields[5],
                        fields[6],
                        fields[7]
                );
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserToCSV(User user) {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            writer.append(user.toCSV() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String emailOrPhone = emailOrPhoneField.getText();
            String password = new String(passwordField.getPassword());

            for (User user : users) {
                if ((user.getEmail().equals(emailOrPhone) || user.getPhone().equals(emailOrPhone)) && user.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful");
                    currentUser = user;  // 保存当前登录用户
                    switchToMovieQueryGUI();
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Invalid Credentials");
        }
    }

    private class RegisterAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText();
            String password = new String(registerPasswordField.getPassword());
            String name = nameField.getText();
            String contactInfo = contactInfoField.getText();
            String preferences = preferencesField.getText();

            int id = users.size() + 1;
            User newUser = new User(id, email, phone, username, password, name, contactInfo, preferences);
            users.add(newUser);
            saveUserToCSV(newUser);

            JOptionPane.showMessageDialog(frame, "Registration Successful");
        }
    }

    // 切换到MovieQueryGUI
    private void switchToMovieQueryGUI() {
        frame.getContentPane().removeAll();
        frame.add(new MovieQueryGUI());
        frame.revalidate();
        frame.repaint();
    }

    // 获取当前登录用户
    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserManagement::new);
    }
}

class User {
    private int id;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String name;
    private String contactInfo;
    private String preferences;

    public User(int id, String email, String phone, String username, String password, String name, String contactInfo, String preferences) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.name = name;
        this.contactInfo = contactInfo;
        this.preferences = preferences;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String toCSV() {
        return id + "," + email + "," + phone + "," + username + "," + password + "," + name + "," + contactInfo + "," + preferences;
    }
}
